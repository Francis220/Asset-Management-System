package com.iffi;

import java.util.List;

/**
 * Models an individual Pro account
 * 
 * @author sinezanz & eallen
 */

public class ProAccount extends Account implements Comparable<Account> {

	public ProAccount(String accountNum, Person owner, Person manager, Person beneficiary, List<Asset> asset) {
		super(accountNum, owner, manager, beneficiary, asset);
	}

	public ProAccount(String accountNum, Person owner, Person manager, Person beneficiary) {
		super(accountNum, owner, manager, beneficiary);
	}

	@Override
	public double getFees() {
		double fees = 0;
		for (Asset a : asset) {
			fees += a.getFee();
		}
		fees = fees - (fees * 0.25);
		return fees;
	}

	@Override
	public String toString() {
		String pro = "Pro Account " + this.getAccountNum();
		return pro;
	}

	@Override
	public int compareTo(Account o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
