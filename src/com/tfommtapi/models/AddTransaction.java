package com.tfommtapi.models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddTransaction {
	
	@JsonProperty("transaction_type")
	@SerializedName("transaction_type")
	@Expose
	private int transaction_type;
	
	@JsonProperty("userid")
	@SerializedName("userid")
	@Expose
	private int userid;
	
	@JsonProperty("fromto")
	@SerializedName("fromto")
	@Expose
	private int fromto;
	
	@JsonProperty("amount")
	@SerializedName("amount")
	@Expose
	private Double amount;	
	
	@JsonProperty("currency")
	@SerializedName("currency")
	@Expose
	private String currency;
	
	@JsonProperty("bank_account")
	@SerializedName("bank_account")
	@Expose
	private String bank_account;
	
	@JsonProperty("acct_type")
    @SerializedName("acct_type")
    @Expose
    private int acct_type;
    
    @JsonProperty("fromto_bank_account")
    @SerializedName("fromto_bank_account")
    @Expose
    private String fromto_bank_account;

    @JsonProperty("fromto_acct_type")
    @SerializedName("fromto_acct_type")
    @Expose
    private int fromto_acct_type;
	
	@JsonProperty("release_type")
	@SerializedName("release_type")
	@Expose
	private int release_type;
	
	@JsonProperty("fromto_currency")
	@SerializedName("fromto_currency")
	@Expose
	private String fromto_currency;
	
	@JsonProperty("fromto_factor")
	@SerializedName("fromto_factor")
	@Expose
	private Double fromto_factor;
	
	@JsonProperty("fromto_admin")
	@SerializedName("fromto_admin")
	@Expose
	private double fromto_admin;
	
	@JsonProperty("fromto_variable")
	@SerializedName("fromto_variable")
	@Expose
	private double fromto_variable;
	
	@JsonProperty("amt_home_rate_snd")
    @SerializedName("amt_home_rate_snd")
    @Expose
    private double amt_Home_Rate_Sender;

    @JsonProperty("amt_home_rate_rcp")
    @SerializedName("amt_home_rate_rcp")
    @Expose
    private double amt_Home_Rate_Recipient;
    
	



	@JsonProperty("remarks")
	@SerializedName("remarks")
	@Expose
	private String remarks;
	
	@JsonProperty("request_id")
	@SerializedName("request_id")
	@Expose
	private int requestId;
	
	private Timestamp release_date;
	
	private String transaction_ref;
	
	public AddTransaction()	{	}
	
	

	public AddTransaction(int transaction_type, int userid, int fromto, Double amount, String currency,
			String bank_account, int acct_type, String fromto_bank_account, int fromto_acct_type,
			int release_type, String fromto_currency, Double fromto_factor, double fromto_admin, double fromto_variable,
			String remarks) {
		super();
		this.transaction_type = transaction_type;
		this.userid = userid;
		this.fromto = fromto;
		this.amount = amount;
		this.currency = currency;
		this.bank_account = bank_account;
		this.acct_type = acct_type;
		this.fromto_bank_account = fromto_bank_account;
		this.fromto_acct_type = fromto_acct_type;
		this.release_type = release_type;
		this.fromto_currency = fromto_currency;
		this.fromto_factor = fromto_factor;
		this.fromto_admin = fromto_admin;
		this.fromto_variable = fromto_variable;
		this.remarks = remarks;
		this.requestId = 0;
	}

	

	public AddTransaction(int transaction_type, int userid, int fromto, Double amount, String currency,
			String bank_account, int acct_type, String fromto_bank_account, int fromto_acct_type, int release_type,
			String fromto_currency, Double fromto_factor, double fromto_admin, double fromto_variable, String remarks, int requestId) {
		super();
		this.transaction_type = transaction_type;
		this.userid = userid;
		this.fromto = fromto;
		this.amount = amount;
		this.currency = currency;
		this.bank_account = bank_account;
		this.acct_type = acct_type;
		this.fromto_bank_account = fromto_bank_account;
		this.fromto_acct_type = fromto_acct_type;
		this.release_type = release_type;
		this.fromto_currency = fromto_currency;
		this.fromto_factor = fromto_factor;
		this.fromto_admin = fromto_admin;
		this.fromto_variable = fromto_variable;
		this.remarks = remarks;
		this.requestId = requestId;
	}

	public AddTransaction(int transaction_type, int userid, int fromto, Double amount, String currency,
			String bank_account, int acct_type, String fromto_bank_account, int fromto_acct_type, int release_type,
			String fromto_currency, Double fromto_factor, double fromto_admin, double fromto_variable,
			double amt_Home_Rate_Sender, double amt_Home_Rate_Recipient, String remarks, int requestId,
			Timestamp release_date, String transaction_ref) {
		super();
		this.transaction_type = transaction_type;
		this.userid = userid;
		this.fromto = fromto;
		this.amount = amount;
		this.currency = currency;
		this.bank_account = bank_account;
		this.acct_type = acct_type;
		this.fromto_bank_account = fromto_bank_account;
		this.fromto_acct_type = fromto_acct_type;
		this.release_type = release_type;
		this.fromto_currency = fromto_currency;
		this.fromto_factor = fromto_factor;
		this.fromto_admin = fromto_admin;
		this.fromto_variable = fromto_variable;
		this.amt_Home_Rate_Sender = amt_Home_Rate_Sender;
		this.amt_Home_Rate_Recipient = amt_Home_Rate_Recipient;
		this.remarks = remarks;
		this.requestId = requestId;
		this.release_date = release_date;
		this.transaction_ref = transaction_ref;
	}


	public int getTransactionType() {
		return transaction_type;
	}

	public int getUserId() {
		return userid;
	}

	public int getFromTo() {
		return fromto;
	}

	public Double getAmount() {
		return amount;
	}

	public String getCurrency() {
		return currency;
	}

	public String getBankAccount() {
		return bank_account;
	}

	public int getReleaseType() {
		return release_type;
	}

	public String getFromTo_Currency() {
		return fromto_currency;
	}

	public Double getFromTo_Factor() {
		return fromto_factor;
	}

	public double getFromTo_Admin_Fee() {
		return fromto_admin;
	}

	public double getFromTo_Variable_Fee() {
		return fromto_variable;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setTransactionType(int transactionType) {
		this.transaction_type = transactionType;
	}

	public void setUserId(int userId) {
		this.userid = userId;
	}

	public void setFromTo(int fromTo) {
		this.fromto = fromTo;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setBankAccount(String bankAccount) {
		this.bank_account = bankAccount;
	}

	public void setReleaseType(int releaseType) {
		this.release_type = releaseType;
	}

	public void setFromTo_Currency(String fromTo_Currency) {
		this.fromto_currency = fromTo_Currency;
	}

	public void setFromTo_Factor(Double fromTo_Factor) {
		this.fromto_factor = fromTo_Factor;
	}

	public void setFromTo_Admin_Fee(double fromTo_Admin_Fee) {
		this.fromto_admin = fromTo_Admin_Fee;
	}

	public void setFromTo_Variable_Fee(double fromTo_Variable_Fee) {
		this.fromto_variable = fromTo_Variable_Fee;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getAcct_type() {
		return acct_type;
	}

	public void setAcct_type(int acct_type) {
		this.acct_type = acct_type;
	}

	public String getFromto_bank_account() {
		return fromto_bank_account;
	}

	public void setFromto_bank_account(String fromto_bank_account) {
		this.fromto_bank_account = fromto_bank_account;
	}

	public int getFromto_acct_type() {
		return fromto_acct_type;
	}

	public void setFromto_acct_type(int fromto_acct_type) {
		this.fromto_acct_type = fromto_acct_type;
	}
	
	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public Timestamp getRelease_date() {
		return release_date;
	}

	public void setRelease_date(Timestamp release_date) {
		this.release_date = release_date;
	}



	public String getTransactionRef() {
		return transaction_ref;
	}



	public void setTransactionRef(String transactionRef) {
		this.transaction_ref = transactionRef;
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



	@Override
	public String toString() {
		return "AddTransaction [transactionType=" + transaction_type + ", userId=" + userid + ", fromTo=" + fromto
				+ ", amount=" + amount + ", currency=" + currency + ", bankAccount=" + bank_account + ", releaseType="
				+ release_type + ", fromTo_Currency=" + fromto_currency + ", fromTo_Factor=" + fromto_factor
				+ ", fromTo_Admin_Fee=" + fromto_admin + ", fromTo_Variable_Fee=" + fromto_variable
				+ ", remarks=" + remarks + "]";
	}
	
	
	
}
