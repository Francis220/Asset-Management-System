package com.iffi;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Reads and parses person. asset, and account data from csv files
 * 
 * @author sinezanz & eallen
 *
 */

public class FileReader {

	public static List<Person> parsePersonFile() {
		List<Person> result = new ArrayList<Person>();
		File f = new File("data/Persons.csv");
		try (Scanner s = new Scanner(f)) {
			s.nextLine();
			while (s.hasNext()) {
				String line = s.nextLine();
				if (!line.trim().isEmpty()) {

					Person p = null;
					String tokens[] = line.split(",");
					String code = tokens[0];
					String firstName = tokens[2];
					String lastName = tokens[1];
					Address a = new Address(tokens[3], tokens[4], tokens[5], tokens[6], tokens[7]);
					List<String> emails = new ArrayList<String>();
					if (tokens.length > 8) {

						for (int i = 8; i < tokens.length; i++) {
							emails.add(tokens[i]);
						}
					}
					p = new Person(code, firstName, lastName, a, emails);
					result.add(p);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static List<Asset> parseAssetFile() {
		List<Asset> result = new ArrayList<Asset>();
		File f = new File("data/Assets.csv");
		try (Scanner s = new Scanner(f)) {
			s.nextLine();
			while (s.hasNext()) {
				String line = s.nextLine();
				if (!line.trim().isEmpty()) {

					Asset a = null;
					String tokens[] = line.split(",");
					String code = tokens[0];
					String type = tokens[1];
					String label = tokens[2];

					if (tokens[1].equals("S")) {
						String symbol = tokens[3];
						Double sharePrice = Double.parseDouble(tokens[4]);

						a = new Stock(code, type, label, symbol, sharePrice);

					} else if (tokens[1].equals("P")) {
						Double appraisedValue = Double.parseDouble(tokens[3]);
						a = new Property(code, type, label, appraisedValue);

					} else if (tokens[1].equals("C")) {
						Double exchangeRate = Double.parseDouble(tokens[3]);
						Double exchangeFeeRate = Double.parseDouble(tokens[4]);

						a = new Cryptocurrency(code, type, label, exchangeRate, exchangeFeeRate);
					}
					result.add(a);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	public static List<Account> parseAccounts() {

		List<Asset> assetList = parseAssetFile();
		List<Person> personList = parsePersonFile();
		List<Account> accountList = new ArrayList<Account>();

		File f = new File("data/Accounts.csv");
		try (Scanner scanner = new Scanner(f)) {

			int numAccounts = Integer.parseInt(scanner.nextLine());

			for (int i = 0; i < numAccounts; i++) {
				List<Asset> accountAssets = new ArrayList<>();

				Person owner = null;
				Person manager = null;
				Person beneficiary = null;

				String line = scanner.nextLine();
				String tokens[] = line.split(",", -1);
				String accountNum = tokens[0];
				String type = tokens[1];
				String ownerCode = tokens[2];
				String managerCode = tokens[3];
				String beneficiaryCode = tokens[4];

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
				for (int j = 5; j < tokens.length;) {
					Asset a = null;
					for (Asset asset : assetList) {
						if (asset.getCode().equals(tokens[j])) {
							a = asset;
						}
					}

					if (a instanceof Property) {
						LocalDate purchaseDate = LocalDate.parse(tokens[j + 1]);
						Double purchasePrice = Double.parseDouble(tokens[j + 2]);
						Property property = new Property((Property) a, purchaseDate, purchasePrice);

						accountAssets.add(property);
						j += 3;

					} else if (a instanceof Cryptocurrency) {
						LocalDate purchaseDate = LocalDate.parse(tokens[j + 1]);
						Double purchasePrice = Double.parseDouble(tokens[j + 2]);
						Double numCoins = Double.parseDouble(tokens[j + 3]);

						Cryptocurrency crypto = new Cryptocurrency((Cryptocurrency) a, purchaseDate, purchasePrice,
								numCoins);

						accountAssets.add(crypto);
						j += 4;

					} else if (a instanceof Stock) {
						String spc = tokens[j + 1];
						if (spc.contains("P")) {
							LocalDate purchaseDate = LocalDate.parse(tokens[j + 2]);
							Double strikePrice = Double.parseDouble(tokens[j + 3]);
							Double shareLimit = Double.parseDouble(tokens[j + 4]);
							Double premium = Double.parseDouble(tokens[j + 5]);
							LocalDate strikeDate = LocalDate.parse(tokens[j + 6]);
							Put put = new Put((Stock) a, purchaseDate, strikePrice, shareLimit, premium, strikeDate);

							accountAssets.add(put);
							j += 7;
						}

						if (spc.contains("C")) {
							LocalDate purchaseDate = LocalDate.parse(tokens[j + 2]);
							Double strikePrice = Double.parseDouble(tokens[j + 3]);
							Double shareLimit = Double.parseDouble(tokens[j + 4]);
							Double premium = Double.parseDouble(tokens[j + 5]);
							LocalDate strikeDate = LocalDate.parse(tokens[j + 6]);
							Call call = new Call((Stock) a, purchaseDate, strikePrice, shareLimit, premium, strikeDate);

							accountAssets.add(call);
							j += 7;
						}
						if (spc.contains("S")) {
							LocalDate purchaseDate = LocalDate.parse(tokens[j + 2]);
							Double purchaseSharePrice = Double.parseDouble(tokens[j + 3]);
							Double numberOfShares = Double.parseDouble(tokens[j + 4]);
							Double dividendTotal = Double.parseDouble(tokens[j + 5]);
							Stock stock = new Stock((Stock) a, purchaseDate, purchaseSharePrice, numberOfShares,
									dividendTotal);

							accountAssets.add(stock);
							j += 6;
						}
					}
				}

				if (type.equalsIgnoreCase("P")) {
					ProAccount p = new ProAccount(accountNum, owner, manager, beneficiary, accountAssets);
					accountList.add(p);
				} else {
					NoobAccount n = new NoobAccount(accountNum, owner, manager, beneficiary, accountAssets);
					accountList.add(n);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(accountList);

		return accountList;
	}

	public static void main(String[] args) {
		parseAccounts();
	}

}
