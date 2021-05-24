package com.tfommtapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RemovePayee {
	
	@JsonProperty("userid")
	private int UserId;
	
	@JsonProperty("payee_id")
	private int PayeeId;
	
	@JsonProperty("payee")
	private String Payee;
	
	@JsonProperty("app_user")
	private int AppUser;
	
	public RemovePayee()	{}
	
	public RemovePayee(int userId, int payeeId, String payee) {
		super();
		UserId = userId;
		PayeeId = payeeId;
		Payee = payee;
	}
	
	

	public RemovePayee(int userId, int payeeId, String payee, int appUser) {
		super();
		UserId = userId;
		PayeeId = payeeId;
		Payee = payee;
		AppUser = appUser;
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

	public void setUserId(int userId) {
		UserId = userId;
	}

	public void setPayeeId(int payeeId) {
		PayeeId = payeeId;
	}

	public void setPayee(String payee) {
		Payee = payee;
	}

	public int getAppUser() {
		return AppUser;
	}

	public void setAppUser(int appUser) {
		AppUser = appUser;
	}
	
	
	
	
}
