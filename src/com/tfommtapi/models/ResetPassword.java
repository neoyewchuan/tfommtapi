package com.tfommtapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResetPassword {

	@JsonProperty("email")
    String email = "";

	@JsonProperty("dob_yrs")
    int dob_yrs = 0;

	@JsonProperty("dob_mth")
    int dob_mth = 0;

	@JsonProperty("dob_day")
    int dob_day = 0;

    public ResetPassword()  {}

    public ResetPassword(String email, int dob_yrs, int dob_mth, int dob_day) {
        this.email = email;
        this.dob_yrs = dob_yrs;
        this.dob_mth = dob_mth;
        this.dob_day = dob_day;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}

