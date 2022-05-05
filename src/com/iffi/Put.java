package com.iffi;

import java.time.LocalDate;

/**
 * Models an instance of a stock that is a put.
 * 
 * @author sinezanz & eallen
 *
 */

public class Put extends Option {

	private double marketPrice = getSharePrice();

	public Put(Stock stock, LocalDate purchaseDate, double strikePrice, double shareLimit, double premium,
			LocalDate strikeDate) {
		super(stock, purchaseDate, strikePrice, shareLimit, premium, strikeDate);
		// TODO Auto-generated constructor stub
	}

	public double getCostBasis() {
		double cost = this.getShareLimit() * this.getPremium();
		return cost;
	}

	public double getValue() {
		double value = 0;
		if (this.marketPrice <= this.strikePrice) {
			value = this.getCostBasis();
		}
		if (this.marketPrice > this.strikePrice) {
			value = ((this.strikePrice() - this.marketPrice) * this.getShareLimit()) + this.getCostBasis();
		}
		return value;
	}

	public double getReturn() {
		double returnValue = this.getValue();
		return returnValue;
	}

	@Override
	public double getReturnPercentage() {
		double returnPercentage = 0.00;
		if (this.getValue() > 0) {
			returnPercentage = +100;
		}
		if (this.getValue() < 0) {
			returnPercentage = -100;
		}

		if (this.getValue() == 0) {
			returnPercentage = 0;
		}
		return returnPercentage;
	}

	@Override
	public String toString() {

		String put = "";

		if (this.marketPrice > this.getStrikePrice()) {

			put = "\n" + this.getCode() + "\t" + this.getLabel() + "\t\t" + "(Put)" + "\t\t\t"
					+ String.format("%.2f", this.getReturnPercentage()) + "%\t\t\t$"
					+ String.format("%.2f", this.getValue()) + "\nSell up to: " + this.getShareLimit() + " @ $"
					+ this.getStrikePrice() + " till " + this.getPurchaseDate() + "\nPremium of: " + this.getPremium()
					+ "/share ( " + this.getCostBasis() + " total )" + "\nShare Price: " + this.getSharePrice()
					+ "\nShort Put Value: " + this.getShareLimit() + " shares" + "\n@ (" + this.getSharePrice() + "-"
					+ this.getStrikePrice() + "-" + this.getShareLimit() + " = " + this.getValue() + ")";
		}

		else if (this.marketPrice < this.getStrikePrice()) {

			put = "\n" + this.getCode() + "\t" + this.getLabel() + "\t\t" + "(Put)" + "\t\t\t"
					+ String.format("%.2f", this.getReturnPercentage()) + "%\t\t\t$"
					+ String.format("%.2f", this.getValue()) + "\nBuy up to: " + this.getShareLimit() + " @ $"
					+ this.getStrikePrice() + " till " + this.getPurchaseDate() + "\nPremium of: " + this.getPremium()
					+ "/share ( " + this.getCostBasis() + " total )" + "\nShare Price: " + this.getSharePrice()
					+ "\nLong Put Value:  " + this.getShareLimit() + " shares @ " + this.getPremium() + " = "
					+ this.getValue();

		}

		return put;
	}

}
