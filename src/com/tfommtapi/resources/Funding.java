package com.tfommtapi.resources;


import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.tfommtapi.manager.AccountsManager;
import com.tfommtapi.manager.FundingManager;
import com.tfommtapi.models.FundingAcct;
import com.tfommtapi.models.User;


@Path("funding")
public class Funding {
	
	AccountsManager acctmanager = new AccountsManager();
	FundingManager fundmanager = new FundingManager();
	
	/*
	 *  Retrieve the funding account associate with the current login user
	 *  All app user will have one EzyWallet account by default, it was created upon user registration
	 *  JWToken is necessary to access for authentication/identification 
	 */
	@RolesAllowed("Member")
	@GET
	@Produces("application/json")
	public Response GetFundingAcct(@Context HttpHeaders httpHeaders) throws SQLException {
		// get current jwt and extract payload - role user id
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		User getUser = this.acctmanager.GetUserById(userId);
		if (getUser != null) {
			ArrayList<FundingAcct> acctList = this.fundmanager.GetMyFundingAcct(getUser.Id);	
			if (acctList  != null && acctList.size() > 0) {
				Gson gson = new Gson();
				
				JsonElement element = gson.toJsonTree(acctList, new TypeToken<ArrayList<FundingAcct>>() {}.getType());

				if (! element.isJsonArray()) {
					return Response.ok(null).build();
				} else {

					JsonArray jsonArray = element.getAsJsonArray();
					
					String json = gson.toJson(jsonArray); 
					return Response.ok(json).build();
				}
			} 
			
		}	
		return Response.ok(null).build();
	}
	
	
	/*
	 *  Retrieve the funding account of current login user by the account type 
	 *  0 = all , 1 = wallet, 2 = local bank account, 3 = foreign bank account, 5 = local+foreign bank account
	 */
	@RolesAllowed("Member")
	@GET		
	@Path("{type}")
	@Produces("application/json")
	public Response GetFundingAcctByType(@PathParam("type") int type, @Context HttpHeaders httpHeaders) throws SQLException {			
		// get current jwt and extract payload - role user id
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		User getUser = this.acctmanager.GetUserById(userId);
		// User must be valid
		if (getUser != null) {
			ArrayList<FundingAcct> acctList;
			// type ?
			// 0 = all , 1 = wallet, 2 = local bank account, 3 = foreign bank account, 5 = local+foreign bank account
			if (type==0)	{
				acctList = this.fundmanager.GetMyFundingAcct(getUser.Id);	
			} else {
				acctList = this.fundmanager.GetMyFundingAcctByType(getUser.Id, type);	
			}
			if (acctList  != null && acctList.size() > 0) {
				Gson gson = new Gson();
					
				JsonElement element = gson.toJsonTree(acctList, new TypeToken<ArrayList<FundingAcct>>() {}.getType());

				if (! element.isJsonArray()) {
					return Response.ok(null).build();
				} else {

					JsonArray jsonArray = element.getAsJsonArray();
						
					String json = gson.toJson(jsonArray); 
					return Response.ok(json).build();
				}
			} 
				
		}	
		return Response.ok(null).build();
	}
		
		
	
	/* 
	 *  Add payee to the current login user 
	 *  User > FundingAccount having one to many relationship, beside the default EzyWallet accout, user can have many other funding account added 
	 */
	@RolesAllowed("Member")
	@POST
	@Consumes("application/json")
	public Response AddFundingAccount(FundingAcct funding, @Context HttpHeaders httpHeaders) throws SQLException {
	
		String result = "UNAUTHORIZED";
		// get current jwt and extract payload - role user id
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		User getUser = this.acctmanager.GetUserById(userId);
		
		// ensure the userid extracted from jwttoken and the id initiated the add payee record is the same
		if (getUser != null && getUser.Id == funding.Id) {
			try {
					int successcreate = this.fundmanager.AddFundingAcct(funding);	
					switch (successcreate)	{
						case 0:
							result = "RECORD EXIST";
							break;
						case 1:
							result = "SUCCESS";
							break;
						case -1:
							result = "ERROR";
							break;
						case -2:
							result = "ADD DENIED";
							break;
						case -3:
							result = "INVALID USER ID";
							break;
						case -4:
							result = "INVALID USER ID";
							break;
						default:
							result = "UNKNOWN";
							break;
								
					}
			} catch (Exception e)	{
				e.printStackTrace();
			}
			return Response.ok(result).build();
		}
		
		return Response.ok(result).build();
	}
	
}
