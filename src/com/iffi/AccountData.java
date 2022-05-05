package com.iffi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 * @author sinezanz & eallen
 */

public class AccountData {

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		Connection conn = ConnectionPool.getConnection();
		PreparedStatement ps = null;

		List<String> tables = Arrays.asList("AccountAsset", "Asset", "Account", "Email", "Person", "Address", "State",
				"Country");

		for (String table : tables) {
			String query = "Delete from " + table + ";";

			try {
				ps = conn.prepareStatement(query);
				ps.executeUpdate();
			} catch (SQLException e) {
				ConnectionPool.LOG.error("SQL Exception: ", e);
				throw new RuntimeException(e);
			}
		}
		try {
			ps.close();
			ConnectionPool.putConnection(conn);
		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addPerson(String personCode, String firstName, String lastName, String street, String city,
			String state, String zip, String country) {
		int stateId = -1;
		int countryId = -1;
		int addressId = -1;

		Connection conn = ConnectionPool.getConnection();

		/*
		 * Insert a state and get its stateId
		 */

		String searchState = "select stateId from State where state = ?;";
		String query = "insert into State(state) values (?);";
		PreparedStatement ps = null;
		ResultSet rs1 = null;

		try {
			ps = conn.prepareStatement(searchState);
			ps.setString(1, state);
			rs1 = ps.executeQuery();

			if (rs1.next()) {
				stateId = rs1.getInt("stateId");
			}

			else {

				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, state);
				ps.executeUpdate();
				ResultSet stateKey = ps.getGeneratedKeys();
				stateKey.next();
				stateId = stateKey.getInt(1);
			}

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

		/*
		 * Insert a country and get its countryId
		 */

		String countryQuery = "insert into Country(country) values(?);";
		String searchCountry = "select countryId from Country where country = ?;";
		ResultSet rs2 = null;

		try {

			ps = conn.prepareStatement(searchCountry);
			ps.setString(1, country);
			rs2 = ps.executeQuery();

			if (rs2.next()) {
				countryId = rs2.getInt("countryId");
			}

			ps = conn.prepareStatement(countryQuery, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, country);
			ps.executeUpdate();
			ResultSet countryKey = ps.getGeneratedKeys();
			countryKey.next();
			countryId = countryKey.getInt(1);

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

		/*
		 * Insert an address with the stateId and countryId and get its addressId
		 */

		String addressQuery = "insert into Address(street, city, stateId, zipCode, countryId) values (?, ?, ?, ?, ?);";

		try {
			ps = conn.prepareStatement(addressQuery, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setInt(3, stateId);
			ps.setString(4, zip);
			ps.setInt(5, countryId);
			ps.executeUpdate();
			ResultSet addressKey = ps.getGeneratedKeys();
			addressKey.next();
			addressId = addressKey.getInt(1);

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

		/*
		 * Insert a person with all the details
		 */

		String personQuery = "insert into Person (personCode, firstName, lastName, addressId) values (?, ?, ?, ?);";

		try {
			ps = conn.prepareStatement(personQuery);
			ps.setString(1, personCode);
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setInt(4, addressId);
			ps.executeUpdate();

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			rs1.close();
			rs2.close();
			ConnectionPool.putConnection(conn);

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 *
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		int personId = 0;
		Connection conn = ConnectionPool.getConnection();

		/*
		 * Get PersonId from PersonCode
		 */
		String getPersonId = "select p.personCode, p.personId from Person p where personCode = ?;";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(getPersonId);
			ps.setString(1, personCode);
			rs = ps.executeQuery();

			if (rs.next()) {
				personId = rs.getInt("p.personId");
			}

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

		/*
		 * insert into email with the corresponding personId
		 */

		String emailQuery = "insert into Email (personId, email) values (?, ?);";

		try {
			ps = conn.prepareStatement(emailQuery);
			ps.setInt(1, personId);
			ps.setString(2, email);
			ps.executeUpdate();

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

		try {
			rs.close();
			ps.close();
			ConnectionPool.putConnection(conn);
		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a crypto currency asset record to the database with the provided data.
	 *
	 * @param assetCode
	 * @param label
	 * @param exchangeRate
	 * @param exchangeFeeRate
	 */
	public static void addCrypto(String assetCode, String label, double exchangeRate, double exchangeFeeRate) {
		Connection conn = ConnectionPool.getConnection();

		String addCryptoQuery = "insert into Asset (assetCode, assetType, label, appraisedValue, symbol, sharePrice, exchangeRate, exchangeFeeRate) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(addCryptoQuery);
			ps.setString(1, assetCode);
			ps.setString(2, "C");
			ps.setString(3, label);
			ps.setDouble(4, 0);
			ps.setString(5, null);
			ps.setDouble(6, 0);
			ps.setDouble(7, exchangeRate);
			ps.setDouble(8, exchangeFeeRate);
			ps.executeUpdate();

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

		try {
			ps.close();
			ConnectionPool.putConnection(conn);
		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a property asset record to the database with the provided data.
	 *
	 * @param assetCode
	 * @param label
	 * @param appraisedValue
	 */
	public static void addProperty(String assetCode, String label, double appraisedValue) {

		Connection conn = ConnectionPool.getConnection();

		String addPropertyQuery = "insert into Asset (assetCode, assetType, label, appraisedValue, symbol, sharePrice, exchangeRate, exchangeFeeRate) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(addPropertyQuery);
			ps.setString(1, assetCode);
			ps.setString(2, "P");
			ps.setString(3, label);
			ps.setDouble(4, appraisedValue);
			ps.setString(5, null);
			ps.setDouble(6, 0);
			ps.setDouble(7, 0);
			ps.setDouble(8, 0);
			ps.executeUpdate();

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			ConnectionPool.putConnection(conn);
		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a stock asset record to the database with the provided data.
	 *
	 * @param assetCode
	 * @param label
	 * @param stockSymbol
	 * @param sharePrice
	 */
	public static void addStock(String assetCode, String label, String stockSymbol, Double sharePrice) {
		Connection conn = ConnectionPool.getConnection();

		String addStockQuery = "insert into Asset (assetCode, assetType, label, appraisedValue, symbol, sharePrice, exchangeRate, exchangeFeeRate) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(addStockQuery);
			ps.setString(1, assetCode);
			ps.setString(2, "S");
			ps.setString(3, label);
			ps.setDouble(4, 0);
			ps.setString(5, stockSymbol);
			ps.setDouble(6, sharePrice);
			ps.setDouble(7, 0);
			ps.setDouble(8, 0);
			ps.executeUpdate();

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			ConnectionPool.putConnection(conn);
		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an account record to the database with the given data. If the account
	 * has no beneficiary, the <code>beneficiaryCode</code> will be
	 * <code>null</code>. The <code>accountType</code> is expected to be either
	 * <code>"N"</code> (noob) or <code>"P"</code> (pro).
	 *
	 * @param accountNumber
	 * @param accountType
	 * @param ownerCode
	 * @param managerCode
	 * @param beneficiaryCode
	 */
	public static void addAccount(String accountNumber, String accountType, String ownerCode, String managerCode,
			String beneficiaryCode) {
		Connection conn = ConnectionPool.getConnection();
		int ownerId = 0;
		int managerId = 0;
		int beneficiaryId = 0;

		/*
		 * Get PersonId from ownerCode
		 */
		String getOwnerId = "select personCode, personId as ownerId from Person where personCode = ?;";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(getOwnerId);
			ps.setString(1, ownerCode);
			rs = ps.executeQuery();
			while (rs.next()) {
				ownerId = rs.getInt("ownerId");
			}
			rs.close();

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

		/*
		 * Get PersonId from managerCode
		 */
		String getManagerId = "select personCode, personId as managerId from Person where personCode = ?;";

		try {
			ps = conn.prepareStatement(getManagerId);
			ps.setString(1, managerCode);
			rs = ps.executeQuery();
			while (rs.next()) {
				managerId = rs.getInt("managerId");
			}
			rs.close();

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

		/*
		 * Get PersonId from beneficiaryCode
		 */
		String getBeneficiaryId = "select personCode, personId as beneficiaryId from Person where personCode = ?;";

		try {
			ps = conn.prepareStatement(getBeneficiaryId);

			if (beneficiaryCode == null || beneficiaryCode == "") {
				return;
			}

			else {
				ps.setString(1, beneficiaryCode);
				rs = ps.executeQuery();

				while (rs.next()) {
					beneficiaryId = rs.getInt("beneficiaryId");
				}
			}

			rs.close();

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

		/*
		 * Insert Account
		 */

		String insertAccount = "insert into Account(accountNumber, accountType, ownerId, managerId, beneficiaryId) values(?, ?, ?, ?, ?) ";
		try {
			ps = conn.prepareStatement(insertAccount);
			ps.setString(1, accountNumber);
			ps.setString(2, accountType);
			ps.setInt(3, ownerId);
			ps.setInt(4, managerId);
			ps.setInt(5, beneficiaryId);
			ps.executeUpdate();

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

		try {
			ps.close();
			ConnectionPool.putConnection(conn);
		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Helper method that returns the auto-generated accountId from the given
	 * account number
	 * 
	 * @param accountNumber
	 * @return
	 */

	public static int getAccountIdFromAccountNum(String accountNumber) {
		Connection conn = ConnectionPool.getConnection();
		int accountId = 0;

		String getAccountId = "select accountId, accountNumber from Account where accountNumber = ?;";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(getAccountId);
			ps.setString(1, accountNumber);
			rs = ps.executeQuery();
			while (rs.next()) {
				accountId = rs.getInt("accountId");
			}

		} catch (SQLException e) {
			ConnectionPool.LOG.error("Get accountId Error: ", e);
			throw new RuntimeException(e);
		}
		try {
			rs.close();
			ps.close();
			ConnectionPool.putConnection(conn);
		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
		return accountId;
	}

	/**
	 * Helper method that returns an auto-generated assetId from the given assetCode
	 * 
	 * @param assetCode
	 * @return
	 */
	public static int getAssetIdFromAssetCode(String assetCode) {
		Connection conn = ConnectionPool.getConnection();
		int assetId = 0;

		String getAssetId = "select assetId, assetCode from Asset ass where assetCode = ?;";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(getAssetId);
			ps.setString(1, assetCode);
			rs = ps.executeQuery();
			if (rs.next()) {

				assetId = rs.getInt("assetId");
			}

		} catch (SQLException e) {
			ConnectionPool.LOG.error("Getting assetId Error: ", e);
			throw new RuntimeException(e);
		}

		try {
			rs.close();
			ps.close();
			ConnectionPool.putConnection(conn);
		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
		return assetId;

	}

	/**
	 * Adds a crypto currency asset corresponding to <code>assetCode</code> (which
	 * is assumed to already exist in the database) to the account corresponding to
	 * the provided <code>accountNumber</code>.
	 *
	 * @param accountNumber
	 * @param assetCode
	 * @param purchaseDate
	 * @param purchaseExchangeRate
	 * @param numberOfCoins
	 */
	public static void addCryptoToAccount(String accountNumber, String assetCode, String purchaseDate,
			double purchaseExchangeRate, double numberOfCoins) {

		Connection conn = ConnectionPool.getConnection();
		String addCryptoAccount = "insert into AccountAsset(assetId, accountId, assetCode, typeOfStock, purchaseDate, purchasePrice, "
				+ "exchangeRate, strikePrice, shareLimitPremium,strikeDate, numOfCoins, purchaseSharePrice, numOfShares, dividendTotal) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(addCryptoAccount);
			ps.setInt(1, getAssetIdFromAssetCode(assetCode));
			ps.setInt(2, getAccountIdFromAccountNum(accountNumber));
			ps.setString(3, assetCode);
			ps.setString(4, null);
			ps.setString(5, purchaseDate);
			ps.setDouble(6, 0);
			ps.setDouble(7, purchaseExchangeRate);
			ps.setDouble(8, 0);
			ps.setDouble(9, 0);
			ps.setDouble(10, 0);
			ps.setDouble(11, numberOfCoins);
			ps.setDouble(12, 0);
			ps.setDouble(13, 0);
			ps.setDouble(14, 0);
			ps.executeUpdate();

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			ConnectionPool.putConnection(conn);
		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a property asset corresponding to <code>assetCode</code> (which is
	 * assumed to already exist in the database) to the account corresponding to the
	 * provided <code>accountNumber</code>.
	 *
	 * @param accountNumber
	 * @param assetCode
	 * @param purchaseDate
	 * @param purchasePrice
	 */
	public static void addPropertyToAccount(String accountNumber, String assetCode, String purchaseDate,
			double purchasePrice) {

		Connection conn = ConnectionPool.getConnection();

		String addPropertyAccount = "insert into AccountAsset(assetId, accountId, assetCode, typeOfStock, purchaseDate, purchasePrice, "
				+ "exchangeRate, strikePrice, shareLimitPremium,strikeDate, numOfCoins, purchaseSharePrice, numOfShares, dividendTotal) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(addPropertyAccount);
			ps.setInt(1, getAssetIdFromAssetCode(assetCode));
			ps.setInt(2, getAccountIdFromAccountNum(accountNumber));
			ps.setString(3, assetCode);
			ps.setString(4, null);
			ps.setString(5, purchaseDate);
			ps.setDouble(6, purchasePrice);
			ps.setDouble(7, 0);
			ps.setDouble(8, 0);
			ps.setDouble(9, 0);
			ps.setDouble(10, 0);
			ps.setDouble(11, 0);
			ps.setDouble(12, 0);
			ps.setDouble(13, 0);
			ps.setDouble(14, 0);
			ps.executeUpdate();

		} catch (SQLException e) {
			ConnectionPool.LOG.error("Adding property to account error ", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			ConnectionPool.putConnection(conn);
		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a stock asset corresponding to <code>assetCode</code> (which is assumed
	 * to already exist in the database) to the account corresponding to the
	 * provided <code>accountNumber</code>.
	 *
	 * @param accountNumber
	 * @param assetCode
	 * @param purchaseDate
	 * @param purchaseSharePrice
	 * @param numberOfShares
	 * @param dividendTotal
	 */
	public static void addStockToAccount(String accountNumber, String assetCode, String purchaseDate,
			double purchaseSharePrice, double numberOfShares, double dividendTotal) {

		Connection conn = ConnectionPool.getConnection();
		String addStockAccount = "insert into AccountAsset(assetId, accountId, assetCode, typeOfStock, purchaseDate, purchasePrice, "
				+ "exchangeRate, strikePrice, shareLimitPremium,strikeDate, numOfCoins, purchaseSharePrice, numOfShares, dividendTotal) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(addStockAccount);
			ps.setInt(1, getAssetIdFromAssetCode(assetCode));
			ps.setInt(2, getAccountIdFromAccountNum(accountNumber));
			ps.setString(3, assetCode);
			ps.setString(4, "S");
			ps.setString(5, purchaseDate);
			ps.setDouble(6, 0);
			ps.setDouble(7, 0);
			ps.setDouble(8, 0);
			ps.setDouble(9, 0);
			ps.setString(10, null);
			ps.setDouble(11, 0);
			ps.setDouble(12, purchaseSharePrice);
			ps.setDouble(13, numberOfShares);
			ps.setDouble(14, dividendTotal);
			ps.executeUpdate();

		} catch (SQLException e) {
			ConnectionPool.LOG.error("Adding stock to account error", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			ConnectionPool.putConnection(conn);
		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a stock option asset corresponding to <code>assetCode</code> (which is
	 * assumed to already exist in the database) to the account corresponding to the
	 * provided <code>accountNumber</code>.
	 *
	 * @param accountNumber
	 * @param assetCode
	 * @param purchaseDate
	 * @param purchaseSharePrice
	 * @param numberOfShares
	 * @param dividendTotal
	 */
	public static void addStockOptionToAccount(String accountNumber, String assetCode, String purchaseDate,
			String optionType, String strikeDate, double shareLimit, double premiumPerShare,
			double strikePricePerShare) {

		Connection conn = ConnectionPool.getConnection();

		String addStockOptionAccount = "insert into AccountAsset(assetId, accountId, assetCode, typeOfStock, purchaseDate, purchasePrice, "
				+ "exchangeRate, strikePrice, shareLimitPremium,strikeDate, numOfCoins, purchaseSharePrice, numOfShares, dividendTotal) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(addStockOptionAccount);
			ps.setInt(1, getAssetIdFromAssetCode(assetCode));
			ps.setInt(2, getAccountIdFromAccountNum(accountNumber));
			ps.setString(3, assetCode);
			ps.setString(4, optionType);
			ps.setString(5, purchaseDate);
			ps.setDouble(6, 0);
			ps.setDouble(7, 0);
			ps.setDouble(8, strikePricePerShare);
			ps.setDouble(9, shareLimit);
			ps.setString(10, strikeDate);
			ps.setDouble(11, 0);
			ps.setDouble(12, premiumPerShare);
			ps.setDouble(13, 0);
			ps.setDouble(14, 0);
			ps.executeUpdate();

		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			ConnectionPool.putConnection(conn);
		} catch (SQLException e) {
			ConnectionPool.LOG.error("SQL Exception: ", e);
			throw new RuntimeException(e);
		}

	}

}
