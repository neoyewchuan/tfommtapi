package com.tfommtapi.resources.v2;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.tfommtapi.auth.jwt.JWTToken;
import com.tfommtapi.auth.jwt.jwtIssuer;
import com.tfommtapi.manager.AccountsManager;
import com.tfommtapi.models.AuthUser;
import com.tfommtapi.models.LoginUser;
import com.tfommtapi.models.User;
import com.tfommtapi.resources.Utility;

@Path("v2/auth")
public class Auth {
	
	private AccountsManager manager = new AccountsManager();
	private static final long TOKEN_TTL = 30 * 60 * 1000;	// 30 minutes
	
	/*
	 *  Alternate method to authenticate the user using username & password or email & password. 
	 *  Accept Json body instead of form data
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response Login(LoginUser access) throws SQLException	{
		
		String resAccess = "UNAUTHORIZED";
		String resToken = "";
		long resTokenTtl = 0;
		long resTokenExp = 0;
		
		if (manager.LoginWithUsernameAndPassword(access.getUsername(), access.getPassword())) {
			// GET account or userid
			User getUser = manager.GetUser(access.getUsername());
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
