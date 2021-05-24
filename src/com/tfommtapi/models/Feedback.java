package com.tfommtapi.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feedback {
	
	@JsonProperty("feedback_uid")
	@SerializedName("feedback_uid")
    @Expose
    private int feedbackUid; // user id
	
	@JsonProperty("feedback_name")
    @SerializedName("feedback_name")
    @Expose
    private String feedbackName;
	
	@JsonProperty("feedback_email")
    @SerializedName("feedback_email")
    @Expose
    private String feedbackEmail;
	
	@JsonProperty("feedback_body")
    @SerializedName("feedback_body")
    @Expose
    private String feedbackBody;
	
	@JsonProperty("feedback_type")
    @SerializedName("feedback_type")
    @Expose
    private String feedbackType;

    public Feedback()   {}

    public Feedback(int feedbackUid, String feedbackName, String feedbackEmail, String feedbackBody, String feedbackType) {
        this.feedbackUid = feedbackUid;
    	this.feedbackName = feedbackName;
        this.feedbackEmail = feedbackEmail;
        this.feedbackBody = feedbackBody;
        this.feedbackType = feedbackType;
    }
    
    public int getFeedbackUid() {
        return feedbackUid;
    }

    public void setFeedbackId(int feedbackUid) {
        this.feedbackUid = feedbackUid;
    }

    public String getFeedbackName() {
        return feedbackName;
    }

    public void setFeedbackName(String feedbackName) {
        this.feedbackName = feedbackName;
    }

    public String getFeedbackEmail() {
        return feedbackEmail;
    }

    public void setFeedbackEmail(String feedbackEmail) {
        this.feedbackEmail = feedbackEmail;
    }

    public String getFeedbackBody() {
        return feedbackBody;
    }

    public void setFeedbackBody(String feedbackBody) {
        this.feedbackBody = feedbackBody;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }
}

