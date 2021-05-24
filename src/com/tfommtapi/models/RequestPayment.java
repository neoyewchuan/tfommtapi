package com.tfommtapi.models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestPayment {
	
	@JsonProperty("request_id")
    @SerializedName("request_id")
    @Expose
    private int requestId;

	@JsonProperty("requestor")
    @SerializedName("requestor")
    @Expose
    private int requestor;

    @JsonProperty("requestee")
    @SerializedName("requestee")
    @Expose
    private int requestee;

    @JsonProperty("requestee_name")
    @SerializedName("requestee_name")
    @Expose
    private String requesteeName;

    @JsonProperty("request_amount")
    @SerializedName("request_amount")
    @Expose
    private double requestAmount;

    @JsonProperty("request_currency")
    @SerializedName("request_currency")
    @Expose
    private String requestCurrency;

    @JsonProperty("request_reason")
    @SerializedName("request_reason")
    @Expose
    private String requestReason;

    @JsonProperty("request_datetime")
    @SerializedName("request_datetime")
    @Expose
    private Timestamp requestDateTime;
    
    @JsonProperty("request_username")
    @SerializedName("request_username")
    @Expose
    private String requestUsername;
    
    @JsonProperty("request_profilepix")
    @SerializedName("request_profilepix")
    @Expose
    private String requestProfilePix = "";    
    
    @JsonProperty("role")
    @SerializedName("role")
    @Expose
    private String userRole;

    public RequestPayment() {}

    public RequestPayment(int requestId, int requestor, String requestorUname, int requestee, String requesteeName,
                          double requestAmount, String requestCurrency,
                          String requestReason, Timestamp requestDateTime, String userRole) {
    	this.requestId = requestId;
        this.requestor = requestor;
        this.requestUsername = requestorUname;
        this.requestee = requestee;
        this.requesteeName = requesteeName;
        this.requestAmount = requestAmount;
        this.requestCurrency = requestCurrency;
        this.requestReason = requestReason;
        this.requestDateTime = requestDateTime;
        this.userRole = userRole;
        this.requestProfilePix = "";
    }
    
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
    
    public int getRequestor() {
        return requestor;
    }

    public void setRequestor(int requestor) {
        this.requestor = requestor;
    }
    

    public int getRequestee() {
        return requestee;
    }

    public void setRequestee(int requestee) {
        this.requestee = requestee;
    }

    public String getRequesteeName() {
        return requesteeName;
    }

    public void setRequesteeName(String requesteeName) {
        this.requesteeName = requesteeName;
    }

    public double getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(double requestAmount) {
        this.requestAmount = requestAmount;
    }

    public String getRequestCurrency() {
        return requestCurrency;
    }

    public void setRequestCurrency(String requestCurrency) {
        this.requestCurrency = requestCurrency;
    }

    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public Timestamp getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(Timestamp requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getRequestUsername() {
		return requestUsername;
	}

	public void setRequestUsername(String requestUsername) {
		this.requestUsername = requestUsername;
	}

	public String getRequestProfilePix() {
		return requestProfilePix;
	}

	public void setRequestProfilePix(String requestProfilePix) {
		this.requestProfilePix = requestProfilePix;
	}
    
    
}
