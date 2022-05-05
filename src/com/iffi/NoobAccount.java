package com.iffi;

import java.util.List;

/**
 * Models an individual Noob account
 * 
 * @author sinezanz & eallen
 */

public class NoobAccount extends Account implements Comparable<Account> {

	public NoobAccount(String accountNum, Person owner, Person manager, Person beneficiary, List<Asset> asset) {
		super(accountNum, owner, manager, beneficiary, asset);
	}

	public NoobAccount(String accountNum, Person owner, Person manager, Person beneficiary) {
		super(accountNum, owner, manager, beneficiary);
	}

	@Override
	public double getFees() {
		double fees = 0.00;

		for (Asset a : asset) {
			fees += a.getFee();
		}
		return fees;
	}

	@Override
	public String toString() {
		String pro = "Noob Account " + this.getAccountNum();
		return pro;
	}

	@Override
	public int compareTo(Account o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
