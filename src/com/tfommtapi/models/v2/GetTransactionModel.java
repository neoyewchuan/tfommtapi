package com.tfommtapi.models.v2;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;

public class GetTransactionModel {
	
	
	@SerializedName("userid")
	private int UserId;
	
	@SerializedName("fromto")
	private int FromTo;
	
	@SerializedName("amount")
	private Double Amount;	// mobile number without country code
	
	@SerializedName("currency")
	private String Currency;
	
	@SerializedName("bank_account")
	private String BankAccount;
	
	@SerializedName("fromto_factor")
	private Double FromTo_Factor;
	
	@SerializedName("fromto_currency")
	private String FromTo_Currency;
	
	@SerializedName("transaction_type")
	private int TransactionType;	 // enum TransactionType.SEND // TOPUP // RECEIVE
	
	@SerializedName("transaction_datetime")
	private Timestamp TransactionDateTime; // country code, 65-SG, 60-MY, 0-US, 851-CN
	
	@SerializedName("remarks")
	private String Remarks;

	public GetTransactionModel()	{}
	
	
	public GetTransactionModel(int userId, int fromTo,  Double amount,
			String currency, String bankAccount, Double fromTo_Factor, String fromTo_Currency, int transactionType,
			Timestamp transactionDateTime, String remarks) {
		super();
		UserId = userId;
		FromTo = fromTo;
		Amount = amount;
		Currency = currency;
		BankAccount = bankAccount;
		FromTo_Factor = fromTo_Factor;
		FromTo_Currency = fromTo_Currency;
		TransactionType = transactionType;
		TransactionDateTime = transactionDateTime;
		Remarks = remarks;
	}


	public int getUserId() {
		return UserId;
	}


	public int getFromTo() {
		return FromTo;
	}


	public Double getAmount() {
		return Amount;
	}


	public String getCurrency() {
		return Currency;
	}


	public String getBankAccount() {
		return BankAccount;
	}


	public Double getFromTo_Factor() {
		return FromTo_Factor;
	}


	public String getFromTo_Currency() {
		return FromTo_Currency;
	}


	public int getTransactionType() {
		return TransactionType;
	}


	public Timestamp getTransactionDateTime() {
		return TransactionDateTime;
	}


	public String getRemarks() {
		return Remarks;
	}


	public void setUserId(int userId) {
		UserId = userId;
	}


	public void setFromTo(int fromTo) {
		FromTo = fromTo;
	}



	public void setAmount(Double amount) {
		Amount = amount;
	}


	public void setCurrency(String currency) {
		Currency = currency;
	}


	public void setBankAccount(String bankAccount) {
		BankAccount = bankAccount;
	}


	public void setFromTo_Factor(Double fromTo_Factor) {
		FromTo_Factor = fromTo_Factor;
	}


	public void setFromTo_Currency(String fromTo_Currency) {
		FromTo_Currency = fromTo_Currency;
	}


	public void setTransactionType(int transactionType) {
		TransactionType = transactionType;
	}


	public void setTransactionDateTime(Timestamp transactionDateTime) {
		TransactionDateTime = transactionDateTime;
	}


	public void setRemarks(String remarks) {
		Remarks = remarks;
	}
	
	
	
	
	
}
