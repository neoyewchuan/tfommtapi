package com.tfommtapi.util;

import com.tfommtapi.auth.jwt.JWTToken;

import io.jsonwebtoken.Claims;


public class Util {
	
	public void Test()	{
		
	}
	
	public static int GetUserIdFromAuthToken(String authToken) {
		authToken = authToken.replace("Bearer ", "");
		Claims claims = JWTToken.getClaims(authToken);
		String payload = claims.get("payload").toString();
		int userId = Integer.parseInt(payload);
		return userId;
	}
	
	/*public static JSONObject quickParse(Object obj) throws IllegalArgumentException, IllegalAccessException, JSONException{
	    JSONObject object = new JSONObject();

	    Class<?> objClass = obj.getClass();
	    Field[] fields = objClass.getDeclaredFields();
	    for(Field field : fields) {
	        field.setAccessible(true);
	        Annotation[] annotations = field.getDeclaredAnnotations();
	        for(Annotation annotation : annotations){
	            if(annotation instanceof SerializedName){
	               SerializedName myAnnotation = (SerializedName) annotation;
	               String name = myAnnotation.value();
	               Object value = field.get(obj);

	               if(value == null)
	                  value = new String("");

	               object.put(name, value);
	            }
	        }
	    }   

	    return object;
	}
	*/
}
