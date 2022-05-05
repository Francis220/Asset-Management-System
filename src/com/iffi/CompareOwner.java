package com.iffi;

import java.util.Comparator;

/**
 * Class that serves as a comparator to order account owners.
 * 
 * @author sinezanz && eallen
 *
 */

public class CompareOwner implements Comparator<Account> {

	@Override
	public int compare(Account a1, Account a2) {

		return a1.getOwner().getLastName().compareTo(a2.getOwner().getLastName());
	}

}
