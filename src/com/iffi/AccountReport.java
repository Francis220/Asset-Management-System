package com.iffi;

/**
 * Takes an account list and prints out a summary report to the
 * standard output organized by owners, managers, account values, and account return rates
 * 
 * @author sinezanz & eallen
 */

public class AccountReport{
	
	
	public static void main(String[] args) {
		ConnectionPool.LOG.info("Loading and creating reports");
		
		SortedList<Account> accountList = new SortedList<Account>();
		
		System.out.println("");
		System.out.println("Account Summary Report by Owner's Last Name");
		accountList = DatabaseLoader.loadAccountAsset(new CompareOwner().reversed());
		PrintReport.getSummaryReport(accountList);
		
		System.out.println("");
		System.out.println("Account Summary Report by Manager's Last Name");
		accountList = DatabaseLoader.loadAccountAsset(new CompareManager().reversed());
		PrintReport.getSummaryReport(accountList);
		
		System.out.println("");
		System.out.println("Account Summary Report by Value");
		accountList = DatabaseLoader.loadAccountAsset(new CompareValues());
		PrintReport.getSummaryReport(accountList);
		
		System.out.println("");
		System.out.println("Account Summary Report by Return Rate");
		accountList = DatabaseLoader.loadAccountAsset(new CompareReturnRates());
		PrintReport.getSummaryReport(accountList);
		
	}
}
