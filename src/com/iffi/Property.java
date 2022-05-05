package com.iffi;

import java.time.LocalDate;

/**
 * Creates and models a property.
 * 
 * @author sinezanz & eallen
 */

public class Property extends Asset {
	private final double appraisedValue;
	private LocalDate purchaseDate;
	private double purchasePrice;

	public Property(String code, String type, String label, double appraisedValue) {
		super(code, type, label);
		this.appraisedValue = appraisedValue;
	}

	public Property(Property property, LocalDate purchaseDate, double purchasePrice) {
		this(property.getCode(), property.getType(), property.getLabel(), property.getAppraisedValue());
		this.purchaseDate = purchaseDate;
		this.purchasePrice = purchasePrice;
	}

	public double getAppraisedValue() {
		return appraisedValue;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	@Override
	public double getCostBasis() {
		return this.purchasePrice;
	}

	@Override
	public double getReturn() {
		double result = this.appraisedValue - this.getCostBasis();
		return result;
	}

	public double getReturnPercentage() {
		double returnPercentage = (this.getReturn() / this.getCostBasis()) * 100;
		return returnPercentage;
	}

	@Override
	public double getFee() {
		double fee = 100.00;
		return fee;
	}

	@Override
	public double getValue() {
		return this.appraisedValue;
	}

	@Override
	public String toString() {
		String property = "\n" + this.getCode() + "\t" + this.getLabel() + "\t\t" + "(Property)" + "\t\t\t"
				+ String.format("%.2f", this.getReturnPercentage()) + "%\t\t\t$" + this.getValue()
				+ "\nCost Basis: purchased @ $" + this.getCostBasis() + " on " + this.getPurchaseDate()
				+ ("\nValue Basis: appraised @ $" + this.getAppraisedValue());
		return property;
	}

}