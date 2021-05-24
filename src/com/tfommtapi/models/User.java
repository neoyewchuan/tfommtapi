package com.tfommtapi.models;

public class User {
	public int Id;
	public String Role; // Admin or Member
	public String FirstName;
	public String LastName;
	public String Username; // could use email as username
	public String Password;
	
	public String Email;
	public String CountryCode; // country code, 65-SG, 60-MY, 0-US, 851-CN
	public String Mobile;	// mobile number without country code
	public int DOB_yrs;	// date of birth, default is blank
	public int DOB_mth;	// date of birth, default is blank
	public int DOB_day;	// date of birth, default is blank
	public String HomeCurrency; // default currency for user
	public String ProfilePix;
	
	//public int LockStat;	// default 0 0
	//public int FailedLogin; // default to 0
	//public Date LockTime; // default to blank
	//public Date LastLogin; // LocalDate myObj = LocalDate.now();
	
	
	
}
