package com.tfommtapi.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.tfommtapi.database.ConnectionManager;
import com.tfommtapi.models.AddPayee;
import com.tfommtapi.models.GetPayee;
import com.tfommtapi.models.RemovePayee;
import com.tfommtapi.models.User;

public class PayeeManager {
	
	AccountsManager manager = new AccountsManager();
	
	
	/*
	 *  Associate the given payee to user
	 *  payee can be another app user or band account
	 */
	@SuppressWarnings("resource")
	public int AddPayeeToUser(AddPayee addpayee) throws SQLException	{
		int theResult = 1;
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		try {
			
			User payee ;
			if (addpayee.getPayeeId() > 0)	{
				payee = this.manager.GetUserById(addpayee.getPayeeId());
			} else {
				payee = this.manager.GetUser(addpayee.getPayee());
			}
			
			//User payee = this.manager.GetUser(addpayee.getPayee());
			if (payee != null)	{
				if (payee.Id != addpayee.getUserId() && (payee.Email.equals(addpayee.getPayee()) || payee.Username.equals(addpayee.getPayee())))	{
			
					stmt = conn.prepareStatement("SELECT * FROM payee WHERE userid = ? AND payee_uid = ?");
					stmt.setInt(1,  addpayee.getUserId());
					stmt.setInt(2,  payee.Id);
					ResultSet res = stmt.executeQuery();
					// check if payee already exist associated to user
					if (res.next()) {
						//if (res.getInt("app_user")==0)
						theResult = 0;
					} else {	
							String insertString = "INSERT into payee (userid, payee_uid, payee_alias, payee_email, payee_uname, payee_pfpix, app_user, bank_name, bank_acnt, home_currency) "
									+ "VALUES (?, ?, ?, ? , ?, ?, ?, ?, ?, ?) " ;
							
							stmt = conn.prepareStatement(insertString);
					 
							stmt.setInt(1, addpayee.getUserId());
							stmt.setInt(2, payee.Id);
							stmt.setString(3, addpayee.getPayeeAlias());
							stmt.setString(4, payee.Email);
							stmt.setString(5,  payee.Username);
							stmt.setString(6, payee.ProfilePix );
							stmt.setInt(7,  1);	// 1= app user, 0=non app user
							stmt.setString(8, "");	// bank name
							stmt.setString(9, "");	// bank account number
							stmt.setString(10,  addpayee.getHomeCurrency());
							stmt.execute();
					}
				} else {
					if (payee.Id == addpayee.getUserId()) {
						theResult = -2;		//  Cannot add self as payee
					} else {
						if (!payee.Email.equals(addpayee.getPayee()) && !payee.Username.equals(addpayee.getPayee()))	{
							theResult = -4;	// Invalid Payee info
						}
					}
				}
			} else {
				theResult = -3; // payee doesn't exist
				// TO-DO
				// add as custom payee? i.e. payee who is not a user of the app
				stmt = conn.prepareStatement("SELECT * FROM payee WHERE userid = ? AND bank_acnt = ?");
				stmt.setInt(1,  addpayee.getUserId());
				stmt.setString(2,  addpayee.getBankAcnt());
				ResultSet res = stmt.executeQuery();
				// check if payee already exist associated to user
				if (res.next()) {
					//if (res.getInt("app_user")==0)
					theResult = 0;
				} else {	
						String insertString = "INSERT into payee (userid, payee_uid, payee_alias, payee_email, payee_uname, payee_pfpix, app_user, bank_name, bank_acnt, swift_codes, home_currency) "
							+ "VALUES (?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?) " ;
					stmt = conn.prepareStatement(insertString);
					 
					stmt.setInt(1, addpayee.getUserId());
					stmt.setInt(2, addpayee.getPayeeId());
					stmt.setString(3, addpayee.getPayeeAlias());
					stmt.setString(4, "");
					stmt.setString(5,  addpayee.getPayee());
					stmt.setString(6, "" );
					stmt.setInt(7,  0);	// 1= app user, 0=non app user
					stmt.setString(8,  addpayee.getBankName());
					stmt.setString(9,  addpayee.getBankAcnt());
					stmt.setString(10,  addpayee.getSwiftCodes());
					stmt.setString(11,  addpayee.getHomeCurrency());
					stmt.execute();
					theResult = 1;
				}	
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			theResult = -1;
		} finally	{
			// close all resultset, statement and connection 
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		
		return theResult;
	}
	
	
	/*
	 *  Retrieve the list of payees associated to the user
	 *  It also retrieve summary of the sent (moneyfromyou) /received (moneytoyou) amount between the user and the payee
	 */
	public ArrayList<GetPayee> GetListPayee(int id, int type) throws SQLException {
		
		ArrayList<GetPayee> arrayList = new ArrayList<GetPayee>();
		Connection conn  = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet theResult = null;
		try	{
			String queryString = "SELECT py.payee_uid, py.payee_alias, py.payee_uname, py.payee_email, py.payee_pfpix,py.bank_name,"+
					"py.bank_acnt, py.swift_codes, py.home_currency, py.app_user,fa.acct_country," +
					"SUM(case when tr.fromto=py.payee_uid AND tr.transaction_type=103 then tr.amount else 0 end) as moneytoyou, " +
					"SUM(case when tr.fromto=py.payee_uid AND tr.transaction_type=102 then tr.amount else 0 end) as moneyfromyou " +
					"FROM payee py left join transactions tr ON (py.userid = tr.userid) " + 
					"left join fundingacct fa on (py.payee_uid = fa.uid) " +
					"WHERE py.userid = ?  AND fa.acct_type=1 " + 
					((type==0 || type==1)? " AND py.app_user="+String.valueOf(type): " ") + 
						" GROUP BY py.userid, py.payee_uid, py.payee_uname,py.payee_alias,py.payee_uname,py.payee_email,py.payee_pfpix,py.bank_name,py.bank_acnt,py.swift_codes,py.home_currency,py.app_user,fa.acct_country ";	
			
			stmt = conn.prepareStatement(queryString);
			stmt.setInt(1, id);
			//stmt.setInt(2,  id);
			
			theResult = stmt.executeQuery();
			
			while (theResult.next())	{
				GetPayee gp = new GetPayee();
				gp.setId(theResult.getInt("payee_uid"));
				gp.setUsername(theResult.getString("payee_uname"));
				gp.setAliasName(theResult.getString("payee_alias"));
				gp.setEmail(theResult.getString("payee_email"));
				gp.setProfilePix(theResult.getString("payee_pfpix"));
				gp.setMoneyToYou(theResult.getDouble("moneytoyou"));
				gp.setMoneyFromYou(theResult.getDouble("moneyfromyou"));
				gp.setBankName(theResult.getString("bank_name"));
				gp.setBankAcnt(theResult.getString("bank_acnt"));
				gp.setSwiftCodes(theResult.getString("swift_codes"));
				gp.setHomeCurrency(theResult.getString("home_currency"));
				gp.setAcctCountry(theResult.getString("acct_country"));
				gp.setAppUser(theResult.getInt("app_user"));

				
				arrayList.add(gp);
			}
			theResult.close();
			stmt.close();
			conn.close();
			return arrayList;
		} catch (SQLException ex)	{
			ex.printStackTrace();
		} finally	{
			// close all resultset, statement and connection 
			if (theResult != null && !theResult.isClosed())	{
				theResult.close();
			}
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		
		return null;
	}
	
	
	/* 
	 *  Retrieve a payee with joint information (acct_country) from the funding account necessary for used in the Android apps 
	 *  to display the supported currency for the payee country
	 */
	public GetPayee GetBasicPayee(String searched_string) throws SQLException {
		// TODO Auto-generated method stub
		
		GetPayee gp = new GetPayee();
		Connection conn  = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet theResult = null;
		try	{
			String queryString = "SELECT ur.id, ur.username, ur.first_name, ur.last_name, ur.email, ur.profilepix, ur.home_currency, fa.acct_country " + 
					"FROM users ur left join fundingacct fa ON (ur.id = fa.uid) WHERE (ur.username = ? OR ur.email = ? OR ur.mobile = ? ) AND fa.acct_type=1 ";
			
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,  searched_string);
			stmt.setString(2,  searched_string);
			stmt.setString(3,  searched_string);
			
			theResult = stmt.executeQuery();
			
			if (theResult.next())	{
				
				gp.setId(theResult.getInt("id"));
				gp.setUsername(theResult.getString("username"));
				gp.setAliasName("");
				gp.setEmail(theResult.getString("email"));
				gp.setProfilePix(theResult.getString("profilepix"));
				gp.setMoneyToYou(0.00);
				gp.setMoneyFromYou(0.00);
				gp.setBankName("");
				gp.setBankAcnt("");
				gp.setSwiftCodes("");
				gp.setHomeCurrency(theResult.getString("home_currency"));
				gp.setAcctCountry(theResult.getString("acct_country"));
				gp.setAppUser(1);

			}
			theResult.close();
			stmt.close();
			conn.close();
			
		} catch (SQLException ex)	{
			ex.printStackTrace();
		} finally	{
			// close all resultset, statement and connection 
			if (theResult != null && !theResult.isClosed())	{
				theResult.close();
			}
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		
		return gp;
	}

	
	/* 
	 *  Safely remove (the association) the selected payee from the given user  
	 *  For transaction historical data completeness, the payee cannot be remove 
	 *  if there is associate transaction between the user and payee 
	 */
	@SuppressWarnings("resource")
	public int SafeRemovePayee(RemovePayee removePayee) throws SQLException  {
		int theResult = 1;
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		try {
			User payee ;
			if (removePayee.getPayeeId() > 0)	{
				payee = this.manager.GetUserById(removePayee.getPayeeId());
			} else {
				payee = this.manager.GetUser(removePayee.getPayee());
			}
			if (payee != null)	{
				if (payee.Id != removePayee.getUserId())	{
					
					// check if there is transaction exist between user and payee
					// forbid the delete if there is transaction.
					// else remove the payee from user 
					
					stmt = conn.prepareStatement("SELECT * FROM transactions WHERE (userid = ? AND fromto = ?) OR (userid = ? AND fromto = ?) ");
					stmt.setInt(1,  removePayee.getUserId());
					stmt.setInt(2,  payee.Id);
					
					stmt.setInt(3,  payee.Id);
					stmt.setInt(4,  removePayee.getUserId());
					ResultSet res = stmt.executeQuery();
					if (res.next()) {
						theResult = 0;
					} else {
						stmt = conn.prepareStatement("DELETE  FROM payee WHERE (userid = ? AND payee_uid = ?) ");
						stmt.setInt(1,  removePayee.getUserId());
						stmt.setInt(2,  payee.Id);
						theResult= stmt.executeUpdate();
						if (theResult!=1) {
							theResult = -1;
						}
						//theResult  = deleted? 1 : -1;
					}
//					stmt = conn.prepareStatement("SELECT * FROM payee WHERE userid = ? AND payee_uid = ?");
//					stmt.setInt(1,  addpayee.getUserId());
//					stmt.setInt(2,  payee.Id);
//					ResultSet res = stmt.executeQuery();
//					// check if payee already exist associated to user
//					if (res.next()) {
//						//if (res.getInt("app_user")==0)
//						theResult = 0;
//					} else {	
//							String insertString = "INSERT into payee (userid, payee_uid, payee_alias, payee_email, payee_uname, payee_pfpix, app_user, bank_name, bank_acnt, home_currency) "
//									+ "VALUES (?, ?, ?, ? , ?, ?, ?, ?, ?, ?) " ;
//							
//							stmt = conn.prepareStatement(insertString);
//					 
//							stmt.setInt(1, addpayee.getUserId());
//							stmt.setInt(2, payee.Id);
//							stmt.setString(3, addpayee.getPayeeAlias());
//							stmt.setString(4, payee.Email);
//							stmt.setString(5,  payee.Username);
//							stmt.setString(6, payee.ProfilePix );
//							stmt.setInt(7,  1);	// 1= app user, 0=non app user
//							stmt.setString(8, "");	// bank name
//							stmt.setString(9, "");	// bank account number
//							stmt.setString(10,  addpayee.getHomeCurrency());
//							stmt.execute();
//					}
				} else {
					if (payee.Id == removePayee.getUserId()) {
						theResult = -2;		//  Cannot remove self 
					} else {
						if (!payee.Email.equals(removePayee.getPayee()) && !payee.Username.equals(removePayee.getPayee()))	{
							theResult = -4;	// Invalid Payee info
						}
					}
				}
			} else {
				theResult = -3; // payee doesn't exist		
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			theResult = -1;
		} finally	{
			// close all resultset, statement and connection 
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		
		return theResult;
	}
	
//	public ArrayList<GetPayee> GetBasicPayee(String searched_string) throws SQLException {
//		// TODO Auto-generated method stub
//		
//		ArrayList<GetPayee> listpayee = new ArrayList<GetPayee>();
//		Connection conn  = ConnectionManager.Get();
//		PreparedStatement stmt = null;
//		ResultSet theResult = null;
//		try	{
//			String queryString = "SELECT id, username, email, profilepix " + 
//					"FROM users WHERE CONCAT(username,email,mobile) like '%"+searched_string+"%' GROUP BY id, username,email,profilepix ";
//			stmt = conn.prepareStatement(queryString);
//			theResult = stmt.executeQuery(queryString);
//			
//			while (theResult.next())	{
//				GetPayee gp = new GetPayee();
//				gp.Id = theResult.getInt("id");
//				gp.Username = theResult.getString("username");
//				gp.AliasName = "";
//				gp.Email = theResult.getString("email");
//				gp.ProfilePix = theResult.getString("profilepix");
//				gp.MoneyToYou = 0.00;
//				gp.MoneyFromYou = 0.00;
//				gp.BankName = "";
//				gp.BankAcnt = "";
//				gp.AppUser = 1;
//
//				
//				listpayee.add(gp);
//			}
//			theResult.close();
//			stmt.close();
//			conn.close();
//			return listpayee;
//		} catch (SQLException ex)	{
//			ex.printStackTrace();
//		} finally	{
//			if (theResult != null && !theResult.isClosed())	{
//				theResult.close();
//			}
//			if (stmt!=null && !stmt.isClosed())	{
//				stmt.close();
//			}
//			if (conn != null && !conn.isClosed()) {
//				conn.close();
//			}
//		}
//		
//		return null;
//	}
}
