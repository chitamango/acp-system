package com.acp.listeners;
/*
 *  @version 1.1
 */

import java.util.EventListener;

import com.acp.controllers.AcpProcessController;
import com.acp.controllers.MonitorObject;
import com.acp.controllers.SharedArtifactClientManager;
import com.acp.events.CombineEvent;
import com.acp.events.MessageEvent;
import com.acp.instance.ProcessInstance;
import com.acp.rule.AcpRuleImpl;



public interface MessageEventListener extends EventListener {
	
	
	void invokeRuleEngine(MessageEvent evt);
	
	
	void invokeRuleEngine(CombineEvent evt);
	
	void setRuleEngine(AcpRuleImpl RuleEngine);
	
	void setProcessController(AcpProcessController Pcontroller);
	

	

}
