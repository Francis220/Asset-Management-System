package com.iffi;

import java.time.LocalDate;

/**
 * Models an instance of a stock that is a put.
 * 
 * @author sinezanz & eallen
 *
 */

public class Call extends Option {

	private double marketPrice = getSharePrice();

	public Call(Stock stock, LocalDate purchaseDate, double strikePrice, double shareLimit, double premium,
			LocalDate strikeDate) {
		super(stock, purchaseDate, strikePrice, shareLimit, premium, strikeDate);

	}

	public double getCostBasis() {
		double cost = this.getShareLimit() * this.getPremium();
		return cost;
	}

	@Override
	public double getValue() {
		double value = 0;
		if (this.marketPrice <= this.strikePrice) {
			value = 0.00;
		} else if (this.marketPrice > this.strikePrice) {
			value = (this.marketPrice - this.strikePrice()) * this.getShareLimit();
		}
		return value;
	}

	public double getReturn() {
		double returnValue = this.getValue() - this.getCostBasis();
		return returnValue;
	}

	@Override
	public double getReturnPercentage() {
		double returnPercentage = (this.getReturn() / this.getCostBasis()) * 100;
		return returnPercentage;
	}

	@Override
	public String toString() {
		String call = "";

		if (this.marketPrice > this.getStrikePrice()) {

			call = "\n" + this.getCode() + "\t" + this.getLabel() + "\t\t" + "(Call)" + "\t\t\t"
					+ String.format("%.2f", this.getReturnPercentage()) + "%\t\t\t$"
					+ String.format("%.2f", this.getValue()) + "\nBuy up to: " + this.getShareLimit() + " @ $"
					+ this.getStrikePrice() + " till " + this.getPurchaseDate() + "\nPremium of: " + this.getPremium()
					+ "/share ( " + this.getCostBasis() + " total )" + "\nShare Price: " + this.getSharePrice()
					+ "\nShort Call Value: " + this.getShareLimit() + " shares" + "\n@ (" + this.getSharePrice() + "-"
					+ this.getStrikePrice() + "-" + this.getShareLimit() + " = " + this.getCostBasis() + ")";
		}

		else if (this.marketPrice < this.getStrikePrice()) {

			call = "\n" + this.getCode() + "\t" + this.getLabel() + "\t\t" + "(Call)" + "\t\t\t"
					+ String.format("%.2f", this.getReturnPercentage()) + "%\t\t\t$"
					+ String.format("%.2f", this.getValue()) + "\nBuy up to: " + this.getShareLimit() + " @ $"
					+ this.getStrikePrice() + " till " + this.getPurchaseDate() + "\nPremium of: " + this.getPremium()
					+ "/share ( " + this.getCostBasis() + " total )" + "\nShare Price: " + this.getSharePrice()
					+ "\nLong Call (not executed); total loss:  " + this.getCostBasis();

		}

		return call;
	}
}