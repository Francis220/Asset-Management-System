package com.iffi;

import java.util.Comparator;

/**
 * Class that serves as a comparator to order account managers.
 * 
 * @author sinezanz && eallen
 *
 */

public class CompareManager implements Comparator<Account> {
	
	@Override
	public int compare(Account a1, Account a2) {
		
		return a1.getManager().getLastName().compareTo(a2.getManager().getLastName());
	}

}
