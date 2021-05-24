package com.tfommtapi.resources;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
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
import com.tfommtapi.manager.TransactionManager;
import com.tfommtapi.models.AddTransaction;
import com.tfommtapi.models.DashboardSummary;
import com.tfommtapi.models.RequestPayment;
import com.tfommtapi.models.Transaction;
import com.tfommtapi.models.TransactionsPagedResult;
import com.tfommtapi.models.User;

import io.jsonwebtoken.ExpiredJwtException;

@Path("transactions")
public class Transactions {
	
	TransactionManager manager = new TransactionManager();
	AccountsManager acctmanager = new AccountsManager();
	
	/* 
	 *  Retrieve the transactions associate to the current login user.
	 *  acceptable parameter {type}: 101=Top-Up Transactions, 102=Send Money Transactions, 103=Receive Money Transaction, or any other value=All Transactions  
	 */
	@RolesAllowed("Member")
	@GET
	@Path("{type}")
	@Produces("application/json")
	public Response getUserTransactions(@PathParam("type") int typeId, @Context HttpHeaders httpHeaders) throws SQLException {
		// get current jwt and extract payload - role user id
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		ArrayList<Transaction> getTransList = this.manager.GetTransactionsById(userId, typeId);
		
		if (getTransList != null && getTransList.size() > 0) {
			Gson gson = new Gson();
			
			JsonElement element = gson.toJsonTree(getTransList, new TypeToken<ArrayList<Transaction>>() {}.getType());

			if (! element.isJsonArray()) {
			// fail appropriately
			    // throw new JsonException();
				return Response.ok("").build();
			} else {

				JsonArray jsonArray = element.getAsJsonArray();
				
				String json = gson.toJson(jsonArray); 
				return Response.ok(json).build();
			}
		}
		
		return Response.ok("").build();
	}
	
	
	/* 
	 *  Retrieve the transactions associate to the current login user with pagination
	 *  acceptable parameter {type}: 101=Top-Up Transactions, 102=Send Money Transactions, 103=Receive Money Transaction, or any other value=All Transactions  
	 */
	@RolesAllowed("Member")
	@GET
	@Produces("application/json")
	public Response getUserTransactionsWithPagination(@QueryParam("type") int typeId,
			@QueryParam("pageSize") @DefaultValue("20") int pageSize,
			@QueryParam("pageIndex") @DefaultValue("0") int pageIndex, @Context HttpHeaders httpHeaders) throws SQLException {
	
		
		// get current jwt and extract payload - role user id
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		TransactionsPagedResult pagedResult = new TransactionsPagedResult();
		
		ArrayList<Transaction> getTransList = this.manager.GetTransactionsById(userId, typeId, pageSize, pageIndex);
		
		if (getTransList != null && getTransList.size() > 0) {
			
			int totalTransactionsCount = this.manager.GetTransactionsCountById(userId, typeId);
			pagedResult.setList(getTransList);
			pagedResult.setNext((((pageIndex + 1) * pageSize) >= totalTransactionsCount) ? "" : "/transactions?type="+typeId+"&pageSize=" + pageSize + "&pageIndex=" + (pageIndex + 1));
			pagedResult.setPrev(pageIndex > 0 ? "/transactions?type=" + typeId + "&pageSize=" + pageSize + "&pageIndex=" + (pageIndex - 1) : "");
			pagedResult.setTotalCount(totalTransactionsCount);
			
			return Response.ok(pagedResult).build();
		}
		
		return Response.ok(null).build();
	}
	
	
	/* 
	 *  Add transactions to the current login user.
	 *  This can be a Top-up, Send Money transaction
	 */
	@RolesAllowed("Member")
	@POST
	@Consumes("application/json")
	public Response Add(AddTransaction transaction, @Context HttpHeaders httpHeaders) throws SQLException	{
		// check if recipient id and sender id exist
		// if exist then add to db
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		
		User getUser = this.acctmanager.GetUserById(userId);
		String result = "UNAUTHORIZED";
		if (getUser != null) {
			
			//Boolean successcreate = this.manager.AddTransaction(transaction);
			Boolean successcreate = this.manager.AddTransactionTriggerless(transaction);
			result =  successcreate ? "SUCCESS" : "FAILURE";
		}
		return Response.ok(result).build();
	}
	
	
	/* 
	 *  Add a payment request transactions to the current login user. Payment Request can only be send to another user of the apps.
	 */
	@RolesAllowed("Member")
	@POST
	@Path("payment-request")
	@Consumes("application/json")
	public Response AddPaymentRequest(RequestPayment request, @Context HttpHeaders httpHeaders) throws SQLException	{
		// check if recipient id and sender id exist
		// if exist then add to db
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
			
		User getUser = this.acctmanager.GetUserById(userId);
		String result = "UNAUTHORIZED";
		if (getUser != null) {
			
			Boolean successcreate = this.manager.AddPaymentRequest(request);
			result =  successcreate ? "SUCCESS" : "FAILURE";
		}
		return Response.ok(result).build();
	}
	
	
	/* 
	 *  Delete a payment request transactions created by the current login user (Requestor). 
	 *  Requestee are not allowed to delete a request, he can only accept the request by sending the money requested
	 *  or requestor to delete the request, or wait for the request to be expired
	 */
	@RolesAllowed("Member")
	@DELETE
	@Path("payment-request/{id}")
	@Consumes("application/json")
	public Response DeletePaymentRequest(@PathParam("id") int reqId, @Context HttpHeaders httpHeaders) throws SQLException	{
		// check if recipient id and sender id exist
		// if exist then add to db
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		
		User getUser = this.acctmanager.GetUserById(userId);
		String result = "UNAUTHORIZED";
		if (getUser != null) {
			
			Boolean successcreate = this.manager.DeletePaymentRequest(userId, reqId);
			result =  successcreate ? "SUCCESS" : "FAILURE";
		}
		return Response.ok(result).build();
	}
	
	
	/* 
	 *  Retrive all payment request transactions associate with the current login user. 
	 *  User can be a Requestor or Requestee in these transaction.
	 */
	@RolesAllowed("Member")
	@GET
	@Path("payment-request")
	@Produces("application/json")
	public Response getRequestPaymentTransaction(@Context HttpHeaders httpHeaders) throws SQLException {
	
		// get current jwt and extract payload - role user id
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		ArrayList<RequestPayment> getTransList = this.manager.GetRequestPaymentByUser(userId);
		
		if (getTransList != null && getTransList.size() > 0) {
			Gson gson = new Gson();
			
			JsonElement element = gson.toJsonTree(getTransList, new TypeToken<ArrayList<RequestPayment>>() {}.getType());

			if (! element.isJsonArray()) {
				return Response.ok(null).build();
			} else {

				JsonArray jsonArray = element.getAsJsonArray();
				
				String json = gson.toJson(jsonArray); 
				return Response.ok(json).build();
			}
		}
		
		return Response.ok(null).build();
	}
	
	
	/* 
	 *  Retrieve a transaction summary of the current login user.
	 *  such as the wallet balance, the total top-up amount, total sent amount, total received amount, and the no. of pending payment request.
	 */
	@RolesAllowed("Member")
	@GET
	@Path("summary")
	@Produces("application/json")
	public Response getDashboardSummary(@Context HttpHeaders httpHeaders) throws SQLException {
	
		// get current jwt and extract payload - role user id
		try	{
			String authToken = httpHeaders.getHeaderString("Authorization");
			int userId = Utility.GetUserIdFromAuthToken(authToken);
			DashboardSummary dashSummary = this.manager.GetDashboardSummaryById(userId);
			
			if (dashSummary != null) {
				
				Gson gson = new Gson();
				String json = gson.toJson(dashSummary);
				
	 
				return Response.ok(json).build();
				
			}
		} catch (ExpiredJwtException ejwt)	{
			ejwt.printStackTrace();
			return Response.ok(null).build();
		}
		
		return Response.ok(null).build();
	}
	
	
}
