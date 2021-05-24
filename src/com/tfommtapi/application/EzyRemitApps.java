package com.tfommtapi.application;

import org.glassfish.jersey.server.ResourceConfig;


public class EzyRemitApps extends ResourceConfig {
	
	public EzyRemitApps()	{
		packages("com.tfommtapi.resources");
		packages("com.tfommtapi.resources.v2");
		register(com.tfommtapi.application.MyJacksonFeature.class);
		
		//register(com.sp.marketplaceapi.auth.BasicAuthentication.class);
		//register(com.tfommtapi.auth.jwt.BearerAuthentication.class);
	}
}




