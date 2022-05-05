package com.iffi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Reads and parses info of all people, assets, and accounts in the database
 * 
 * @author sinezanz & eallen
 */
public class DatabaseLoader {

	public static List<Person> loadPerson() {
		List<Person> result = new ArrayList<Person>();
		Connection conn = ConnectionPool.getConnection();

		String query = "select p.personCode as personCode, " + "p.firstName as firstName, p.lastName as lastName, "
				+ "e.email as email, ad.street as street, ad.city as city, "
				+ "s.state as state, ad.zipCode as zipCode, c.country as country " + "from Person p "
				+ "left join Email e on e.personId = p.personId "
				+ "right join Address ad on p.addressId = ad.addressId "
				+ "right join State s on s.stateId = ad.stateId "
				+ "right join Country c on c.countryId = ad.countryId;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				Person p = null;
				String code = rs.getString("personCode");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				Address a = new Address(rs.getString("street"), rs.getString("city"), rs.getString("state"),
						rs.getString("zipCode"), rs.getString("country"));
				String email = rs.getString("email");
				List<String> emails = new ArrayList<String>();
				emails.add(email);

				p = new Person(code, firstName, lastName, a, emails);
				result.add(p);
			}
			ps.close();
			rs.close();
			ConnectionPool.putConnection(conn);

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
		return result;

	}

	/**
	 * Returns a list of all assets in the database.
	 *
	 * @return
	 */

	public static List<Asset> loadAsset() {

		List<Asset> result = new ArrayList<Asset>();

		Connection conn = ConnectionPool.getConnection();

		String query = "select a.assetCode as assetCode, a.assetType as assetType, "
				+ "a.label as label, a.appraisedValue as appraisedValue, "
				+ "a.symbol as symbol, a.sharePrice as sharePrice, "
				+ "a.exchangeRate as exchangeRate, a.exchangeFeeRate as exchangeFeeRate from Asset a;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				Asset a = null;
				String code = rs.getString("assetCode");
				String type = rs.getString("assetType");
				String label = rs.getString("label");

				if (type.equals("S")) {
					String symbol = rs.getString("symbol");
					Double sharePrice = rs.getDouble("sharePrice");

					a = new Stock(code, type, label, symbol, sharePrice);

				} else if (type.equals("P")) {
					Double appraisedValue = (double) (rs.getInt("appraisedValue"));
					a = new Property(code, type, label, appraisedValue);

				} else if (type.equals("C")) {
					Double exchangeRate = rs.getDouble("exchangeRate");
					Double exchangeFeeRate = rs.getDouble("exchangeFeeRate");

					a = new Cryptocurrency(code, type, label, exchangeRate, exchangeFeeRate);
				}
				result.add(a);
			}
			ps.close();
			rs.close();
			ConnectionPool.putConnection(conn);

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * Returns a list of all accounts and their respective assets from the database.
	 *
	 * @return
	 */
	public static SortedList<Account> loadAccountAsset(Comparator<Account> cmp) {
		List<Asset> assetList = loadAsset();
		List<Person> personList = loadPerson();
		SortedList<Account> accountList = new SortedList<Account>(cmp);
		Connection conn = ConnectionPool.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		Person owner = null;
		Person manager = null;
		Person beneficiary = null;

		String query = "select acc.accountNumber, acc.accountType, owner.personCode, manager.personCode, "
				+ "beneficiary.personCode, ass.assetCode, a.typeOfStock, a.purchaseDate, a.purchasePrice, a.exchangeRate, "
				+ "a.strikePrice , a.shareLimitPremium, a.strikeDate, a.numOfCoins, a.purchaseSharePrice, a.numOfShares, "
				+ "a.dividendTotal from Account acc " + "left join AccountAsset a on acc.accountId = a.accountId "
				+ "left join Asset ass on ass.assetCode = a.assetCode "
				+ "join Person owner on owner.personId = acc.ownerId "
				+ "join Person manager on manager.personId = acc.managerId "
				+ "join Person beneficiary on beneficiary.personId = acc.beneficiaryId group by acc.accountNumber;";

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				List<Asset> accountAssets = new ArrayList<>();

				String accountNum = rs.getString("acc.accountNumber");
				String accountType = rs.getString("acc.accountType");
				String typeOfStock = rs.getString("a.typeOfStock");
				String assetCode = rs.getString("ass.assetCode");

				String ownerCode = rs.getString("owner.personCode");
				String managerCode = rs.getString("manager.personCode");
				String beneficiaryCode = rs.getString("beneficiary.personCode");

				for (Person p : personList) {
					if (p.getCode().equals(ownerCode)) {
						owner = p;
					}
					if (p.getCode().equals(managerCode)) {
						manager = p;
					}
					if (p.getCode().equals(beneficiaryCode)) {
						beneficiary = p;
					}
				}

				Asset a = null;

				for (Asset asset : assetList) {
					if (asset.getCode().equals(assetCode)) {
						a = asset;
					}
				}

				if (a instanceof Property) {
					LocalDate purchaseDate = LocalDate.parse(rs.getString("a.purchaseDate"));
					Double purchasePrice = rs.getDouble("a.purchasePrice");
					Property property = new Property((Property) a, purchaseDate, purchasePrice);

					accountAssets.add(property);

				} else if (a instanceof Cryptocurrency) {
					LocalDate purchaseDate = LocalDate.parse(rs.getString("a.purchaseDate"));
					Double purchasePrice = rs.getDouble("a.purchasePrice");
					Double numCoins = rs.getDouble("a.numOfCoins");

					Cryptocurrency crypto = new Cryptocurrency((Cryptocurrency) a, purchaseDate, purchasePrice,
							numCoins);

					accountAssets.add(crypto);

				} else if (a instanceof Stock) {

					if (typeOfStock.equals("P")) {
						LocalDate purchaseDate = LocalDate.parse(rs.getString("a.purchaseDate"));
						Double strikePrice = rs.getDouble("a.strikePrice");
						Double shareLimit = rs.getDouble("a.shareLimitPremium");
						Double premium = rs.getDouble("a.purchaseSharePrice");
						LocalDate strikeDate = LocalDate.parse(rs.getString("a.strikeDate"));
						Put put = new Put((Stock) a, purchaseDate, strikePrice, shareLimit, premium, strikeDate);

						accountAssets.add(put);
					}

					if (typeOfStock.equals("C")) {
						LocalDate purchaseDate = LocalDate.parse(rs.getString("a.purchaseDate"));
						Double strikePrice = rs.getDouble("a.strikePrice");
						Double shareLimit = rs.getDouble("a.shareLimitPremium");
						Double premium = rs.getDouble("a.purchaseSharePrice");
						LocalDate strikeDate = LocalDate.parse(rs.getString("a.strikeDate"));
						Call call = new Call((Stock) a, purchaseDate, strikePrice, shareLimit, premium, strikeDate);

						accountAssets.add(call);
					}
					if (typeOfStock.equals("S")) {
						LocalDate purchaseDate = LocalDate.parse(rs.getString("a.purchaseDate"));
						Double purchaseSharePrice = rs.getDouble("a.purchaseSharePrice");
						Double numberOfShares = rs.getDouble("a.numOfShares");
						Double dividendTotal = rs.getDouble("a.dividendTotal");
						Stock stock = new Stock((Stock) a, purchaseDate, purchaseSharePrice, numberOfShares,
								dividendTotal);

						accountAssets.add(stock);
					}
				}

				if (accountType.equals("P")) {
					ProAccount pro = new ProAccount(accountNum, owner, manager, beneficiary, accountAssets);

					accountList.insert(pro);

				} else if (accountType.equals("N")) {
					NoobAccount noob = new NoobAccount(accountNum, owner, manager, beneficiary, accountAssets);

					accountList.insert(noob);

				}
			}
			ps.close();
			rs.close();
			ConnectionPool.putConnection(conn);

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
		return accountList;
	}
}
