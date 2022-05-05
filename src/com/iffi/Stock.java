package com.iffi;

import java.time.LocalDate;

/**
 * 
 * Creates a stock with symbol and share price of stock.
 * 
 * @author sinezanz & eallen
 *
 */

public class Stock extends Asset {
	private String symbol;
	private double sharePrice;
	private LocalDate purchaseDate;
	private double purchaseSharePrice;
	private double numberOfShares;
	private double dividendTotal;
	private double costBasis;

	public Stock(String code, String type, String label, String symbol, Double sharePrice) {
		super(code, type, label);
		this.symbol = symbol;
		this.sharePrice = sharePrice;
	}

	public Stock(Stock stock, LocalDate purchaseDate, double purchaseSharePrice, double numberOfShares,
			double dividendTotal) {
		this(stock.getCode(), stock.getType(), stock.getLabel(), stock.getSymbol(), stock.getSharePrice());
		this.purchaseDate = purchaseDate;
		this.purchaseSharePrice = purchaseSharePrice;
		this.numberOfShares = numberOfShares;
		this.dividendTotal = dividendTotal;
	}

	public String getSymbol() {
		return symbol;
	}

	public double getSharePrice() {
		return sharePrice;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	public double getDividendTotal() {
		return dividendTotal;
	}

	public double getNumberOfShares() {
		return numberOfShares;
	}

	public double getPurchaseSharePrice() {
		return purchaseSharePrice;
	}

	public double getCostBasis() {
		costBasis = (purchaseSharePrice * numberOfShares);
		return costBasis;
	}

	@Override
	public double getValue() {
		double value = this.sharePrice * this.numberOfShares + this.dividendTotal;
		return value;
	}

	public double getReturn() {
		double returnValue = (this.getValue() - this.getCostBasis());
		return returnValue;
	}

	@Override
	public double getFee() {
		return 0.00;
	}

	@Override
	public double getReturnPercentage() {
		double returnPercentage = (this.getValue() / this.getCostBasis()) * 100;
		return returnPercentage;
	}

	@Override
	public String toString() {
		String stock = "\n" + this.getCode() + "\t" + this.getLabel() + "\t\t" + "(Stock)" + "\t\t\t"
				+ String.format("%.2f", this.getReturnPercentage()) + "%\t\t\t$"
				+ String.format("%.2f", this.getValue()) + "\nCost Basis: " + this.getNumberOfShares() + " shares @ $"
				+ this.getPurchaseSharePrice() + " on " + this.getPurchaseDate() + "\nValue Basis: "
				+ this.getNumberOfShares() + " shares @ " + this.getSharePrice();
		return stock;
	}
}