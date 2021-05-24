package com.tfommtapi.models;

import com.google.gson.annotations.SerializedName;

public class AuthUser {
	
	@SerializedName("access")
	String access;
	
	@SerializedName("token")
	String token;
	
	@SerializedName("token_ttl")
	long token_ttl;
	
	@SerializedName("token_exp")
	long token_exp;
	
	public AuthUser()	{
		this.access = "";
		this.token = "";
		this.token_ttl = 0;
		this.token_exp = 0;
	}
	
	public AuthUser(String access, String token, long token_ttl, long token_exp)	{
		this.access = access;
		this.token = token;
		this.token_ttl = token_ttl;
		this.token_exp = token_exp;
	}
	
}
