package com.acp.events;
/*
 *  @version 1.1
 */

import java.util.EventObject;

import javax.servlet.http.HttpServletRequest;

public class RequestEvent extends EventObject {

protected HttpServletRequest requestEvent; 
	
	public RequestEvent(Object Source,HttpServletRequest request) {
		super(Source);
		// TODO Auto-generated constructor stub
		this.requestEvent = request;
		
	}
	
	public HttpServletRequest getRequest()
	{
		
		return this.requestEvent;
			
		
	}

}
