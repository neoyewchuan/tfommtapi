package com.tfommtapi.resources;

import java.sql.SQLException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.google.gson.*;
import com.tfommtapi.manager.AccountsManager;
import com.tfommtapi.models.ChangePassword;
import com.tfommtapi.models.CreateAccount;
import com.tfommtapi.models.Feedback;
import com.tfommtapi.models.GetMyProfile;
import com.tfommtapi.models.ResetPassword;
import com.tfommtapi.models.UpdateProfile;
import com.tfommtapi.models.User;
import com.tfommtapi.models.UserExistModel;

@Path("users")
public class Users {
	
	private AccountsManager manager = new AccountsManager();
	
	/* 
	 *  Endpoint for querying if the given userid/email exist in database, this is for used during registration process
	 */
	@PermitAll
	@GET
	@Path("{username}")
	@Produces("application/json")
	public Response UserExist(@PathParam("username") String username) throws SQLException	{
		//User user = manager.GetUser(username) ;
		//boolean userExist = (user != null && (user.Username.equals(username)||user.Email.equals(username)));
		UserExistModel obj = new UserExistModel();
		User user = this.manager.GetUser(username);
		if (user != null)	{
			
			if (user.Username.equals(username)) {
				obj.username = "exist";
			} 
			if (user.Email.equals(username)) {
				obj.email = "exist";
			} 
		} else {
			obj.username = "error";
			obj.email = "error";
		}
		

		Gson gson = new Gson();
		String json = gson.toJson(obj); 
		
		return Response.ok(json).build();  
	}
	
	
	/* 
	 *  Retrieve the profile information of the current login user. 
	 */
	@RolesAllowed("Member")
	@GET
	@Path("profile")
	@Produces("application/json")
	public Response getUserProfile(@Context HttpHeaders httpHeaders) throws SQLException {
			
		// get current jwt and extract payload - role user id
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		GetMyProfile getProfile = this.manager.GetUserProfileById(userId);
		
		if (getProfile != null) {
			Gson gson = new Gson();
			String json = gson.toJson(getProfile); 
			return Response.ok(json).build();
		}
		
		return Response.ok(null).build();
	}
	
	
	/* 
	 *  Update the profile info of the current login user. Only the profile picture, first name and last name can be changed after registration.
	 */
	@RolesAllowed("Member")
	@PUT
	@Path("profile")
	@Produces("application/json")
	public Response updateUserProfile(UpdateProfile updProfile, @Context HttpHeaders httpHeaders) throws SQLException {
	
		String theResult = "UNAUTHORIZED";
		// get current jwt and extract payload - role user id
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		User getUser = this.manager.GetUserById(userId);
		
		if (getUser != null) {
			try {
					boolean successupdate = manager.UpdateProfile(
							getUser.Id,
							updProfile.FirstName,
							updProfile.LastName,
							updProfile.ProfilePix);
					
					theResult = successupdate ? "SUCCESS" : "FAILURE";
			} catch (Exception e)	{
				e.printStackTrace();
			}
			return Response.ok(theResult).build();
		}
		
		return Response.ok(theResult).build();
	}
	
	
	/* 
	 *  Endpoint to handle the change password request of current user.
	 */
	@RolesAllowed("Member")
	@PUT
	@Path("chgpass")
	@Consumes("application/json")
	public Response changePassword(ChangePassword chgpwd, @Context HttpHeaders httpHeaders) throws SQLException {
	
		String theResult = "UNAUTHORIZED";
		// get current jwt and extract payload - role user id
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		User getUser = this.manager.GetUserByIdAndPassword(userId, chgpwd.OldPassword);
		
		if (getUser != null) {
			try {
					
					boolean successupdate = manager.UpdatePassword(
							getUser.Id,
							chgpwd.OldPassword, 
							chgpwd.NewPassword);
					
					theResult = successupdate ? "SUCCESS" : "FAILURE";
			} catch (Exception e)	{
				e.printStackTrace();
			}
			return Response.ok(theResult).build();
		}
		
		return Response.ok(theResult).build();
	}
	
	
	/* 
	 *  Endpoint to reset the password for the given email, need to supply email and birth date for verification before  the password can be reset
	 *  The reset password will always be 'password1' as I do not have a backend to handle the sending out email with reset link to user. 
	 */
	@PermitAll
	@POST
	@Path("resetpass")
	@Consumes("application/json")
	public Response resetPassword(ResetPassword resetpwd) throws SQLException {
	
		String theResult = "FAILURE";
		try {
					
				int successreset = manager.ResetPasswordRequest(resetpwd);
					
				theResult = (successreset==1 ? "SUCCESS" : successreset ==-1? "INVALID": "FAILURE");
		} catch (Exception e)	{
				e.printStackTrace();
		}
		
		return Response.ok(theResult).build();
	}
	
	
	/* 
	 *  Create a user to the database
	 */
	@PermitAll
	@POST
	@Consumes("application/json")
	public Response Create(CreateAccount newAccount) throws Exception {
		
		String theResult = "";
		try {
			// check if the registering username/email already exist in system
			if (manager.GetUser(newAccount.getUsername())==null && manager.GetUser(newAccount.getEmail())==null)	{
				boolean successcreate = manager.CreateAccount(
						newAccount.getEmail(), 
						newAccount.getUsername(),
						newAccount.getPassword(),
						newAccount.getFirstName(), 
						newAccount.getLastName(), 
						newAccount.getCountryCode(), 
						newAccount.getMobile(),
						newAccount.getDOB_yrs(),
						newAccount.getDOB_mth(),
						newAccount.getDOB_day(),
						newAccount.getProfilePix(),
						newAccount.getHomeCurrency(),
						"MEMBER");
				theResult = successcreate ? "SUCCESS" : "FAILURE";
			} else {
				theResult = "USER_EXIST";
			}
			
		} catch (Exception ex ){
			// supposed to log this somewhere...
			System.out.println(ex.getMessage());
			//JOptionPane.showMessageDialog(null, ex.getMessage(),JOptionPane.INFORMATION_MESSAGE);
			//throw ex;-
			theResult = "ERROR";
		}
		
		return Response.ok(theResult).build();  
	}
	
	
	/* 
	 *  Allow current login user to send feedback 
	 */
	@RolesAllowed("Member")
	@POST
	@Path("feedback")
	@Consumes("application/json")
	public Response SendFeedback(Feedback feedback,  @Context HttpHeaders httpHeaders) throws Exception {
		
		String theResult = "UNAUTHORIZED";
		// get current jwt and extract payload - role user id
		String authToken = httpHeaders.getHeaderString("Authorization");
		int userId = Utility.GetUserIdFromAuthToken(authToken);
		
		User getUser = this.manager.GetUserById(userId);
		
		if (getUser != null) {
			try {
					
					boolean successpost = this.manager.AddFeedback(feedback);
					
					theResult = successpost ? "SUCCESS" : "FAILURE";
			} catch (Exception e)	{
				e.printStackTrace();
			}
			return Response.ok(theResult).build();
		}
		
		return Response.ok(theResult).build();
	}
	
}
