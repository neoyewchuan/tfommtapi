package com.tfommtapi.models;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@XmlRootElement(name = "CreateUser")
public class CreateAccount {
	
	@JsonProperty("first_name")
	public String FirstName;
	
	@JsonProperty("last_name")
	public String LastName;
	
	@JsonProperty("email")
	public String Email;
	
	@JsonProperty("username")
	public String Username;
	
	@JsonProperty("password")
	public String Password;
	
	@JsonProperty("mobile")
	public String Mobile;
	
	@JsonProperty("country_code")
	public String CountryCode;
	
	@JsonProperty("role")
	public String Role;
	
	@JsonProperty("dob_yrs")
	public int DOB_yrs;
	
	@JsonProperty("dob_mth")
	public int DOB_mth;
	
	@JsonProperty("dob_day")
	public int DOB_day;
	
	@JsonProperty("create_time")
	public LocalDate CreateTime;
	
	@JsonProperty("profile_pix")
	public String ProfilePix;
	
	@JsonProperty("home_currency")
	public String HomeCurrency;
	
	
	// default constructor
	public CreateAccount()	{	}
	
	
	
	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getCountryCode() {
		return CountryCode;
	}

	public void setCountryCode(String countryCode) {
		CountryCode = countryCode;
	}

	public String getRole() {
		return Role;
	}

	public void setRole(String role) {
		Role = role;
	}

	public int getDOB_yrs() {
		return DOB_yrs;
	}

	public void setDOB_yrs(int dOB_yrs) {
		DOB_yrs = dOB_yrs;
	}

	public int getDOB_mth() {
		return DOB_mth;
	}

	public void setDOB_mth(int dOB_mth) {
		DOB_mth = dOB_mth;
	}

	public int getDOB_day() {
		return DOB_day;
	}

	public void setDOB_day(int dOB_day) {
		DOB_day = dOB_day;
	}

	public LocalDate getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(LocalDate createTime) {
		CreateTime = createTime;
	}

	public String getProfilePix() {
		return ProfilePix;
	}

	public void setProfilePix(String profilePix) {
		ProfilePix = profilePix;
	}

	public String getHomeCurrency() {
		return HomeCurrency;
	}

	public void setHomeCurrency(String homeCurrency) {
		HomeCurrency = homeCurrency;
	}
	
	/*
	@JsonProperty("lock_stat")
	public int LockStat;
	
	@JsonProperty("failed_login")
	public int FailedLogin; // default to 0
	
	@JsonProperty("locked_time")
	public Date LockTime; // default to blank
	
	@JsonProperty("last_login")
	public Date LastLogin; // LocalDate myObj = LocalDate.now();
	*/
	
	
	
}
