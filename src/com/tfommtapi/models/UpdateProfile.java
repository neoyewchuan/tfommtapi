package com.tfommtapi.models;


import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateProfile {

		@JsonProperty("first_name")
		public String FirstName;
		
		@JsonProperty("last_name")
		public String LastName;
		
		@JsonProperty("profile_pix")
		public String ProfilePix;
}