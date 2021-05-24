package com.tfommtapi.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tfommtapi.database.ConnectionManager;
import com.tfommtapi.models.FundingAcct;

public class FundingManager {
	
	
	
	/* 
	 *  Retrieve the funding accounts associated with the user
	 */
	public ArrayList<FundingAcct> GetMyFundingAcct(int id) throws SQLException {
		ArrayList<FundingAcct> arrayList = new ArrayList<FundingAcct>();
		Connection conn  = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet theResult = null;
		try	{
			String queryString = "SELECT uid, acct_name, acct_numbr, acct_balc, acct_type, acct_currency, short_name, acct_country,swift_codes, acct_alias " +
					"FROM fundingacct WHERE uid = ?  " + 
						"ORDER BY acct_type, short_name, acct_name, acct_numbr ";		
			
			stmt = conn.prepareStatement(queryString);
			stmt.setInt(1, id);

			theResult = stmt.executeQuery();
			
			while (theResult.next())	{
				FundingAcct fund = new FundingAcct();
				fund.setId(theResult.getInt("uid"));
				fund.setAcctName(theResult.getString("acct_name"));
				fund.setAcctNumber(theResult.getString("acct_numbr"));
				fund.setAcctBalance(theResult.getDouble("acct_balc"));
				fund.setAcctType(theResult.getInt("acct_type"));
				fund.setAcctCurrency(theResult.getString("acct_currency"));
				fund.setShortName(theResult.getString("short_name"));
				fund.setAcctCountry(theResult.getString("acct_country"));
				fund.setSwiftCodes(theResult.getString("swift_codes"));
				fund.setAcctAlias(theResult.getString("acct_alias"));
				
				arrayList.add(fund);
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
	 *  Add a new funding account to the user
	 */
	@SuppressWarnings("resource")
	public int AddFundingAcct(FundingAcct funding) throws SQLException {
		
		int theResult = 1;
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		try {
			
			
			// check if funding account already associate with user
			String queryFundingAcct = "SELECT * FROM fundingacct WHERE uid = ? AND acct_type = ? " +
							" AND acct_numbr = ? ";
			stmt = conn.prepareStatement(queryFundingAcct);
			stmt.setInt(1,  funding.getId());
			stmt.setInt(2,  funding.getAcctType());
			stmt.setString(3,  funding.getAcctNumber());
			ResultSet res = stmt.executeQuery();
			// funding account already exist return 0
			if (res.next()) {
				theResult = 0;
			} else {	
				String insertString = "INSERT into fundingacct (uid, acct_name, acct_numbr, acct_balc, acct_type, acct_currency, short_name, acct_country, swift_codes, acct_alias) "
									+ "VALUES (?, ?, ?, ? , ?, ?, ?, ?, ?, ?) " ;
							
				stmt = conn.prepareStatement(insertString);						
				stmt.setInt(1, funding.getId());
				stmt.setString(2, funding.getAcctName());
				stmt.setString(3, funding.getAcctNumber());
				stmt.setDouble(4, funding.getAcctBalance());
				stmt.setInt(5,  funding.getAcctType());
				stmt.setString(6, funding.getAcctCurrency());
				stmt.setString(7, funding.getShortName());	
				stmt.setString(8,  funding.getAcctCountry());
				stmt.setString(9,  funding.getSwiftCodes());
				stmt.setString(10,  funding.getAcctAlias());
				theResult = stmt.executeUpdate();
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
	 *  Retrieve the associated funding account of the user by the given account type
	 */
	public ArrayList<FundingAcct> GetMyFundingAcctByType(int id, int acct_type) throws SQLException {
		ArrayList<FundingAcct> arrayList = new ArrayList<FundingAcct>();
		Connection conn  = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet theResult = null;
		try	{
			String queryString = "";
			// type ?
			// 0 = all , 1 = wallet, 2 = local bank account, 3 = foreign bank account, 5 = local+foreign bank account	
			if (acct_type == 5)	{
				queryString = "SELECT uid, acct_name, acct_numbr, acct_balc, acct_type, acct_currency, short_name, acct_country, swift_codes, acct_alias " +
					"FROM fundingacct WHERE uid = ?  AND (acct_type = 2 OR acct_type = 3) " + 
						"ORDER BY acct_type, short_name, acct_name, acct_numbr ";		
				stmt = conn.prepareStatement(queryString);
				stmt.setInt(1, id);
			} else {
				queryString = "SELECT uid, acct_name, acct_numbr, acct_balc, acct_type, acct_currency, short_name, acct_country, swift_codes, acct_alias " +
						"FROM fundingacct WHERE uid = ?  AND acct_type = ? " + 
							"ORDER BY acct_type, short_name, acct_name, acct_numbr ";		
				stmt = conn.prepareStatement(queryString);
				stmt.setInt(1, id);
				stmt.setInt(2,  acct_type);
			}
			theResult = stmt.executeQuery();
			
			while (theResult.next())	{
				FundingAcct fund = new FundingAcct();
				fund.setId(theResult.getInt("uid"));
				fund.setAcctName(theResult.getString("acct_name"));
				fund.setAcctNumber(theResult.getString("acct_numbr"));
				fund.setAcctBalance(theResult.getDouble("acct_balc"));
				fund.setAcctType(theResult.getInt("acct_type"));
				fund.setAcctCurrency(theResult.getString("acct_currency"));
				fund.setShortName(theResult.getString("short_name"));
				fund.setAcctCountry(theResult.getString("acct_country"));
				fund.setSwiftCodes(theResult.getString("swift_codes"));
				fund.setAcctAlias(theResult.getString("acct_alias"));
				
				arrayList.add(fund);
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
	

}
