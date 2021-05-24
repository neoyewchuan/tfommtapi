package com.tfommtapi.models;

import com.google.gson.annotations.SerializedName;

public class GetMyProfile {
	
	@SerializedName("id")
	int id = 0;
	
	@SerializedName("username")
    String username = "";

    @SerializedName("email")
    String email = "";

    @SerializedName("first_name")
    String first_name = "";

    @SerializedName("last_name")
    String last_name = "";

    @SerializedName("country_code")
    String country_code = "";

    @SerializedName("mobile")
    String mobile = "";

    @SerializedName("role")
    String role = "";

    @SerializedName("profile_pix")
    String profile_pix = "";

    @SerializedName("dob_yrs")
    int dob_yrs = 0;

    @SerializedName("dob_mth")
    int dob_mth = 0;

    @SerializedName("dob_day")
    int dob_day = 0;
    
    @SerializedName("home_currency")
    String home_currency = "";

    public void UserProfile() {    }

    public void UserProfile(int id, String username, String email, String first_name, String last_name, String country_code, String mobile, String role, String profile_pix, int dob_yrs, int dob_mth, int dob_day, String home_currency) {
        this.id = id;
    	this.username = username;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.country_code = country_code;
        this.mobile = mobile;
        this.role = role;
        this.profile_pix = profile_pix;
        this.dob_yrs = dob_yrs;
        this.dob_mth = dob_mth;
        this.dob_day = dob_day;
        this.home_currency = home_currency;
    }
    
    

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHome_currency() {
		return home_currency;
	}

	public void setHome_currency(String home_currency) {
		this.home_currency = home_currency;
	}

	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfile_pix() {
        return profile_pix;
    }

    public void setProfile_pix(String profile_pix) {
        this.profile_pix = profile_pix;
    }

    public int getDob_yrs() {
        return dob_yrs;
    }

    public void setDob_yrs(int dob_yrs) {
        this.dob_yrs = dob_yrs;
    }

    public int getDob_mth() {
        return dob_mth;
    }

    public void setDob_mth(int dob_mth) {
        this.dob_mth = dob_mth;
    }

    public int getDob_day() {
        return dob_day;
    }

    public void setDob_day(int dob_day) {
        this.dob_day = dob_day;
    }
	
    public void setHomeCurrency(String home_currency)	{
    	this.home_currency = home_currency;
    }
    
    public String getHomeCurrency()	{
    	return this.home_currency;
    }
	
}
