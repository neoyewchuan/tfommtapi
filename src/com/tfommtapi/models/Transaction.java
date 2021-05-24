package com.tfommtapi.models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transaction {
	
	@JsonProperty("id")
	@SerializedName("id")
	@Expose
	private int Id;
	
	@JsonProperty("userid")
	@SerializedName("userid")
	@Expose
	private int UserId;
	
	@JsonProperty("fromto")
	@SerializedName("fromto")
	@Expose
	private int FromTo;
	
	@JsonProperty("username")
	@SerializedName("username")
	@Expose
	private String UserName;
	
	@JsonProperty("fromto_name")
	@SerializedName("fromto_name")
	@Expose
	private String FromToName;
	
	@JsonProperty("amount")
	@SerializedName("amount")
	@Expose
	private Double Amount;	
	
	@JsonProperty("currency")
	@SerializedName("currency")
	@Expose
	private String Currency;
	
	@JsonProperty("bankaccount")
	@SerializedName("bankaccount")
	@Expose
	private String BankAccount;
	
	@JsonProperty("fromto_factor")
	@SerializedName("fromto_factor")
	@Expose
	private Double FromTo_Factor;
	
	@JsonProperty("fromto_currency")
	@SerializedName("fromto_currency")
	@Expose
	private String FromTo_Currency;
	
	@JsonProperty("transaction_type")
	@SerializedName("transaction_type")
	@Expose
	private int TransactionType;	 // TransactionType.SEND // TOPUP // RECEIVE
	
	@JsonProperty("transaction_datetime")
	@SerializedName("transaction_datetime")
	@Expose
	private Timestamp TransactionDateTime; 
	
	@JsonProperty("remarks")
	@SerializedName("remarks")
	@Expose
	private String Remarks;
	
	@JsonProperty("transaction_ref")
	@SerializedName("transaction_ref")
	@Expose
	private String TransactionRef;	 // TransactionType.SEND // TOPUP // RECEIVE
	
	@JsonProperty("amt_home_rate_snd")
    @SerializedName("amt_home_rate_snd")
    @Expose
    private double amt_Home_Rate_Sender;

    @JsonProperty("amt_home_rate_rcp")
    @SerializedName("amt_home_rate_rcp")
    @Expose
    private double amt_Home_Rate_Recipient;
	
	
	public Transaction()	{ }
	
	public Transaction(int userId, int fromTo, String userName, String fromToName, Double amount, String currency,
			String bankAccount, Double fromTo_Factor, String fromTo_Currency, int transactionType,
			Timestamp transactionDateTime, String remarks) {
		super();
		UserId = userId;
		FromTo = fromTo;
		UserName = userName;
		FromToName = fromToName;
		Amount = amount;
		Currency = currency;
		BankAccount = bankAccount;
		FromTo_Factor = fromTo_Factor;
		FromTo_Currency = fromTo_Currency;
		TransactionType = transactionType;
		TransactionDateTime = transactionDateTime;
		Remarks = remarks;
	}
	
	

	public Transaction(int id, int userId, int fromTo, String userName, String fromToName, Double amount,
			String currency, String bankAccount, Double fromTo_Factor, String fromTo_Currency, int transactionType,
			Timestamp transactionDateTime, String remarks, String transactionRef) {
		super();
		Id = id;
		UserId = userId;
		FromTo = fromTo;
		UserName = userName;
		FromToName = fromToName;
		Amount = amount;
		Currency = currency;
		BankAccount = bankAccount;
		FromTo_Factor = fromTo_Factor;
		FromTo_Currency = fromTo_Currency;
		TransactionType = transactionType;
		TransactionDateTime = transactionDateTime;
		Remarks = remarks;
		TransactionRef = transactionRef;
	}
	
	

	public Transaction(int id, int userId, int fromTo, String userName, String fromToName, Double amount,
			String currency, String bankAccount, Double fromTo_Factor, String fromTo_Currency, int transactionType,
			Timestamp transactionDateTime, String remarks, String transactionRef, double amt_Home_Rate_Sender,
			double amt_Home_Rate_Recipient) {
		super();
		Id = id;
		UserId = userId;
		FromTo = fromTo;
		UserName = userName;
		FromToName = fromToName;
		Amount = amount;
		Currency = currency;
		BankAccount = bankAccount;
		FromTo_Factor = fromTo_Factor;
		FromTo_Currency = fromTo_Currency;
		TransactionType = transactionType;
		TransactionDateTime = transactionDateTime;
		Remarks = remarks;
		TransactionRef = transactionRef;
		this.amt_Home_Rate_Sender = amt_Home_Rate_Sender;
		this.amt_Home_Rate_Recipient = amt_Home_Rate_Recipient;
	}

	public void setId(int id) {
		Id = id;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public void setFromTo(int fromTo) {
		FromTo = fromTo;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public void setFromToName(String fromToName) {
		FromToName = fromToName;
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

	public int getId() {
		return Id;
	}

	public int getUserId() {
		return UserId;
	}

	public int getFromTo() {
		return FromTo;
	}

	public String getUserName() {
		return UserName;
	}

	public String getFromToName() {
		return FromToName;
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

	public String getTransactionRef() {
		return TransactionRef;
	}

	public void setTransactionRef(String transactionRef) {
		TransactionRef = transactionRef;
	}
	
	public double getAmt_Home_Rate_Sender() {
		return amt_Home_Rate_Sender;
	}



	public void setAmt_Home_Rate_Sender(double amt_Home_Rate_Sender) {
		this.amt_Home_Rate_Sender = amt_Home_Rate_Sender;
	}



	public double getAmt_Home_Rate_Recipient() {
		return amt_Home_Rate_Recipient;
	}



	public void setAmt_Home_Rate_Recipient(double amt_Home_Rate_Recipient) {
		this.amt_Home_Rate_Recipient = amt_Home_Rate_Recipient;
	}

	
	
	
	
	
	
}


