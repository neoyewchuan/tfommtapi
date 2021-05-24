package com.tfommtapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddPayee {
	
	@JsonProperty("userid")
	private int UserId;
	
	@JsonProperty("payee_id")
	private int PayeeId;
	
	@JsonProperty("payee")
	private String Payee;
	
	@JsonProperty("alias_name")
	private String PayeeAlias;
	
	@JsonProperty("bank_name")
	private String BankName = "";

	@JsonProperty("bank_acnt")
	private String BankAcnt = "";
	
	@JsonProperty("swift_codes")
	private String SwiftCodes;
	
	@JsonProperty("home_currency")
	private String HomeCurrency="";

	@JsonProperty("type")
	private int PayeeType = 1;  // 1-user of EzyRemit, 0-non EzyRemit users
	
	// default constructor
	public AddPayee()	{	}
	
	public AddPayee(int userId, int payeeId, String payee, String payeeAlias, String bankName, String bankAcnt, 
			String swiftCodes, String homeCurrency, 	int payeeType) {
		super();
		UserId = userId;
		PayeeId = payeeId;
		Payee = payee;
		PayeeAlias = payeeAlias;
		BankName = bankName;
		BankAcnt = bankAcnt;
		SwiftCodes = swiftCodes;
		HomeCurrency = homeCurrency;
		PayeeType = payeeType;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}
	
	public void setPayeeId(int payeeId) {
		PayeeId = payeeId;
	}

	public void setPayee(String payee) {
		Payee = payee;
	}

	public void setPayeeAlias(String payeeAlias) {
		PayeeAlias = payeeAlias;
	}

	public void setBankName(String bankName) {
		BankName = bankName;
	}

	public void setBankAcnt(String bankAcnt) {
		BankAcnt = bankAcnt;
	}
	
	public void setSwiftCodes(String swiftCodes) {
	SwiftCodes = swiftCodes;
	}

	public void setHomeCurrency(String homeCurrency) {
		HomeCurrency = homeCurrency;
	}

	public void setPayeeType(int payeeType) {
		PayeeType = payeeType;
	}

	public int getUserId() {
		return UserId;
	}
	
	public int getPayeeId() {
		return PayeeId;
	}
	
	public String getPayee() {
		return Payee;
	}

	public String getPayeeAlias() {
		return PayeeAlias;
	}

	public String getBankName() {
		return BankName;
	}

	public String getBankAcnt() {
		return BankAcnt;
	}
	
	public String getSwiftCodes()	{
		return SwiftCodes;
	}

	public String getHomeCurrency() {
		return HomeCurrency;
	}

	public int getPayeeType() {
		return PayeeType;
	}
	
	
	
}
