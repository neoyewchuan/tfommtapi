package com.tfommtapi.models;

public class BoolResponseModel {
	String value;
	Boolean response;
	
	public BoolResponseModel(String value, Boolean response)	{
		this.response = response;
		this.value = value;
	}
}
