package com.tfommtapi.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.tfommtapi.models.Feedback;
import com.tfommtapi.models.FundingAcct;
import com.tfommtapi.models.GetMyProfile;
import com.tfommtapi.models.ResetPassword;
import com.tfommtapi.models.User;
import com.tfommtapi.models.v2.GetTransactionModel;
import com.tfommtapi.models.v2.GetUserModel;
import com.tfommtapi.auth.jwt.AES;
import com.tfommtapi.auth.jwt.apiKey;
import com.tfommtapi.database.ConnectionManager;
/*
 * Handles all accounts creation, forget password logic
 */
public class AccountsManager {
	
	FundingManager fundManager = new FundingManager();
	
	/*
	 *  Retrieve the basic user info (username,email,profilepix,userid) using username/email
	 */
	public User GetUser(String username) throws SQLException {
		User getUser = null;
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet result = null;
		try {
			
			stmt = conn.prepareStatement("SELECT * FROM users WHERE (username = ? or email = ?)");
			stmt.setString(1, username);
			stmt.setString(2,  username);
			result = stmt.executeQuery();
			if (result.next()) {
				getUser = new User();
				getUser.Username = result.getString("username");
				getUser.Email = result.getString("Email");
				getUser.ProfilePix = result.getString("profilepix");
				getUser.Role = result.getString("role");
				getUser.Id = result.getInt("id");
				
				// TODO get the others, but no password
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally	{
			// close all resultset, statement and connection 
			if (result != null && !result.isClosed())	{
				result.close();
			}
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return getUser;
	}
	
	
	/* 
	*  Method to login user with the given username/email and password
	*  If password is incorrect, the 'fail_login' will be recorded (incremented) in the 'login' table  
	*  lock_stat change to 1 (from 0) after 5 failed login attempts, 
	*  to indicate the account is locked for 30 mins (the locked_time is recorded for this purpose)
	*  no login is allowed with the 30 mins locked time even the password is correct.
	*  the lock_stat will be set to 0, locked_time set to null after a successful login 
	 */
	public boolean LoginWithUsernameAndPassword(String username, String password) throws SQLException {
		boolean isValid = false, validId = false;
		
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rsUser = null;
		ResultSet rsLogin = null;
		
		try {
			
			String email = username;
			stmt =  conn.prepareStatement("SELECT * FROM users "
					+ "WHERE (username = ? OR email = ?)");
			int i = 1;
			stmt.setString(i++, username);
			stmt.setString(i++, email);
			rsUser = stmt.executeQuery();
			
			validId = rsUser.next();
			
			if (validId)	{
				int theUserId = rsUser.getInt("id");
				stmt2 = conn.prepareStatement("SELECT * FROM login WHERE userid = ? ",
						ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
				stmt2.setInt(1, theUserId);
				rsLogin = stmt2.executeQuery();
				if (!rsLogin.next())	{
					// no login record yet
					rsLogin.moveToInsertRow();
					rsLogin.updateInt("userid", theUserId);
					rsLogin.updateInt("lock_stat", 0);
					rsLogin.updateInt("failed_login", 0);
					rsLogin.updateTimestamp("locked_time",  null);
					rsLogin.updateTimestamp("last_login", new Timestamp(new Date().getTime()));
					rsLogin.insertRow();
				}
				/* verify that the lock_stat is 0 or (lock_stat=1 but already past the 30 mins account locked window)
				 * and the password matches
				 */
				isValid = (rsLogin.getInt("lock_stat")==0 || (rsLogin.getInt("lock_stat")==1 && 
						rsLogin.getTimestamp("locked_time").getTime() + 1800000 < new Date().getTime())) 
						&&  rsUser.getString("password").equals(AES.encrypt(password, apiKey.getSecret()));
				if (isValid)	{
					/* set the lock_stat to 0 to indicate account is not locked
					 * reset the failed_login count, reset the locked_time
					 */
					rsLogin.updateInt("lock_stat", 0);
					rsLogin.updateInt("failed_login", 0);
					rsLogin.updateTimestamp("locked_time",  null);
					rsLogin.updateTimestamp("last_login", new Timestamp(new Date().getTime()));
					rsLogin.updateRow();
					
				} else {
					/* 
					 * password is not correct, increment the failed_login if it is less than 5 
					 */
					if (rsLogin.getInt("failed_login") < 5)	{
						rsLogin.updateInt("failed_login", rsLogin.getInt("failed_login") + 1);
						rsLogin.updateRow();
					}
					/* 
					 * if the failed_login is 5, set the lock_stat to 1, locked_time to current timestamp
					 */
					if (rsLogin.getInt("failed_login") == 5)	{
						rsLogin.updateInt("lock_stat", 1);
						rsLogin.updateTimestamp("locked_time", new Timestamp(new Date().getTime()));
						rsLogin.updateRow();
					}
				}
			}
		} catch (SQLException ex) {
			isValid = false;
		} finally	{
			// close all resultset, statement and connection 
			if (rsUser != null && !rsUser.isClosed())	{
				rsUser.close();
			}
			if (rsLogin != null && !rsLogin.isClosed())	{
				rsLogin.close();
			}
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (stmt2!=null && !stmt2.isClosed())	{
				stmt2.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return isValid;
	}
	

	/* 
	 *  Retrieve the detail of the user by the given userid
	 */
	public User GetUserById(int uid) throws SQLException {
		User getUser = null;
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet result = null;
		try {
	
			stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ? ");
			stmt.setInt(1, uid);
			result = stmt.executeQuery();
			if (result.next()) {
				getUser = new User();
				getUser.Id = result.getInt("Id");
				getUser.Username = result.getString("username");
				getUser.Email = result.getString("Email");
				getUser.FirstName = result.getString("first_name");
				getUser.LastName = result.getString("last_name");
				getUser.CountryCode = result.getString("country_code");
				getUser.Mobile = result.getString("mobile");
				getUser.DOB_yrs = result.getInt("dob_yrs");
				getUser.DOB_mth = result.getInt("dob_mth");
				getUser.DOB_day = result.getInt("dob_day");
				getUser.ProfilePix = result.getString("profilepix");
				getUser.Role = result.getString("role");
				
				// TODO get the others, but no password
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally	{
			// close all resultset, statement and connection 
			if (result != null && !result.isClosed())	{
				result.close();
			}
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return getUser;
	}
	
	
	
	/* 
	 *  Retrieve the full profile detail of the user by the given userid
	 */
	public GetMyProfile GetUserProfileById(int uid) throws SQLException {
		GetMyProfile getProfile = null;
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet result = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ? ");
			stmt.setInt(1, uid);
			result = stmt.executeQuery();
			if (result.next()) {
				getProfile = new GetMyProfile();
				getProfile.setId(result.getInt("id"));
				getProfile.setUsername(result.getString("username"));
				getProfile.setEmail(result.getString("Email"));
				getProfile.setFirst_name(result.getString("first_name"));
				getProfile.setLast_name(result.getString("last_name"));
				getProfile.setCountry_code(result.getString("country_code"));
				getProfile.setMobile(result.getString("mobile"));
				getProfile.setDob_yrs(result.getInt("dob_yrs"));
				getProfile.setDob_mth(result.getInt("dob_mth"));
				getProfile.setDob_day(result.getInt("dob_day"));
				getProfile.setProfile_pix(result.getString("profilepix"));
				getProfile.setRole(result.getString("role"));
				getProfile.setHomeCurrency(result.getString("home_currency"));
				
				// TODO get the others, but no password
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally	{
			// close all resultset, statement and connection 
			if (result != null && !result.isClosed())	{
				result.close();
			}
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return getProfile;
	}
	
	
	/* 
	 * Retrieve the very basic of user by the given id and password, for authentication
	 */
	public User GetUserByIdAndPassword(int uid, String pwd) throws SQLException {
		User getUser = null;
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet result = null;
		try {
			
			stmt = conn.prepareStatement("SELECT Id, Username, Email FROM users WHERE id = ? AND password = ?");
			stmt.setInt(1, uid);
			stmt.setString(2,  AES.encrypt(pwd, apiKey.getSecret()));
			result = stmt.executeQuery();
			if (result.next()) {
				getUser = new User();
				getUser.Id = result.getInt("Id");
				getUser.Username = result.getString("Username");
				getUser.Email = result.getString("Email");
				// Just get the basic
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally	{
			// close all resultset, statement and connection 
			if (result != null && !result.isClosed())	{
				result.close();
			}
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return getUser;
	}
	
	
	@SuppressWarnings("resource")
	public boolean CreateAccount(String email, String username ,String password, String firstName, 
			String lastName, String countryCode, String mobile, int dob_yrs, int dob_mth, int dob_day, 
			String profile_pix, String home_currency, String role) throws SQLException {

		// set auto commit to false for this connection
		// 1. create the user account (already validated non-duplicated user prior to this function) 
		// 2. check that the insert is successful
		// 3. retrieve the auto-generated id of user
		// 4. generate the account number for the default EzyWallet account
		// 5. create the EzyWallet account in the fundingacct table, 
		// if successful commit and return true, otherwise rollback and return false
		boolean result = true;
		Connection conn = ConnectionManager.Get();
		conn.setAutoCommit(false);
		PreparedStatement stmt = null;
		
		try {
			
			stmt = conn.prepareStatement("INSERT INTO users "
					+ "(email, username, password, first_name, last_name, country_code, mobile, dob_yrs, dob_mth, dob_day, profilepix, home_currency, role, create_time) "
					+ "VALUES (?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			stmt.setString(1, email);
			stmt.setString(2, username);
			stmt.setString(3, AES.encrypt(password, apiKey.getSecret()));
			stmt.setString(4, firstName);
			stmt.setString(5, lastName);
			stmt.setString(6, countryCode);
			stmt.setString(7, mobile);
			stmt.setInt(8, dob_yrs);
			stmt.setInt(9, dob_mth);
			stmt.setInt(10, dob_day);
			stmt.setString(11,  profile_pix);
			stmt.setString(12,  home_currency);
			stmt.setString(13, role);
			stmt.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
			
			int insRes = stmt.executeUpdate();
			if (insRes ==1)	{
				/* Generate a new account number for the EzyWallet account only if the user is successfully created
				 * Account number will have the format of CYY-MM-{7 digits USERID left pad with 0}-{CHECK DITGIT}
				 * Eg. userid of 43 who registered in Jan'19 would have the account number 120-01-0000043-9
				 */
				Calendar credDate = Calendar.getInstance();
				// Year representation in 3 digits form, Eg. 2020 - 1900 = 120, 2019 - 1900 = 119
				String acct_numbr_1 = String.valueOf(credDate.get(Calendar.YEAR)-1900);
				String acct_numbr_2 = String.format("%02d", credDate.get(Calendar.MONTH)+1);
				//String acct_numbr_p1 = acct_numbr_1 + "-" + acct_numbr_2;
				String queryAcctNumbr = "SELECT Id from users WHERE email = ? AND username = ? ";
				stmt = conn.prepareStatement(queryAcctNumbr);
				stmt.setString(1, email);
				stmt.setString(2, username);
				ResultSet res1 = stmt.executeQuery();
				if (res1.next())	{
							
					int theUserId = res1.getInt("Id");
					String acct_numbr_3 = String.format("%07d",  theUserId);
					String acct_numbr123 = acct_numbr_1 + acct_numbr_2 + acct_numbr_3;
					String acct_chkdigit = checkDigit(acct_numbr123);
					String acct_numbr_x = acct_numbr_1 + "-" + acct_numbr_2 + "-" + acct_numbr_3 + "-" + acct_chkdigit;
					
					FundingAcct fundingAcct = new FundingAcct(theUserId, "EzyWallet", acct_numbr_x,
								0.00, 1, home_currency, "EW", countryCode, "", "EzyWallet");
					
					int insRes2 = fundManager.AddFundingAcct(fundingAcct);
					
					if (insRes2==1)	{
						result = true;
						// commit onlt when funding account is successfully created
						conn.commit();
					} else	{
						// else roll back
						conn.rollback();
						result = false;
					}
				}
				
			}
		} catch (SQLException ex) {
			if (conn != null)	{
				conn.rollback();
			}
			result = false;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally	{
			// close all resultset, statement and connection 
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		
		return result;
	
	}
	
	
	
	
	/* 
	 * Function to calculate the check digit of a given 12 digits of number
	 * Modular 10 check digit system
	 */
	private String checkDigit(String acnt123) {
		// TODO Auto-generated method stub
		if (acnt123.length()!=12)	{
			return " ";
		} 
		int SUM = 0;
		int ptr = 1;
		while (ptr <= 12) {
			int ONE = 0;
			ONE = Character.getNumericValue(acnt123.charAt(ptr-1));
			if (ptr % 2 == 0)	{
				ONE *= 3;
			}
			SUM += ONE;
			ptr++;
		}
		SUM %= 10;
		if (SUM != 0)	{	
			return String.valueOf(10-SUM);
		}
		return "0";
	}

	
	/* 
	 *  Method to handle the updating of the user profile
	 */
	public boolean UpdateProfile(int Id, String firstName, 	String lastName, String profile_pix) throws SQLException {
		
		int result = 0;
		Connection conn = ConnectionManager.Get();
		conn.setAutoCommit(false);
		PreparedStatement stmt = null;
		try {
			
			// update the profile of user
			
			 stmt = conn.prepareStatement("UPDATE users "
					+ "SET first_name = ?, last_name = ?, profilepix = ? "
					+ "WHERE Id = ? ") ;
			 
			 stmt.setString(1, firstName);
			 stmt.setString(2, lastName);
			 stmt.setString(3, profile_pix);
			 stmt.setInt(4, Id);

			 result = stmt.executeUpdate();
			 if (result == 1)	{
				 // update the profile_pix in the payee table so that all the correspondence payees see the updated profile pix also.
				 stmt = conn.prepareStatement("UPDATE payee SET payee_pfpix = ? "
						 + "WHERE payee_uid = ? ");
				 stmt.setString(1, profile_pix);
				 stmt.setInt(2, Id);
				 stmt.executeUpdate();
				 conn.commit();
			 }
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			conn.rollback();
			result = 0;
		} finally {
			// close all resultset, statement and connection 
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}	
		return (result == 1);		// return true if 1 record is updated, otherwise false
	}
	
	
	/* 
	 *  Methos to update the user password
	 */
	public boolean UpdatePassword(int Id, String oldpwd, String newpwd) throws SQLException {
		
		int result = 0;
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		try {
			
			 stmt = conn.prepareStatement("UPDATE users "
					+ "SET password = ? "
					+ "WHERE Id = ? AND password = ? ") ;
			 // encrypt the new password when storing to user table
			 stmt.setString(1, AES.encrypt(newpwd, apiKey.getSecret()));
			 stmt.setInt(2, Id);
			 // encrypt the supplied password to compare with the encrypted password in the table to validate the user identity
			 stmt.setString(3, AES.encrypt(oldpwd, apiKey.getSecret()));
			 result = stmt.executeUpdate();
			 //System.out.println(result);
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			result = 0;
		} finally {
			// close all resultset, statement and connection 
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}	
		return (result == 1);	
	}

	
	/*
	 *  Just to retrieve the username of the given user
	 */
	protected String GetUserNameById(int uid) throws SQLException {
		// TODO Auto-generated method stub
		String username = "";
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet result = null;
		try {
			stmt = conn.prepareStatement("SELECT username FROM users WHERE id = ? ");
			stmt.setInt(1, uid);
			result = stmt.executeQuery();
			if (result.next()) {
				username = result.getString("username");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally	{
			if (result != null && !result.isClosed())	{
				result.close();
			}
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return username;
	}

	
	/* 
	 *  Method to handle the reset password request
	 *  User need to supply the registed email and birth date for verification
	 */
	public int ResetPasswordRequest(ResetPassword resetpwd) throws SQLException {
		// TODO Auto-generated method stub
		int result = 0;
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			
			 stmt = conn.prepareStatement("SELECT  * FROM users "
					+ "WHERE email = ? "
					+ "AND DOB_yrs = ? AND DOB_mth = ? AND DOB_day = ? ") ;
			 
			 stmt.setString(1, resetpwd.getEmail());
			 stmt.setInt(2, resetpwd.getDob_yrs());
			 stmt.setInt(3, resetpwd.getDob_mth());
			 stmt.setInt(4, resetpwd.getDob_day());
			 ResultSet res = stmt.executeQuery();
			 if (res.next()) {
				 if (res.getString("email").equals(resetpwd.getEmail()))	{
					  stmt2 = conn.prepareStatement("UPDATE users SET password = ? "
							 + "WHERE id = ? AND email = ? ");
					  // as no backend to handle the sending out of reset eamil, all password 
					  // will be reset to 'password1' here
					 stmt2.setString(1, AES.encrypt("password1", apiKey.getSecret()));
					 stmt2.setInt(2,  res.getInt("Id"));
					 stmt2.setString(3,  res.getString("email"));
					 result = stmt2.executeUpdate();
				 } else {
					 result = -1;
				 }
			 } else {
				 result = -1;
			 }
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			result = 0;
		} finally {
			// close all resultset, statement and connection 
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (stmt2!=null && !stmt2.isClosed()) {
				stmt2.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}	
		return (result);	
	}

	
	/*
	 *  Method to handle/record the feedback submitted by user within the apps.
	 */
	public boolean AddFeedback(Feedback feedback) throws SQLException {

			boolean result = true;
			Connection conn = ConnectionManager.Get();
			//conn.setAutoCommit(false);
			PreparedStatement stmt = null;
			
			try {
				
				stmt = conn.prepareStatement("INSERT INTO feedback "
						+ "(uid, uname, uemail, type, feedback)  "
						+ "VALUES (?, ? , ?, ?, ?) ");
				
				stmt.setInt(1, feedback.getFeedbackUid());
				stmt.setString(2, feedback.getFeedbackName());
				stmt.setString(3, feedback.getFeedbackEmail());
				stmt.setString(4, feedback.getFeedbackType());
				stmt.setString(5, feedback.getFeedbackBody());
				
				//int insRes = stmt.executeUpdate();
				stmt.executeUpdate();
//				result = (insRes == 1);
			} catch (SQLException ex)	{
				result = false;
				ex.printStackTrace();
			} finally	{
				// close all resultset, statement and connection 
				if (stmt!=null && !stmt.isClosed())	{
					stmt.close();
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			}
			return result;
	}

	
	/*
	 *  Deprecated 
	 */
	@SuppressWarnings("resource")
	public GetUserModel getUserTest(int userid) throws SQLException {
		GetUserModel getUserModel = new GetUserModel();
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet result = null;
		try {
			
			stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ? ");
			stmt.setInt(1, userid);
			result = stmt.executeQuery();
			if (result.next()) {
				getUserModel.getProfile().setId(result.getInt("id"));
				getUserModel.getProfile().setUsername(result.getString("username"));
				getUserModel.getProfile().setEmail(result.getString("Email"));
				getUserModel.getProfile().setFirstName(result.getString("first_name"));
				getUserModel.getProfile().setLastName(result.getString("last_name"));
				getUserModel.getProfile().setCountryCode(result.getString("country_code"));
				getUserModel.getProfile().setMobile(result.getString("mobile"));
				getUserModel.getProfile().setDOB_yrs(result.getInt("dob_yrs"));
				getUserModel.getProfile().setDOB_mth(result.getInt("dob_mth"));
				getUserModel.getProfile().setDOB_day(result.getInt("dob_day"));
				getUserModel.getProfile().setRole(result.getString("role"));
				getUserModel.getProfile().setHomeCurrency(result.getString("home_currency"));
				
				stmt = conn.prepareStatement("SELECT * FROM transactions WHERE userid = ? ") ;
				stmt.setInt(1, userid);
				ResultSet theResult = stmt.executeQuery();
					
				while (theResult.next())	{
						GetTransactionModel tr = new GetTransactionModel();
						tr.setAmount(theResult.getDouble("amount"));
						tr.setBankAccount(theResult.getString("bankaccount"));
						tr.setUserId(theResult.getInt("userid"));
						tr.setFromTo( theResult.getInt("fromto"));
						tr.setCurrency( theResult.getString("currency"));
						tr.setFromTo_Factor(theResult.getDouble("fromto_factor"));
						tr.setFromTo_Currency(theResult.getString("fromto_currency"));
						tr.setTransactionType(theResult.getInt("transaction_type"));	// enum TransactionType.SEND // TOPUP // RECEIVE
						tr.setTransactionDateTime(theResult.getTimestamp("transaction_datetime")); // country code, 65-SG, 60-MY, 0-US, 851-CN
						tr.setRemarks(theResult.getString("remarks"));
						
						getUserModel.getListTransaction().add(tr);
				}
				
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally	{
			// close all resultset, statement and connection 
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return getUserModel;
		}


	
	
	
	
}
