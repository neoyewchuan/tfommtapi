package com.tfommtapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FundingAcct {
	
	@JsonProperty("id")
	@SerializedName("id")
	@Expose
	public int Id;
	
	@JsonProperty("acct_name")
	@SerializedName("acct_name")
	@Expose
	public String acctName;
	
	@JsonProperty("acct_numbr")
	@SerializedName("acct_numbr")
	@Expose
	public String acctNumber;
	
	@JsonProperty("acct_balc")
	@SerializedName("acct_balc")
	@Expose
	public Double acctBalance;
	
	@JsonProperty("acct_type")
	@SerializedName("acct_type")
	@Expose
	public int acctType;
	
	@JsonProperty("acct_currency")
	@SerializedName("acct_currency")
	@Expose
	public String acctCurrency;
	
	@JsonProperty("short_name")
	@SerializedName("short_name")
	@Expose
	public String shortName;
	
	@JsonProperty("acct_country")
	@SerializedName("acct_country")
    @Expose
    private String acctCountry;
	
	@JsonProperty("swift_codes")
	@SerializedName("swift_codes")
	@Expose
    private String swiftCodes;

	@JsonProperty("acct_alias")
	@SerializedName("acct_alias")
	@Expose
    private String acctAlias;
    
	public FundingAcct()	{	}
	
	public FundingAcct(int id, String acctName, String acctNumber, Double acctBalance, int acctType,
			String acctCurrency, String shortName, String swiftCodes, String acctAlias) {
		super();
		Id = id;
		this.acctName = acctName;
		this.acctNumber = acctNumber;
		this.acctBalance = acctBalance;
		this.acctType = acctType;
		this.acctCurrency = acctCurrency;
		this.shortName = shortName;
		this.swiftCodes = swiftCodes;
        this.acctAlias = acctAlias;
	}
	

	public FundingAcct(int id, String acctName, String acctNumber, Double acctBalance, int acctType,
			String acctCurrency, String shortName, String acctCountry, String swiftCodes, String acctAlias) {
		super();
		Id = id;
		this.acctName = acctName;
		this.acctNumber = acctNumber;
		this.acctBalance = acctBalance;
		this.acctType = acctType;
		this.acctCurrency = acctCurrency;
		this.shortName = shortName;
		this.acctCountry = acctCountry;
		this.swiftCodes = swiftCodes;
		this.acctAlias = acctAlias;
	}
	

	public int getId() {
		return Id;
	}

	public String getAcctName() {
		return acctName;
	}

	public String getAcctNumber() {
		return acctNumber;
	}

	public Double getAcctBalance() {
		return acctBalance;
	}

	public int getAcctType() {
		return acctType;
	}

	public String getAcctCurrency() {
		return acctCurrency;
	}

	public String getShortName() {
		return shortName;
	}

	public void setId(int id) {
		Id = id;
	}

	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

	public void setAcctNumber(String acctNumber) {
		this.acctNumber = acctNumber;
	}

	public void setAcctBalance(Double acctBalance) {
		this.acctBalance = acctBalance;
	}

	public void setAcctType(int acctType) {
		this.acctType = acctType;
	}

	public void setAcctCurrency(String acctCurrency) {
		this.acctCurrency = acctCurrency;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	public String getSwiftCodes() {
        return swiftCodes;
    }

    public void setSwiftCodes(String swiftCodes) {
        this.swiftCodes = swiftCodes;
    }

    public String getAcctAlias() {
        return acctAlias;
    }

    public void setAcctAlias(String acctAlias) {
        this.acctAlias = acctAlias;
    }
	
    public String getAcctCountry() {
        return acctCountry;
    }

    public void setAcctCountry(String acctCountry) {
        this.acctCountry = acctCountry;
    }

}
