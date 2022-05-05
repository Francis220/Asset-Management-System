package com.iffi;

import java.util.List;

/**
 * Models an account, including different information to identify the account.
 * 
 * @author sinezanz & eallen
 */

public abstract class Account{

	private String accountNum;
	protected List<Asset> asset;
	private Person owner;
	private Person manager;
	private Person beneficiary;

	public Account(String accountNum, Person owner, Person manager, Person beneficiary, List<Asset> asset) {
		this.accountNum = accountNum;
		this.owner = owner;
		this.manager = manager;
		this.beneficiary = beneficiary;
		this.asset = asset;
	}

	public Account(String accountNum, Person owner, Person manager, Person beneficiary) {

	}

	public Person getOwner() {
		return owner;
	}

	public Person getManager() {
		return manager;
	}

	public Person getBeneficiary() {
		return beneficiary;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public List<Asset> getAssets() {
		return asset;
	}

	public abstract double getFees();

	public double getReturn() {
		double returnValue = 0.00;
		for (Asset a : asset) {
			returnValue += a.getReturn();
		}
		return returnValue;
	}

	public double getReturnPercentage() {
		double returnPercentage = 0.00;
		for (Asset a : asset) {
			returnPercentage += a.getReturnPercentage();
		}
		return returnPercentage;
	}

	public double getValue() {
		double value = 0;
		for (Asset a : asset) {
			value += a.getValue();
		}
		return value;
	}

	public double getCostBasis() {
		double costBasis = 0;
		for (Asset a : asset) {
			costBasis += a.getCostBasis();
		}
		return costBasis;
	}
	
	public abstract String toString();

	public void getAccountAssetsDetail() {

		for (int i = 0; i < this.getAssets().size(); i++) {
			System.out.println(this.getAssets().get(i).toString());
		}
	}
}
