package com.tfommtapi.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import com.tfommtapi.database.ConnectionManager;
import com.tfommtapi.models.DashboardSummary;
import com.tfommtapi.models.RequestPayment;
import com.tfommtapi.models.Transaction;
import com.tfommtapi.models.TransactionType;
import com.tfommtapi.resources.Utility;

public class TransactionManager {
	
	AccountsManager acctmanager = new AccountsManager();
	
	
	/* 
	 *  Retrieve the transactions associated with the given user
	 *  The transaction can be a Top-up, Send Money, Received Money or all transaction type
	 *  Only transaction with date <= current timestamp will be returned, future date transaction will be ignored  
	 */
	// ******************************************************************************************************
	public ArrayList<Transaction> GetTransactionsById(int userId, int trantype) throws SQLException	{
		ArrayList<Transaction> arrayList = new ArrayList<Transaction>();
		Connection conn  = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet theResult = null ;
		try	{
			String queryString = "SELECT * FROM transactions " ;
			//Timestamp ts = new Timestamp(System.currentTimeMillis());
			switch (trantype) {
				case TransactionType.TOPUP :
					queryString += "WHERE transaction_type = ? AND userid = ?  AND release_date <= CURRENT_TIMESTAMP" ;
					stmt = conn.prepareStatement(queryString);
					stmt.setInt(1,  TransactionType.TOPUP);
					stmt.setInt(2,  userId);
					break;
				case TransactionType.SEND :
					queryString += "WHERE transaction_type = ? AND userid = ?  AND release_date <= CURRENT_TIMESTAMP" ;
					stmt = conn.prepareStatement(queryString);
					stmt.setInt(1,  TransactionType.SEND);
					stmt.setInt(2,  userId);
					break;
				case TransactionType.RECEIVE :
					queryString += "WHERE transaction_type = ? AND userid = ?   AND release_date <=CURRENT_TIMESTAMP" ;
					stmt = conn.prepareStatement(queryString);
					stmt.setInt(1,  TransactionType.RECEIVE);
					stmt.setInt(2,  userId);
					break;
				default :
					queryString += "WHERE userid = ? AND release_date <= CURRENT_TIMESTAMP" ;
					stmt = conn.prepareStatement(queryString);
					stmt.setInt(1,  userId);
					break;
			}
			
			theResult = stmt.executeQuery();
			
			while (theResult.next())	{
				Transaction tr = new Transaction();
				tr.setAmount(theResult.getDouble("amount"));
				tr.setBankAccount(theResult.getString("bankaccount"));
				tr.setUserId(theResult.getInt("userid"));
				tr.setFromTo(theResult.getInt("fromto"));
				tr.setCurrency(theResult.getString("currency"));
				tr.setFromTo_Factor(theResult.getDouble("fromto_factor"));
				tr.setFromTo_Currency(theResult.getString("fromto_currency"));
				tr.setTransactionType(theResult.getInt("transaction_type"));	// enum TransactionType.SEND // TOPUP // RECEIVE
				tr.setTransactionDateTime(theResult.getTimestamp("transaction_datetime")); // country code, 65-SG, 60-MY, 0-US, 851-CN
				tr.setRemarks(theResult.getString("remarks"));
				tr.setUserName(this.acctmanager.GetUserNameById(tr.getUserId()));
				tr.setFromToName(this.acctmanager.GetUserNameById(tr.getFromTo()));
				tr.setTransactionRef(theResult.getString("transaction_ref"));
				tr.setAmt_Home_Rate_Sender(theResult.getDouble("amt_home_rate_snd"));
				tr.setAmt_Home_Rate_Recipient(theResult.getDouble("amt_home_rate_rcp"));
				arrayList.add(tr);
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
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
		return arrayList;
	}
	
	
	/*
	 *  DEPRECIATED *
	 *  Add a transaction to the user, it can be a Send Money or Top-up money transaction
	 *  If a Send Money transaction was a fulfillment of a payment request, the payment request record (in table transactionR) will be updated
	 */
	// ******************************************************************************************************
	public Boolean AddTransaction(com.tfommtapi.models.AddTransaction transaction) throws SQLException	{
		Boolean theResult = true;
		Connection conn = ConnectionManager.Get();
		conn.setAutoCommit(false);
		PreparedStatement stmt = null;
		try {
			//System.out.println(transaction.toString());
			stmt = conn.prepareStatement(
					"INSERT INTO transactiont (transaction_type, userid, fromto, release_type, " + 
							"amount, currency, bankaccount, acct_type, fromto_bankaccount, fromto_acct_type, fromto_currency," +
							"fromto_factor, fromto_admin, fromto_variable, release_date, remarks, transaction_ref) " +
							" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			stmt.setInt(1,  transaction.getTransactionType());
			stmt.setInt(2,  transaction.getUserId());
			stmt.setInt(3,  transaction.getFromTo());
			stmt.setInt(4,  transaction.getReleaseType());
			stmt.setDouble(5,  transaction.getAmount());
			stmt.setString(6,  transaction.getCurrency());
			stmt.setString(7,  transaction.getBankAccount());
			stmt.setInt(8,  transaction.getAcct_type());
			stmt.setString(9,  transaction.getFromto_bank_account());
			stmt.setInt(10,  transaction.getFromto_acct_type());
			stmt.setString(11,  transaction.getFromTo_Currency());
			stmt.setDouble(12,  transaction.getFromTo_Factor());
			stmt.setDouble(13,  transaction.getFromTo_Admin_Fee());
			stmt.setDouble(14,  transaction.getFromTo_Variable_Fee());
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			Calendar cal = Calendar.getInstance();
			cal.setTime(ts);
			if (transaction.getReleaseType()>0) {
				// only need to set the time to 00:00:00:000 if the release type is not immediate
				cal.add(Calendar.DAY_OF_YEAR, transaction.getReleaseType());
			}
			cal.set(Calendar.HOUR_OF_DAY, 0);  
		    cal.set(Calendar.MINUTE, 0);  
		    cal.set(Calendar.SECOND, 0);  
		    cal.set(Calendar.MILLISECOND, 0);  
			stmt.setTimestamp(15,  new Timestamp(cal.getTimeInMillis()));
			stmt.setString(16,  transaction.getRemarks());
			
			// Transaction Ref: E{Transaction_Type}cyymdd-{8 RandomCharacters}
			// E.g. E101120203-RWE48FG1	
//			Calendar cal2 = Calendar.getInstance();
//			cal2.setTime(ts);
//			String ref_seg_cyy = String.valueOf(cal2.get(Calendar.YEAR)-1900);
//			int cal2Mth = cal2.get(Calendar.MONTH)+1;
// 			1 - 9 for Jan - Sep, A = Oct, B = Nov & C = Dec
//			String ref_seg_mdd = String.format("%s", cal2Mth==12? "C": cal2Mth==11? "B": cal2Mth==10? "A": cal2Mth)
//					.concat(String.format("%2d",  cal2.get(Calendar.DAY_OF_MONTH)));
//			String transRef = "E" + transaction.getTransactionType() + ref_seg_cyy + ref_seg_mdd + "-" + Utility.getRandomString(8);
			String transRef = Utility.genTransactionRef(transaction.getTransactionType());
			stmt.setString(17,  transRef);
			
			int insertRes = stmt.executeUpdate();
			if (insertRes == 1)	{
				if (transaction.getRequestId()!=0 && transaction.getFromTo()!=0)	{
					// the transaction is a send money to fulfill a payment request
					// set the release_stat to 1 (true)
					stmt = conn.prepareStatement("UPDATE transactionr SET release_stat=1 WHERE id = ? AND requestor = ? AND requestee = ? AND release_stat=0");
					stmt.setInt(1,  transaction.getRequestId());
					stmt.setInt(2,  transaction.getFromTo());
					stmt.setInt(3, transaction.getUserId());
					int updateRes = stmt.executeUpdate();
					if (updateRes == 1)	{
						conn.commit();
					} else {
						conn.rollback();
						theResult = false;
					}
				} else {
					conn.commit();
				}
			}
		} catch (SQLException ex)	{
			ex.printStackTrace();
			theResult = false;
		} finally {
			// close all resultset, statement and connection 
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return  theResult;
	}
	
	
	/*
	 *  Add a transaction to the user, it can be a Send Money or Top-up money transaction
	 *  If a Send Money transaction was a fulfillment of a payment request, the payment request record (in table transactionR) will be updated 
	 *  note: this is a re-write of the AddTransaction method in this class, as the free-tier of Amazon RDS do not support database trigger, many of the trigger automation 
	 *  need to be translate into Java codes here
	 */
	@SuppressWarnings("resource")
	public Boolean AddTransactionTriggerless(com.tfommtapi.models.AddTransaction transaction) throws SQLException	{
		
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.setTime(ts);
		if (transaction.getReleaseType()>0) {
			// only need to set the time to 00:00:00:000 if the release type is not immediate
			cal.add(Calendar.DAY_OF_YEAR, transaction.getReleaseType());
		}
		cal.set(Calendar.HOUR_OF_DAY, 0);  
	    cal.set(Calendar.MINUTE, 0);  
	    cal.set(Calendar.SECOND, 0);  
	    cal.set(Calendar.MILLISECOND, 0);  
		transaction.setRelease_date(new Timestamp(cal.getTimeInMillis()));
		// Transaction Ref: E{Transaction_Type}cyymdd-{8 RandomCharacters}
		// E.g. E101120203-RWE48FG1	
		transaction.setTransactionRef(Utility.genTransactionRef(transaction.getTransactionType()));
		
		Boolean theResult = true;
		Connection conn = ConnectionManager.Get();
		conn.setAutoCommit(false);
		PreparedStatement prepstmt = null;
		try {
			int updateStat_0 = 0, updateStat_1 = 0, updateStat_2 = 0, updateStat_3 = 0, updateStat_4 = 0;
			switch (transaction.getTransactionType())	{
				case TransactionType.TOPUP:
					// call the method to construct and return the PreparedStatement for Top-up money transaction 
					prepstmt = prepMoneyTopup(conn, transaction);
					updateStat_0 = prepstmt.executeUpdate();
					if (updateStat_0 == 1) {
						// update wallet balance
						// call the method to construct and return the PreparedStatement updating funding account balance after the top-up
						prepstmt = prepFundingTopup(conn, transaction);
						updateStat_1 = prepstmt.executeUpdate();
					} 
					if (updateStat_1 == 1) {
						conn.commit();
					} else {
						if (updateStat_0==1)	{
							conn.rollback();
						}
						theResult = false;
					}
					break;
				case TransactionType.SEND:
					// call the method to construct and return the PreparedStatement for Send money transaction for sending party 
					prepstmt = prepMoneySender(conn, transaction);
					
					updateStat_0 = prepstmt.executeUpdate();
					
					if (updateStat_0 == 1)	{
						// call the method to construct and return the PreparedStatement for Send money transaction for receiving party 
						prepstmt = prepMoneyReceiver(conn, transaction);
						updateStat_1 = prepstmt.executeUpdate();
					} 
					if (updateStat_1 == 1) {
						// call the method to construct and return the PreparedStatement for updating funding account after Send money transaction for sending party 
						prepstmt = prepFundingSender(conn, transaction);
						updateStat_2 = prepstmt.executeUpdate();
					} else {
						if (updateStat_0==1)	{
							conn.rollback();
							break;
						}
						theResult = false;
					}
					if (updateStat_2 == 1) {
						// call the method to construct and return the PreparedStatement for updating funding account after Send money transaction for receiving party 
						prepstmt = prepFundingReceiver(conn, transaction);
						updateStat_3 = prepstmt.executeUpdate();
					} else {
						if (updateStat_1==1)	{
							conn.rollback();
							break;
						}
						theResult = false;
					}
					if (updateStat_3 == 1) {
						// if the send money is to fulfill a payment request transaction, update the payment request table (transactionR) and set release_stat = 1
						if (transaction.getRequestId()!=0 && transaction.getFromTo()!=0)	{
							prepstmt = conn.prepareStatement("UPDATE transactionr SET release_stat=1, release_date = ? WHERE id = ? AND requestor = ? AND requestee = ? AND release_stat=0");
							prepstmt.setTimestamp(1,  new Timestamp(System.currentTimeMillis()));
							prepstmt.setInt(2,  transaction.getRequestId());
							prepstmt.setInt(3,  transaction.getFromTo());
							prepstmt.setInt(4, transaction.getUserId());
							updateStat_4 = prepstmt.executeUpdate();
							if (updateStat_4 == 1)	{
								// commit only if everything completed successfully
								conn.commit();
							} else {
								// otherwise rollback
								conn.rollback();
								theResult = false;
							}
						} else {
							// otherwise commit the transactions
							conn.commit();
						}
					} else {
						if (updateStat_2==1)	{
							// rollback if any is failed
							conn.rollback();
							break;
						}
						theResult = false;
					}
					break;
			}	
			
		} catch (SQLException ex)	{
			ex.printStackTrace();
			theResult = false;
		} finally {
			// close all resultset, statement and connection 
			if (prepstmt!=null && !prepstmt.isClosed())	{
				prepstmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return  theResult;
	}
	
	
	/* 
	 *  PreparedStatement for Send Money transaction for the Sender 
	 */
	private PreparedStatement prepMoneySender(Connection conn, com.tfommtapi.models.AddTransaction transaction) throws SQLException	{
		
		PreparedStatement stmt = null;
		try {
			//System.out.println(transaction.toString());
			stmt = conn.prepareStatement(
					"INSERT INTO transactions (transaction_type, userid, fromto, release_type, " + 
							"amount, currency, bankaccount, acct_type, fromto_bankaccount, fromto_acct_type," +
							" fromto_currency, fromto_factor, fromto_admin, fromto_variable, release_date, remarks, " +
							"transaction_ref, amt_home_rate_snd, amt_home_rate_rcp) " +
							" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			stmt.setInt(1,  transaction.getTransactionType());
			stmt.setInt(2,  transaction.getUserId());
			stmt.setInt(3,  transaction.getFromTo());
			stmt.setInt(4,  transaction.getReleaseType());
			stmt.setDouble(5,  transaction.getAmount()*-1);
			stmt.setString(6,  transaction.getCurrency());
			stmt.setString(7,  transaction.getBankAccount());
			stmt.setInt(8,  transaction.getAcct_type());
			stmt.setString(9,  transaction.getFromto_bank_account());
			stmt.setInt(10,  transaction.getFromto_acct_type());
			stmt.setString(11,  transaction.getFromTo_Currency());
			stmt.setDouble(12,  transaction.getFromTo_Factor());
			stmt.setDouble(13,  transaction.getFromTo_Admin_Fee()*-1);
			stmt.setDouble(14,  transaction.getFromTo_Variable_Fee()*-1);
			stmt.setTimestamp(15,  transaction.getRelease_date());
			stmt.setString(16,  transaction.getRemarks());
			stmt.setString(17,  transaction.getTransactionRef());
			stmt.setDouble(18,  transaction.getAmt_Home_Rate_Sender());
			stmt.setDouble(19,  transaction.getAmt_Home_Rate_Recipient());
			
		} catch (SQLException ex)	{
			ex.printStackTrace();
		} 
		return stmt;
	}
	
	
	/* 
	 *  PreparedStatement for Send Money transaction for the Sender 
	 */
	private PreparedStatement prepMoneyReceiver(Connection conn, com.tfommtapi.models.AddTransaction transaction) throws SQLException	{
		
		PreparedStatement stmt = null;
		try {
			//System.out.println(transaction.toString());
			stmt = conn.prepareStatement(
					"INSERT INTO transactions (transaction_type, userid, fromto, release_type, " + 
							"amount, currency, bankaccount, acct_type, fromto_bankaccount, fromto_acct_type, fromto_currency," +
							"fromto_factor, fromto_admin, fromto_variable, release_date, remarks, transaction_ref, amt_home_rate_snd, amt_home_rate_rcp) " +
							" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			stmt.setInt(1,  TransactionType.RECEIVE);
			stmt.setInt(2,  transaction.getFromTo());
			stmt.setInt(3,  transaction.getUserId());
			stmt.setInt(4,  transaction.getReleaseType());
			stmt.setDouble(5,  transaction.getAmount()*transaction.getFromTo_Factor());
			stmt.setString(6,  transaction.getFromTo_Currency());
			stmt.setString(7,  transaction.getFromto_bank_account());
			stmt.setInt(8,  transaction.getFromto_acct_type());
			stmt.setString(9,  transaction.getBankAccount());
			stmt.setInt(10,  transaction.getAcct_type());
			stmt.setString(11,  transaction.getCurrency());
			stmt.setDouble(12,  transaction.getFromTo_Factor());
			stmt.setDouble(13,  0.00);
			stmt.setDouble(14,  0.00);
			stmt.setTimestamp(15,  transaction.getRelease_date());
			stmt.setString(16,  transaction.getRemarks());
			stmt.setString(17,  transaction.getTransactionRef());
			stmt.setDouble(18,  transaction.getAmt_Home_Rate_Recipient());
			stmt.setDouble(19,  transaction.getAmt_Home_Rate_Sender());
		} catch (SQLException ex)	{
			ex.printStackTrace();
		} 
		return stmt;
	}
	
	
	/* 
	 *  PreparedStatement for Top-up Money transaction for the user 
	 */
	private PreparedStatement prepMoneyTopup(Connection conn, com.tfommtapi.models.AddTransaction transaction) throws SQLException	{
		
		PreparedStatement stmt = null;
		try {
			//System.out.println(transaction.toString());
			stmt = conn.prepareStatement(
					"INSERT INTO transactions (userid, amount, currency, transaction_type, bankaccount, " +
							"acct_type, fromto_acct_type, fromto_bankaccount, fromto_currency, fromto_admin, " +
							"fromto_variable, fromto_factor, release_type, release_date, remarks, transaction_ref, amt_home_rate_snd, amt_home_rate_rcp) " +
							" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)");
			
			stmt.setInt(1,  transaction.getUserId());
			stmt.setDouble(2,  transaction.getAmount());
			stmt.setString(3,  transaction.getCurrency());
			stmt.setInt(4,  transaction.getTransactionType());
			stmt.setString(5,  transaction.getBankAccount());
			stmt.setInt(6,  transaction.getAcct_type());
			stmt.setInt(7,  transaction.getFromto_acct_type());
			stmt.setString(8,  transaction.getFromto_bank_account());
			stmt.setString(9,  transaction.getFromTo_Currency());
			stmt.setDouble(10,  transaction.getFromTo_Admin_Fee()*-1);
			stmt.setDouble(11,  transaction.getFromTo_Variable_Fee()*-1);
			stmt.setDouble(12,  transaction.getFromTo_Factor());
			stmt.setDouble(13,  transaction.getReleaseType());
			stmt.setTimestamp(14,  transaction.getRelease_date());
			stmt.setString(15,  transaction.getRemarks());
			stmt.setString(16, transaction.getTransactionRef());
			//stmt.setString(17, transaction.getTransactionRef());
			stmt.setDouble(17,  transaction.getAmt_Home_Rate_Sender());
			stmt.setDouble(18,  transaction.getAmt_Home_Rate_Recipient());
		} catch (SQLException ex)	{
			ex.printStackTrace();
		} 
		return stmt;
	}
	
	
	/* 
	 *  PreparedStatement for updating user's funding account for a Top-up transaction.
	 */
	private PreparedStatement prepFundingTopup(Connection conn, com.tfommtapi.models.AddTransaction transaction) throws SQLException	{
		
		PreparedStatement stmt = null;
		try {
			//System.out.println(transaction.toString());
			stmt = conn.prepareStatement(
					"UPDATE fundingacct SET acct_balc = acct_balc + ? WHERE uid = ? AND acct_type=1");
			
			stmt.setDouble(1,  transaction.getAmount());
			stmt.setInt(2,  transaction.getUserId());
			
		} catch (SQLException ex)	{
			ex.printStackTrace();
		} 
		return stmt;
	}
	
	
	/* 
	 *  PreparedStatement for updating sender's funding account for a Send Money transaction.
	 */
	private PreparedStatement prepFundingSender(Connection conn, com.tfommtapi.models.AddTransaction transaction) throws SQLException	{
		
		PreparedStatement stmt = null;
		try {
			//System.out.println(transaction.toString());
			stmt = conn.prepareStatement(
					"UPDATE fundingacct SET acct_balc = acct_balc - ? WHERE uid = ? AND acct_type=1");
			
			stmt.setDouble(1,  (transaction.getAmount()+transaction.getFromTo_Variable_Fee()+transaction.getFromTo_Admin_Fee()));
			stmt.setInt(2,  transaction.getUserId());
			
		} catch (SQLException ex)	{
			ex.printStackTrace();
		} 
		return stmt;
	}
	
	
	/* 
	 *  PreparedStatement for updating receiver's funding account for a Send Money transaction.
	 */
	private PreparedStatement prepFundingReceiver(Connection conn, com.tfommtapi.models.AddTransaction transaction) throws SQLException	{
		
		PreparedStatement stmt = null;
		try {
			//System.out.println(transaction.toString());
			stmt = conn.prepareStatement(
					"UPDATE fundingacct SET acct_balc = acct_balc + ? WHERE uid = ? AND acct_type=1");
			
			//stmt.setDouble(1,  (transaction.getAmount()*transaction.getFromTo_Factor()));
			stmt.setDouble(1,  (transaction.getAmount()*transaction.getFromTo_Factor())*transaction.getAmt_Home_Rate_Recipient());
			stmt.setInt(2,  transaction.getFromTo());
			
		} catch (SQLException ex)	{
			ex.printStackTrace();
		} 
		return stmt;
	}

	
	
	
	
	/* 
	 *  Retrieve Dashboard Summary of a given user, this includes the total money top-up, sent and received 
	 *  for the given user, and also the EzyWallet account balance, and any pending payment request associate to the user
	 */
	// ******************************************************************************************************
	@SuppressWarnings("resource")
	public DashboardSummary GetDashboardSummaryById(int userId) throws SQLException {
		// TODO Auto-generated method stub
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet theResult = null;
		try {
			DashboardSummary dash = new DashboardSummary();
			
			stmt = conn.prepareStatement(
					"SELECT SUM(case when transaction_type = 101 then amount else 0 end) as topupamount," +
							"SUM(case when transaction_type = 102 then amount else 0 end) as sentamount," +
							"SUM(case when transaction_type = 103 then amount else 0 end) as recvamount " +
							"FROM transactions WHERE userid = ? ");
			stmt.setInt(1,  userId);
			theResult = stmt.executeQuery();
			if (theResult.next())	{
				
				dash.setTotalTopupAmount(theResult.getDouble("topupamount"));
				dash.setTotalSentAmount(theResult.getDouble("sentamount"));
				dash.setTotalReceivedAmount(theResult.getDouble("recvamount"));
				
				theResult.close();
				
//				stmt = conn.prepareStatement("SELECT fa.acct_numbr, fa.acct_currency, sum(fa.acct_balc) as acct_balc, SUM(" +
//						"case when tr.transaction_type = 101 then tr.amount  " +
//								 "when tr.transaction_type = 102 AND tr.acct_type=1 then (tr.amount+tr.fromto_variable+tr.fromto_admin)*-1 " +
//						         "when tr.transaction_type = 103 AND tr.acct_type=1 then tr.amount else 0 end) as balance " +
//						"FROM fundingacct fa join transactions tr on (tr.userid=fa.uid) WHERE fa.uid = ?  AND fa.acct_type=1 " +
//						         "GROUP BY fa.acct_numbr, fa.acct_currency");
				
				// retrieve the EzyWallet balance
				stmt = conn.prepareStatement("SELECT acct_numbr, acct_currency, acct_balc " +
						"FROM fundingacct WHERE uid = ?  AND acct_type=1 " );
				
				
				stmt.setInt(1, userId);
				theResult = stmt.executeQuery();
				if (theResult.next())	{
					dash.setAcctBalance(theResult.getDouble("acct_balc"));
					dash.setAcctNumbr(theResult.getString("acct_numbr"));
					dash.setAcctCurrency(theResult.getString("acct_currency"));
				} else {
					dash.setAcctBalance(0.00);
					dash.setAcctNumbr("");
					dash.setAcctCurrency("");
				}
				theResult.close();
				
				// Retrieve the number of payment request associate to the user, if any. No detail of the request is return.
				stmt = conn.prepareStatement("SELECT count(*) as reqcount FROM transactionr WHERE (requestor = ?  OR requestee = ?) AND release_stat=0  " );
				stmt.setInt(1, userId);
				stmt.setInt(2,  userId);
				theResult = stmt.executeQuery();
				if (theResult.next())	{
					dash.setTotalPaymentRequest(theResult.getInt("reqcount"));
				} else {
					dash.setTotalPaymentRequest(0);
				}
				theResult.close();
				
			}
			stmt.close();
			conn.close();
			
			return dash;
		} catch (SQLException ex)	{
			ex.printStackTrace();
		} finally {
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
	 *  Add a payment request for the user.
	 */
	public Boolean AddPaymentRequest(RequestPayment request) throws SQLException {
		Boolean theResult = true;
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		try {
			//System.out.println(transaction.toString());
			stmt = conn.prepareStatement(
					"INSERT INTO transactionr (amount, requestor, requestee, requestee_name, release_stat, " + 
							"currency, transaction_type, reason, transaction_ref) " +
							" VALUES (?, ?, ?,  ?, ?, ?, ?, ?, ?)");
			
			stmt.setDouble(1,  request.getRequestAmount());
			stmt.setInt(2,  request.getRequestor());
			stmt.setInt(3,  request.getRequestee());
			stmt.setString(4,  request.getRequesteeName());
			stmt.setInt(5,  0);
			stmt.setString(6,  request.getRequestCurrency());
			stmt.setInt(7,  TransactionType.REQUEST);
			stmt.setString(8,  request.getRequestReason());
			stmt.setString(9,  Utility.genTransactionRef(TransactionType.REQUEST));
			stmt.executeUpdate();
		} catch (SQLException ex)	{
			ex.printStackTrace();
			theResult = false;
		} finally {
			// close all resultset, statement and connection 
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return  theResult;
	}

	
	/* 
	 *  Retrieve a list of the transaction associate to the user (with pagination). These can be a Top-up money, Send money or
	 *  Receive money. 
	 */
	public ArrayList<Transaction> GetTransactionsById(int userId, int typeId, int pageSize, int pageIndex) throws SQLException {
		ArrayList<Transaction> arrayList = new ArrayList<Transaction>();
		Connection conn  = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet theResult = null ;
		try	{
			String queryString = "SELECT * FROM transactions " ;
			//Timestamp ts = new Timestamp(System.currentTimeMillis());
			int offset = pageIndex * pageSize;
			switch (typeId) {
				case TransactionType.TOPUP :
					queryString += "WHERE transaction_type = ? AND userid = ?  AND release_date <= CURRENT_TIMESTAMP LIMIT ? OFFSET ?" ;
					stmt = conn.prepareStatement(queryString);
					stmt.setInt(1,  TransactionType.TOPUP);
					stmt.setInt(2,  userId);
					stmt.setInt(3,  pageSize);
					stmt.setInt(4, offset);
					break;
				case TransactionType.SEND :
					queryString += "WHERE transaction_type = ? AND userid = ?  AND release_date <= CURRENT_TIMESTAMP LIMIT ? OFFSET ?" ;
					stmt = conn.prepareStatement(queryString);
					stmt.setInt(1,  TransactionType.SEND);
					stmt.setInt(2,  userId);
					stmt.setInt(3,  pageSize);
					stmt.setInt(4, offset);
					break;
				case TransactionType.RECEIVE :
					queryString += "WHERE transaction_type = ? AND userid = ?   AND release_date <=CURRENT_TIMESTAMP LIMIT ? OFFSET ?" ;
					stmt = conn.prepareStatement(queryString);
					stmt.setInt(1,  TransactionType.RECEIVE);
					stmt.setInt(2,  userId);
					stmt.setInt(3,  pageSize);
					stmt.setInt(4, offset);
					break;
				default :
					queryString += "WHERE userid = ? AND release_date <= CURRENT_TIMESTAMP LIMIT ? OFFSET ?" ;
					stmt = conn.prepareStatement(queryString);
					stmt.setInt(1,  userId);
					stmt.setInt(2,  pageSize);
					stmt.setInt(3, offset);
					break;
			}
			
			theResult = stmt.executeQuery();
			
			while (theResult.next())	{
				Transaction tr = new Transaction();
				tr.setAmount(theResult.getDouble("amount"));
				tr.setBankAccount(theResult.getString("bankaccount"));
				tr.setUserId(theResult.getInt("userid"));
				tr.setFromTo(theResult.getInt("fromto"));
				tr.setCurrency(theResult.getString("currency"));
				tr.setFromTo_Factor(theResult.getDouble("fromto_factor"));
				tr.setFromTo_Currency(theResult.getString("fromto_currency"));
				tr.setTransactionType(theResult.getInt("transaction_type"));	// enum TransactionType.SEND // TOPUP // RECEIVE
				tr.setTransactionDateTime(theResult.getTimestamp("transaction_datetime")); 
				tr.setTransactionRef(theResult.getString("transaction_ref"));
				tr.setRemarks(theResult.getString("remarks"));
				tr.setUserName(this.acctmanager.GetUserNameById(tr.getUserId()));
				tr.setFromToName(this.acctmanager.GetUserNameById(tr.getFromTo()));
				tr.setAmt_Home_Rate_Sender(theResult.getDouble("amt_home_rate_snd"));
				tr.setAmt_Home_Rate_Recipient(theResult.getDouble("amt_home_rate_rcp"));
				
				arrayList.add(tr);
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
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
		return arrayList;
	}

	
	/* 
	 *  Return a total transaction count user for the given transaction type
	 *  this is for setting/formatting the next / prev href for the pagination 
	 */
	public int GetTransactionsCountById(int userId, int typeId) throws SQLException {
		
		int transactionsCount = 0;
		Connection conn  = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet theResult = null ;
		try	{
			String queryString = "SELECT COUNT(*) As TransCount FROM transactions " ;
			//Timestamp ts = new Timestamp(System.currentTimeMillis());
			switch (typeId) {
				case TransactionType.TOPUP :
					queryString += "WHERE transaction_type = ? AND userid = ?  AND release_date <= CURRENT_TIMESTAMP" ;
					stmt = conn.prepareStatement(queryString);
					stmt.setInt(1,  TransactionType.TOPUP);
					stmt.setInt(2,  userId);
					break;
				case TransactionType.SEND :
					queryString += "WHERE transaction_type = ? AND userid = ?  AND release_date <= CURRENT_TIMESTAMP" ;
					stmt = conn.prepareStatement(queryString);
					stmt.setInt(1,  TransactionType.SEND);
					stmt.setInt(2,  userId);
					break;
				case TransactionType.RECEIVE :
					queryString += "WHERE transaction_type = ? AND userid = ?   AND release_date <=CURRENT_TIMESTAMP" ;
					stmt = conn.prepareStatement(queryString);
					stmt.setInt(1,  TransactionType.RECEIVE);
					stmt.setInt(2,  userId);
					break;
				default :
					queryString += "WHERE userid = ? AND release_date <= CURRENT_TIMESTAMP" ;
					stmt = conn.prepareStatement(queryString);
					stmt.setInt(1,  userId);
					break;
			}
			
			theResult = stmt.executeQuery();
			if (theResult.next())	{
				transactionsCount = theResult.getInt("transCount");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
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
		return transactionsCount;
	}

	
	/* 
	 *  Retrieve a list of payment request associate to the given user, only unfulfilled request (release_stat=0) will be returned
	 */
	public ArrayList<RequestPayment> GetRequestPaymentByUser(int userId) throws SQLException {
		ArrayList<RequestPayment> arrayList = new ArrayList<RequestPayment>();
		Connection conn  = ConnectionManager.Get();
		PreparedStatement stmt = null;
		ResultSet theResult = null ;
		try	{
			String queryString = "SELECT tr.*, " + 
				"ur.username as requestor_uname, ur.profilepix as requestor_pfpix, " +
				"case when tr.requestor = ? then 'REQUESTOR' else 'REQUESTEE' end as role " +	
					" FROM transactionr tr LEFT JOIN users ur ON (tr.requestor = ur.id) " +
					" WHERE (tr.requestor = ? OR tr.requestee = ?)  AND tr.release_stat = 0 " + 
					" ORDER BY role, transaction_datetime ";
			stmt = conn.prepareStatement(queryString);
			stmt.setInt(1,  userId);
			stmt.setInt(2,  userId);
			stmt.setInt(3,  userId);
			
			theResult = stmt.executeQuery();
			
			while (theResult.next())	{
				RequestPayment tr = new RequestPayment();
				tr.setRequestId(theResult.getInt("id"));
				tr.setRequestAmount(theResult.getDouble("amount"));
				tr.setRequestCurrency(theResult.getString("currency"));
				tr.setRequestor(theResult.getInt("requestor"));
				tr.setRequestUsername(theResult.getString("requestor_uname"));
				tr.setRequestProfilePix(theResult.getString("requestor_pfpix"));
				tr.setRequestee(theResult.getInt("requestee"));
				tr.setRequesteeName(theResult.getString("requestee_name"));
				tr.setRequestReason(theResult.getString("reason"));
				tr.setUserRole(theResult.getString("role"));
				tr.setRequestDateTime(theResult.getTimestamp("transaction_datetime"));
				
				arrayList.add(tr);
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
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
		return arrayList;
	}

	/* 
	 * Delete the payment request record only if it is still open, completed/expired request cannot be deleted
	 */
	public Boolean DeletePaymentRequest(int userId, int reqId) throws SQLException {
		Boolean theResult = true;
		Connection conn = ConnectionManager.Get();
		PreparedStatement stmt = null;
		try {
			//System.out.println(transaction.toString());
			stmt = conn.prepareStatement(
					"DELETE FROM transactionr WHERE requestor = ? AND id = ? AND release_stat=0");
			
			stmt.setInt(1,  userId);
			stmt.setInt(2,  reqId);
			
			int recDeleted = stmt.executeUpdate();
			theResult = (recDeleted==1);
		} catch (SQLException ex)	{
			ex.printStackTrace();
			theResult = false;
		} finally {
			// close all resultset, statement and connection 
			if (stmt!=null && !stmt.isClosed())	{
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return  theResult;
	}
}
