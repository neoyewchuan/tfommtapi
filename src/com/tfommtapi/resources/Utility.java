package com.tfommtapi.resources;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;

import com.tfommtapi.auth.jwt.JWTToken;

import io.jsonwebtoken.Claims;

public class Utility {
	
	private static final String ALLOWED_CHARACTERS = "0123456789QWERTYUIOPASDFGHJKLZXCVBNM";
	
	/* 
	 *  Retrieve the userid from JWToken
	 */
	public static int GetUserIdFromAuthToken(String authToken) {
		authToken = authToken.replace("Bearer ", "");
		Claims claims = JWTToken.getClaims(authToken);
		String payload = claims.get("payload").toString();
		int userId = Integer.parseInt(payload);
		return userId;
	}
	
	/* 
	 *  Retrieve the Token expiry time from the JWToken
	 */
	public static long GetTokenExpiryFromAuthToken(String authToken) {
		authToken = authToken.replace("Bearer ", "");
		Claims claims = JWTToken.getClaims(authToken);
		Timestamp expiry = new Timestamp(claims.getExpiration().getTime());
		
		return expiry.getTime();
	}
	
	/* 
	 *  Generate a random string of character 
	 */
	private static String getRandomString(final int sizeOfRandomString) {
		final Random random=new Random();
		final StringBuilder sb=new StringBuilder(sizeOfRandomString);
		for (int i = 0; i<sizeOfRandomString; i++) {
			sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
		}
		return sb.toString();
	}
	
	/*
	 *  Generate an unique transaction reference for the transaction
	 */
	public static String genTransactionRef(int transType)	{
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.setTime(ts);
		String ref_seg_cyy = String.valueOf(cal.get(Calendar.YEAR)-1900);
		int cal2Mth = cal.get(Calendar.MONTH)+1;
		String ref_seg_mdd = String.format("%s", cal2Mth==12? "C": cal2Mth==11? "B": cal2Mth==10? "A": cal2Mth)
				.concat(String.format("%2d",  cal.get(Calendar.DAY_OF_MONTH)));
		return  "E" + transType + ref_seg_cyy + ref_seg_mdd + "-" + getRandomString(8);

	}
	
	
}
