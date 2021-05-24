package com.tfommtapi.models.v2;

import java.util.ArrayList;
import java.util.List;

import com.tfommtapi.models.v2.GetUserProfileModel;

public class GetUserModel {
	
	private GetUserProfileModel profile;

	private List<GetTransactionModel> listTransaction;
	
	public GetUserModel()	{
		this.profile = new GetUserProfileModel();
		this.listTransaction = new ArrayList<GetTransactionModel>();
	}

	public GetUserProfileModel getProfile() {
		return profile;
	}

	public void setProfile(GetUserProfileModel profile) {
		this.profile = profile;
	}

	public List<GetTransactionModel> getListTransaction() {
		return listTransaction;
	}

	public void setListTransaction(List<GetTransactionModel> listTransaction) {
		this.listTransaction = listTransaction;
	}
	
	
}
