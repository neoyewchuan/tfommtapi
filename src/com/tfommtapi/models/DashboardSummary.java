package com.tfommtapi.models;

import com.google.gson.annotations.SerializedName;

public class DashboardSummary {
	
	@SerializedName("total_topup")
	private Double TotalTopupAmount;
	
	@SerializedName("total_sent")
	private Double TotalSentAmount;
	
	@SerializedName("total_received")
	private Double TotalReceivedAmount;
	
	@SerializedName("acct_balance")
	private Double AcctBalance;
	
	@SerializedName("acct_numbr")
	private String AcctNumbr;
	
	@SerializedName("acct_currency")
	private String AcctCurrency;
	
	@SerializedName("total_request")
	private int TotalPaymentRequest;
	
	public DashboardSummary()	{}
	
	public DashboardSummary(Double totalTopupAmount, Double totalSentAmount, Double totalReceivedAmount,
			Double acctBalance, String acctNumbr, String acctCurrency, int totalPaymentRequest) {
		super();
		TotalTopupAmount = totalTopupAmount;
		TotalSentAmount = totalSentAmount;
		TotalReceivedAmount = totalReceivedAmount;
		AcctBalance = acctBalance;
		AcctNumbr = acctNumbr;
		AcctCurrency = acctCurrency;
		TotalPaymentRequest = totalPaymentRequest;
	}

	public Double getTotalTopupAmount() {
		return TotalTopupAmount;
	}

	public Double getTotalSentAmount() {
		return TotalSentAmount;
	}

	public Double getTotalReceivedAmount() {
		return TotalReceivedAmount;
	}

	public Double getAcctBalance() {
		return AcctBalance;
	}

	public String getAcctNumbr() {
		return AcctNumbr;
	}

	public String getAcctCurrency() {
		return AcctCurrency;
	}

	public void setTotalTopupAmount(Double totalTopupAmount) {
		TotalTopupAmount = totalTopupAmount;
	}

	public void setTotalSentAmount(Double totalSentAmount) {
		TotalSentAmount = totalSentAmount;
	}

	public void setTotalReceivedAmount(Double totalReceivedAmount) {
		TotalReceivedAmount = totalReceivedAmount;
	}

	public void setAcctBalance(Double acctBalance) {
		AcctBalance = acctBalance;
	}

	public void setAcctNumbr(String acctNumbr) {
		AcctNumbr = acctNumbr;
	}

	public void setAcctCurrency(String acctCurrency) {
		AcctCurrency = acctCurrency;
	}

	public int getTotalPaymentRequest() {
		return TotalPaymentRequest;
	}

	public void setTotalPaymentRequest(int totalPaymentRequest) {
		TotalPaymentRequest = totalPaymentRequest;
	}
	
	
	
}
