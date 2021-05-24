package com.tfommtapi.resources.v2;

import java.sql.SQLException;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.gson.*;
import com.tfommtapi.manager.AccountsManager;
import com.tfommtapi.models.CreateAccount;
import com.tfommtapi.models.v2.GetUserModel;

@Path("v2/users")
public class Users {
	
	private AccountsManager manager = new AccountsManager();

	
	@PermitAll
	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response GetUserTest(@PathParam("id") int userid) throws SQLException	{
		
		GetUserModel usermodel = this.manager.getUserTest(userid);
		
		if (usermodel != null) {
			Gson gson = new Gson();
			String json = gson.toJson(usermodel); 
			return Response.ok(json).build();
		}
		
		return Response.ok(null).build();
	}
	
	
}
