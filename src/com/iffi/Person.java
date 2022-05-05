package com.iffi;

import java.util.List;

/**
 * Models a person and includes different factors for identification.
 * 
 * @author sinezanz & eallen
 *
 */

public class Person {
	private String code;
	private List<String> emails;
	private String firstName;
	private String lastName;
	private Address address;

	public Person(String code, String firstName, String lastName, Address address, List<String> emails) {
		this.code = code;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.emails = emails;

	}

	public List<String> getEmails() {
		return emails;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Address getAddress() {
		return address;
	}

	public String getCode() {
		return code;
	}

	public List<String> getEmail() {
		return emails;
	}

	@Override
	public String toString() {
		return "Persons [code=" + code + ", emails=" + emails + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", address=" + address + "]";
	}

	public int compareTo(Person person) {
		String a = this.getLastName() + " " + this.getFirstName();
		String b = person.getLastName() + " " + person.getFirstName();
		return a.compareTo(b);
	}
}