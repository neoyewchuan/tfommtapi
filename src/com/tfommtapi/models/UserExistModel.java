package com.tfommtapi.models;

import com.google.gson.annotations.SerializedName;

public class UserExistModel {
	
	@SerializedName("username")
	public String username = "unknown";
	
	@SerializedName("email")
	public String email = "unknown";
	
	public UserExistModel()	{	}
}
