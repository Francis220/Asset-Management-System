package com.iffi;

/**
 * Creates assets with code, type, and label for all asset types.
 * 
 * @author sinezanz
 *
 */

public abstract class Asset {
	private String code;
	private String type;
	private String label;

	public Asset(String code, String type, String label) {
		this.code = code;
		this.type = type;
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	public String getType() {
		return type;
	}

	public String getLabel() {
		return label;
	}

	public abstract double getFee();

	public abstract double getReturn();

	public abstract double getReturnPercentage();

	public abstract double getCostBasis();

	public abstract double getValue();

	public abstract String toString();

}
