package com.tfommtapi.models.v2;

import com.google.gson.annotations.SerializedName;

public class GetUserProfileModel {
		
	@SerializedName("id")
	private int Id;
	
	@SerializedName("role")
	private String Role; // Admin or Member
	
	@SerializedName("firstname")
	private String FirstName;
	
	@SerializedName("lastname")
	private String LastName;
	
	@SerializedName("username")
	private String Username; // could use email as username
	
	@SerializedName("email")
	private String Email;
	
	@SerializedName("country_code")
	private String CountryCode; // country code, 65-SG, 60-MY, 0-US, 851-CN
	
	@SerializedName("mobile")
	private String Mobile;	// mobile number without country code
	
	@SerializedName("dob_yrs")
	private int DOB_yrs;	// date of birth, default is blank
	
	@SerializedName("dob_mth")
	private int DOB_mth;	// date of birth, default is blank
	
	@SerializedName("dob_day")
	private int DOB_day;	// date of birth, default is blank
	
	@SerializedName("home_currency")
	private String HomeCurrency; // default currency for user
	
	public GetUserProfileModel()	{}
	
	public GetUserProfileModel(int id, String role, String firstName, String lastName, String username, String email,
			String countryCode, String mobile, int dOB_yrs, int dOB_mth, int dOB_day, String homeCurrency) {
		super();
		Id = id;
		Role = role;
		FirstName = firstName;
		LastName = lastName;
		Username = username;
		Email = email;
		CountryCode = countryCode;
		Mobile = mobile;
		DOB_yrs = dOB_yrs;
		DOB_mth = dOB_mth;
		DOB_day = dOB_day;
		HomeCurrency = homeCurrency;
	}

	public void setId(int id) {
		Id = id;
	}

	public void setRole(String role) {
		Role = role;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public void setCountryCode(String countryCode) {
		CountryCode = countryCode;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public void setDOB_yrs(int dOB_yrs) {
		DOB_yrs = dOB_yrs;
	}

	public void setDOB_mth(int dOB_mth) {
		DOB_mth = dOB_mth;
	}

	public void setDOB_day(int dOB_day) {
		DOB_day = dOB_day;
	}

	public void setHomeCurrency(String homeCurrency) {
		HomeCurrency = homeCurrency;
	}

	public int getId() {
		return Id;
	}

	public String getRole() {
		return Role;
	}

	public String getFirstName() {
		return FirstName;
	}

	public String getLastName() {
		return LastName;
	}

	public String getUsername() {
		return Username;
	}

	public String getEmail() {
		return Email;
	}

	public String getCountryCode() {
		return CountryCode;
	}

	public String getMobile() {
		return Mobile;
	}

	public int getDOB_yrs() {
		return DOB_yrs;
	}

	public int getDOB_mth() {
		return DOB_mth;
	}

	public int getDOB_day() {
		return DOB_day;
	}

	public String getHomeCurrency() {
		return HomeCurrency;
	}
	
	
	
	

}
