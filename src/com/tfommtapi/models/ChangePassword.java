package com.tfommtapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePassword {
	
	@JsonProperty("old_password")
	public String OldPassword;
	
	@JsonProperty("new_password")
	public String NewPassword;	
	
}
