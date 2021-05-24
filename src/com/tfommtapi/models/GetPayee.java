package com.tfommtapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPayee {
	
	@JsonProperty("id")
	@SerializedName("id")
	@Expose
	private int Id;
	
	@JsonProperty("username")
	@SerializedName("username")
	@Expose
	private String Username;
	
	@JsonProperty("email")
	@SerializedName("email")
	@Expose
	private String Email;
	
	@JsonProperty("alias")
	@SerializedName("alias")
	@Expose
	private String AliasName;
	
	@JsonProperty("bank_name")
	@SerializedName("bank_name")
	@Expose
	private String BankName;
	
	@JsonProperty("bank_acnt")
	@SerializedName("bank_acnt")
	@Expose
	private String BankAcnt;
	
	@JsonProperty("swift_codes")
	@SerializedName("swift_codes")
	@Expose
	private String SwiftCodes;
	
	@JsonProperty("profile_pix")
	@SerializedName("profile_pix")
	@Expose
	private String ProfilePix;
	
	@JsonProperty("money_to_you")
	@SerializedName("money_to_you")
	@Expose
	private Double MoneyToYou;
	
	@JsonProperty("money_from_you")
	@SerializedName("money_from_you")
	@Expose
	private Double MoneyFromYou;
	
	@JsonProperty("home_currency")
	@SerializedName("home_currency")
	@Expose
	private String HomeCurrency;
	
	@JsonProperty("acct_country")
	@SerializedName("acct_country")
	@Expose
	private String acctCountry;
	
	@JsonProperty("app_user")
	@SerializedName("app_user")
	@Expose
	private int AppUser;
	
	public GetPayee()		{
		
	}

	public GetPayee(int id, String username, String email, String aliasName, String bankName, String bankAcnt,
			String swiftCodes, String profilePix, Double moneyToYou, Double moneyFromYou, String homeCurrency, int appUser) {
		super();
		Id = id;
		Username = username;
		Email = email;
		AliasName = aliasName;
		BankName = bankName;
		BankAcnt = bankAcnt;
		SwiftCodes = swiftCodes;
		ProfilePix = profilePix;
		MoneyToYou = moneyToYou;
		MoneyFromYou = moneyFromYou;
		HomeCurrency = homeCurrency;
		AppUser = appUser;
	}
	
	
	

	public GetPayee(int id, String username, String email, String aliasName, String bankName, String bankAcnt,
			String swiftCodes, String profilePix, Double moneyToYou, Double moneyFromYou, String homeCurrency,
			String acctCountry, int appUser) {
		super();
		Id = id;
		Username = username;
		Email = email;
		AliasName = aliasName;
		BankName = bankName;
		BankAcnt = bankAcnt;
		SwiftCodes = swiftCodes;
		ProfilePix = profilePix;
		MoneyToYou = moneyToYou;
		MoneyFromYou = moneyFromYou;
		HomeCurrency = homeCurrency;
		this.acctCountry = acctCountry;
		AppUser = appUser;
	}

	public String getHomeCurrency() {
		return HomeCurrency;
	}

	public void setHomeCurrency(String homeCurrency) {
		HomeCurrency = homeCurrency;
	}

	public int getId() {
		return Id;
	}

	public String getUsername() {
		return Username;
	}

	public String getEmail() {
		return Email;
	}

	public String getAliasName() {
		return AliasName;
	}

	public String getBankName() {
		return BankName;
	}

	public String getBankAcnt() {
		return BankAcnt;
	}
	
	public String getSwiftCodes()	{
		return SwiftCodes;
	}

	public String getProfilePix() {
		return ProfilePix;
	}

	public Double getMoneyToYou() {
		return MoneyToYou;
	}

	public Double getMoneyFromYou() {
		return MoneyFromYou;
	}

	public int getAppUser() {
		return AppUser;
	}
	
	public void setId(int id)	{
		Id = id;
	}
	
	public void setUsername(String username) {
		Username = username;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public void setAliasName(String aliasName) {
		AliasName = aliasName;
	}

	public void setBankName(String bankName) {
		BankName = bankName;
	}

	public void setBankAcnt(String bankAcnt) {
		BankAcnt = bankAcnt;
	}
	
	public void setSwiftCodes(String swiftCodes) {
		SwiftCodes = swiftCodes;
	}
	
	public void setProfilePix(String profilePix) {
		ProfilePix = profilePix;
	}

	public void setMoneyToYou(Double moneyToYou) {
		MoneyToYou = moneyToYou;
	}

	public void setMoneyFromYou(Double moneyFromYou) {
		MoneyFromYou = moneyFromYou;
	}

	public void setAppUser(int appUser) {
		AppUser = appUser;
	}

	public String getAcctCountry() {
		return acctCountry;
	}

	public void setAcctCountry(String acctCountry) {
		this.acctCountry = acctCountry;
	}
	
	
	
}
