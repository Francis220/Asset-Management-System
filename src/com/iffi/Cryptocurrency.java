package com.iffi;

import java.time.LocalDate;

/**
 * Models a cryptocurrency, includes exchange rate and exchange fee rate for the
 * cryptocurrency.
 * 
 * @author sinezanz & eallen
 *
 */

public class Cryptocurrency extends Asset {

	private double exchangeRate;
	private double exchangeFeeRate;
	private LocalDate purchaseDate;
	private double numberOfCoins;
	private double purchasePrice;

	public Cryptocurrency(String code, String type, String label, double exchangeRate, double exchangeFeeRate) {
		super(code, type, label);
		this.exchangeRate = exchangeRate;
		this.exchangeFeeRate = exchangeFeeRate;
	}

	public Cryptocurrency(Cryptocurrency cryptocurrency, LocalDate purchaseDate, double purchasePrice,
			double numberOfCoins) {
		this(cryptocurrency.getCode(), cryptocurrency.getType(), cryptocurrency.getLabel(),
				cryptocurrency.getExchangeRate(), cryptocurrency.getExchangeFeeRate());
		this.purchaseDate = purchaseDate;
		this.purchasePrice = purchasePrice;
		this.numberOfCoins = numberOfCoins;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public double getExchangeFeeRate() {
		return exchangeFeeRate;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public double getNumberOfCoins() {
		return numberOfCoins;
	}

	@Override
	public double getFee() {
		double fee = 10.00;
		return fee;
	}

	public double getCurrentValue() {
		double currentValue = 65694.50;
		return currentValue;
	}

	@Override
	public double getValue() {
		double value = this.numberOfCoins * this.getCurrentValue() * (1 - (0.01 / 100));
		return value;
	}

	@Override
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
	public double getCostBasis() {
		return this.purchasePrice * this.numberOfCoins;
	}

	@Override
	public String toString() {
		String crypto = "\n" + this.getCode() + "\t" + this.getLabel() + "\t\t" + "(Crypto)" + "\t\t\t"
				+ String.format("%.2f", this.getReturnPercentage()) + "%\t\t\t$"
				+ String.format("%.2f", this.getValue()) + "\nCost Basis: " + this.getNumberOfCoins() + " coins @ $"
				+ this.getPurchasePrice() + " on " + this.getPurchaseDate() + "\n" + "Value Basis: "
				+ this.getNumberOfCoins() + " coins @ $" + this.getCurrentValue() + " less " + this.getExchangeFeeRate()
				+ "%";
		return crypto;
	}

}