package com.acp.listeners;
/*
 *  @version 1.1
 */

import java.util.EventListener;

import com.acp.events.RequestEvent;

public interface RequestEventListener extends EventListener {
	
	void invokeRuleEngine(RequestEvent evt);
	

}
