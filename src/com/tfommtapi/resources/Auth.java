package com.tfommtapi.resources;

import java.sql.SQLException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.google.gson.Gson;
import com.tfommtapi.auth.jwt.JWTToken;
import com.tfommtapi.auth.jwt.jwtIssuer;
import com.tfommtapi.models.AuthUser;
import com.tfommtapi.models.User;
import com.tfommtapi.manager.*;

@Path("auth")
public class Auth {
	
	private AccountsManager manager = new AccountsManager();
	private static final long TOKEN_TTL = 30 * 60 * 1000;	// session default to 30 minutes
	
	
	/*
	 *  Authenticate the user using username & password or email & password. 
	 */
	@POST
	@Produces("application/json")
	public Response Login(
			@FormParam("username") String username,
			@FormParam("password") String password) throws SQLException	{
		
		String resAccess = "UNAUTHORIZED";
		String resToken = "";
		long resTokenTtl = 0;
		long resTokenExp = 0;
		
		if (manager.LoginWithUsernameAndPassword(username, password)) {
			// GET account or userid
			User getUser = manager.GetUser(username);
			String userId = getUser.Id + "";
			String roleName = getUser.Role;
			String accessToken = JWTToken.createJWT(jwtIssuer.getId(), jwtIssuer.getIssuer(), "user", 
					userId, roleName, "", TOKEN_TTL);
			resAccess = "AUTHORIZED";
			resToken = accessToken;
			resTokenTtl = TOKEN_TTL;
			resTokenExp = Utility.GetTokenExpiryFromAuthToken(accessToken);
		}
		AuthUser authUser = new AuthUser(resAccess, resToken, resTokenTtl, resTokenExp);
		Gson gson = new Gson();
		String json = gson.toJson(authUser);
		
		return Response.ok(json).build();

		
	}
	
	
	
}
