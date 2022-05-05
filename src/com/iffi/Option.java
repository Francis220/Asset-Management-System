package com.iffi;

import java.time.LocalDate;

/**
 * Models an option stock with information from purchase.
 * 
 * @author sinezanz & eallen
 *
 */

public abstract class Option extends Stock {

	private LocalDate purchaseDate;
	protected double strikePrice;
	protected double shareLimit;
	protected double premium;
	private LocalDate strikeDate;
	private double sharePrice;

	public Option(Stock stock, LocalDate purchaseDate, double strikePrice, double shareLimit, double premium,
			LocalDate strikeDate) {
		super(stock.getCode(), stock.getType(), stock.getLabel(), stock.getSymbol(), stock.getSharePrice());
		this.purchaseDate = purchaseDate;
		this.strikePrice = strikePrice;
		this.shareLimit = shareLimit;
		this.premium = premium;
		this.strikeDate = strikeDate;
		this.sharePrice = stock.getSharePrice();
	}

	public double getStrikePrice() {
		return strikePrice;
	}

	public double getShareLimit() {
		return shareLimit;
	}

	public double getPremium() {
		return premium;
	}

	public LocalDate getStrikeDate() {
		return strikeDate;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	public double strikePrice() {
		return strikePrice;
	}

	public double shareLimit() {
		return shareLimit;
	}

	public double sharePrice() {
		return sharePrice;
	}

	public abstract double getReturn();

	public abstract double getReturnPercentage();

	public abstract double getValue();

	public abstract String toString();
}