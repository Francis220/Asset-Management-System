package com.iffi;

import java.util.Comparator;

/**
 * Class that serves as a comparator to order account return rates.
 * 
 * @author sinezanz && eallen
 *
 */

public class CompareReturnRates implements Comparator<Account> {
	
	@Override
	public int compare(Account a, Account b) {
		return (int) (a.getReturnPercentage() - b.getReturnPercentage());
	}

}
