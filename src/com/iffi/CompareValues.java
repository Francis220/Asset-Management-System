package com.iffi;

import java.util.Comparator;

/**
 * Class that serves as a comparator to order account values.
 * 
 * @author sinezanz && eallen
 *
 */

public class CompareValues implements Comparator<Account> {
	
	@Override
	public int compare(Account a, Account b) {
		return (int) (a.getValue() - b.getValue());
	}
}
