package com.iffi;

import java.util.List;

/**
 * Generates summary and detailed reports from info in the database
 * 
 * @author sinezanz & eallen
 *
 */

public class PrintReport {

	public static void getSummaryReport(SortedList<Account> accountList) {

		double totalFees = 0;
		double totalReturnValue = 0;
		double totalValue = 0;
		double totalReturnPercentage = 0;

		System.out.println(
				"===============================================================================================================================");
		System.out.printf("%9s %20s %20s %12s %15s %18s %18s\n", "Account", "Owner", "Manager", "Fees", "Return",
				"Return %", "Value");

		for (int i = 0; i < accountList.getSize(); i++) {

			String accountNumber = accountList.getElementAt(i).getAccountNum();
			String owner = accountList.getElementAt(i).getOwner().getLastName() + ", "
					+ accountList.getElementAt(i).getOwner().getFirstName();
			String manager = accountList.getElementAt(i).getManager().getLastName() + ", "
					+ accountList.getElementAt(i).getManager().getFirstName();

			double fees = accountList.getElementAt(i).getFees();
			double returnValue = accountList.getElementAt(i).getReturn();
			double value = accountList.getElementAt(i).getValue();
			double costBasis = accountList.getElementAt(i).getCostBasis();

			totalFees += fees;
			totalReturnValue += returnValue;
			totalValue += value;
			totalReturnPercentage = (returnValue / costBasis) * 100;

			System.out.printf("%9s %20s %20s %12s $%15.2f $%18.2f %18.2f $ \n", accountNumber, owner, manager, fees,
					returnValue, totalReturnPercentage, value);

		}
		System.out.printf("%120s\n", "-----------------------------------------------------------------");
		System.out.printf("%50s $%15.2f $%15.2f $%35.2f $ \n", "Totals: ", totalFees, totalReturnValue, totalValue);
	}

	public static void getDetailedReport(List<Account> accountList) {

		System.out.println("Account Details");
		System.out.println("================================================================================");

		for (int i = 0; i < accountList.size(); i++) {
			System.out.println();
			System.out.println("||================================================||");
			System.out.println("\t" + accountList.get(i).toString());
			System.out.println("||================================================||");

			System.out.println("+-------+");
			System.out.println(" Owner");
			System.out.println("+-------+");
			System.out.println(
					accountList.get(i).getOwner().getFirstName() + ", " + accountList.get(i).getOwner().getLastName());
			System.out.println(accountList.get(i).getOwner().getEmail());
			System.out.println(accountList.get(i).getOwner().getAddress().printAddress());

			System.out.println("+-------+");
			System.out.println(" Manager");
			System.out.println("+-------+");
			System.out.println(accountList.get(i).getManager().getLastName() + ", "
					+ accountList.get(i).getManager().getFirstName());
			System.out.println(accountList.get(i).getManager().getEmail());
			System.out.println(accountList.get(i).getManager().getAddress().printAddress());

			System.out.println("+-------------+");
			System.out.println(" Beneficiary");
			System.out.println("+-------------+");
			if (accountList.get(i).getBeneficiary() != null) {
				System.out.println(accountList.get(i).getBeneficiary().getFirstName());
				System.out.println(accountList.get(i).getBeneficiary().getEmail());
				System.out.println(accountList.get(i).getBeneficiary().getAddress().printAddress());
			}

			System.out.println("+-------+");
			System.out.println(" Assets");
			System.out.println("+-------+");
			accountList.get(i).getAccountAssetsDetail();

			double fees = accountList.get(i).getFees();
			double returnValue = accountList.get(i).getReturn();
			double value = accountList.get(i).getValue();
			double costBasis = accountList.get(i).getCostBasis();
			double returnPercentage = accountList.get(i).getReturnPercentage();

			System.out.println();
			System.out.println("+--------+");
			System.out.println(" Totals");
			System.out.println("+--------+");
			System.out.printf("%9s \t $%20.2f \n", "Total Value:", value);
			System.out.printf("%9s \t $%20.2f \n", "Cost Basis:", costBasis);
			System.out.printf("%9s \t $%20.2f \n", "Total Account Fees:", fees);
			System.out.printf("%9s \t $%20.2f \n", "Total Return:", returnValue);
			System.out.printf("%9s \t %20.2f \n", "Total Return %:", returnPercentage);
		}
	}
}
