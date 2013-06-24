package com.acp.listeners;

import java.util.EventListener;

import com.acp.events.RequestEvent;

public interface RequestEventListener extends EventListener {
	
	void invokeRuleEngine(RequestEvent evt);
	

}
