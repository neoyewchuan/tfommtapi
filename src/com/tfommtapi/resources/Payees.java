package com.tfommtapi.resources;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.tfommtapi.manager.AccountsManager;
import com.tfommtapi.manager.PayeeManager;
import com.tfommtapi.models.AddPayee;
import com.tfommtapi.models.GetPayee;
import com.tfommtapi.models.RemovePayee;
import com.tfommtapi.models.User;


@Path("payees")
public class Payees {
	
	AccountsManager acctmanager = new AccountsManager();
	PayeeManager paymanager = new PayeeManager();
	
	
	/* 
	 *  Retrieve the payee (contacts) associate to the current login user
	 *  User to payee 
	 */
	@RolesAllowed("Member")
	@GET
	@Path("{type}")
	@Produces("application/json")
	public Response GetPayees(@PathParam("type") int type, 
			@Context HttpHeaders httpHeaders) throws SQLException {
		// get current jwt and extract payload - role user id
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		User getUser = this.acctmanager.GetUserById(userId);
		
		// ensure it is a valid user
		if (getUser != null) {
			ArrayList<GetPayee> payeeList = this.paymanager.GetListPayee(getUser.Id, type);	
			if (payeeList  != null && payeeList.size() > 0) {
				Gson gson = new Gson();
				
				JsonElement element = gson.toJsonTree(payeeList, new TypeToken<ArrayList<GetPayee>>() {}.getType());

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
	
	
	@RolesAllowed("Member")
	@GET
	@Path("search")
	@Produces("application/json")
	public Response SearchPayee(@QueryParam("payee") String searched_string,
			@Context HttpHeaders httpHeaders) throws SQLException	{
		
		// get current jwt and extract payload - role user id
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		User getUser = this.acctmanager.GetUserById(userId);
		
		// ensure the userid extracted from jwttoken and the id initiated the add payee record is the same
		if (getUser != null) {
			
		
			GetPayee  basicPayee= this.paymanager.GetBasicPayee(searched_string);
			
			// ensure the userid extracted from jwttoken and the id initiated the add payee record is the same
		
			if (basicPayee  != null ) {
				Gson gson = new Gson();
				String json = gson.toJson(basicPayee); 
				return Response.ok(json).build();
			}
		} 
				
		return Response.ok(null).build();
	}
	
	
	/* 
	 *  Add payee (contacts) to the users
	 *  User > Payee has one to many relationship, the payee can be another user on the same app or another bank account
	 */
	@RolesAllowed("Member")
	@POST
	@Consumes("application/json")
	public Response AddPayees(AddPayee addpayee, @Context HttpHeaders httpHeaders) throws SQLException {
	
		String result = "UNAUTHORIZED";
		// get current jwt and extract payload - role user id
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		User getUser = this.acctmanager.GetUserById(userId);
		
		// ensure the user is valid
		if (getUser != null && getUser.Id == addpayee.getUserId()) {
			try {
					int successcreate = this.paymanager.AddPayeeToUser(addpayee);	
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
							result = "INVALID PAYEE ID";
							break;
						case -4:
							result = "INVALID PAYEE INFO";
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
	
	
	/* 
	 *  Remove the payee (contacts) from the users
	 *  Payee can only be removed if there is not already any transaction between the user and payee.
	 */
	@RolesAllowed("Member")
	@DELETE
	@Consumes("application/json")
	public Response RemovePayees(RemovePayee removePayee, @Context HttpHeaders httpHeaders) throws SQLException {
	
		String result = "UNAUTHORIZED";
		// get current jwt and extract payload - role user id
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		User getUser = this.acctmanager.GetUserById(userId);
		
		// ensure the user is valid
		if (getUser != null && getUser.Id == removePayee.getUserId()) {
			try {
					int successcreate = this.paymanager.SafeRemovePayee(removePayee);	
					switch (successcreate)	{
						case 0:
							result = "TRANSACTION EXIST";
							break;
						case 1:
							result = "SUCCESS";
							break;
						case -1:
							result = "ERROR";
							break;
						case -2:
							result = "DELETE DENIED";
							break;
						case -3:
							result = "INVALID PAYEE ID";
							break;
						default:
							result = "UNKNOWN ERROR";
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
