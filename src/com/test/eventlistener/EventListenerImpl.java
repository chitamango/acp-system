package com.test.eventlistener;
/*
 *  @version 1.1
 */

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.axiom.om.OMElement;

import com.acp.controllers.AcpProcessController;
import com.acp.controllers.MonitorObject;
import com.acp.controllers.SharedArtifactClientManager;
import com.acp.events.ArtifactChangeEvent;
import com.acp.events.CombineEvent;
import com.acp.events.MessageEvent;
import com.acp.events.RequestEvent;
import com.acp.instance.ProcessInstance;
import com.acp.listeners.ArtifactChangeEventListener;
import com.acp.listeners.MessageEventListener;
import com.acp.listeners.RequestEventListener;
import com.acp.rule.AcpRuleImpl;

public class EventListenerImpl implements ArtifactChangeEventListener, MessageEventListener {

	
	
	private AcpRuleImpl RuleEngine ;
	private AcpProcessController Pcontroller;

	
	
	
	
	
	
	public EventListenerImpl()
	{
		/*this.RuleEngine = new AcpRuleImpl();
		this.Pcontroller = new AcpProcessController();
		this.ClientManager = new SharedArtifactClientManager();
		this.monitor = new MonitorObject();
		
		this.Pcontroller.addEventListener(this); //dont forger we add reference to a single listener here
		this.Pcontroller.setClientManager(ClientManager);
		this.Pcontroller.setMonitor(monitor);
		
		this.ClientManager.setRuleEngine(RuleEngine);
		this.ClientManager.setMonitor(monitor);
		this.ClientManager.setProcessController(Pcontroller);
		this.ClientManager.setMapper(Pcontroller.getMapper());
		*/
		
	}
	/*	
	@Override
	public void invokeRuleEngine(RequestEvent evt) {
		// TODO Auto-generated method stub
		 	
		HttpServletRequest request = evt.getRequest();
	       RuleEngine.executeRules(request,this.Pcontroller); 
			
	}

*/	
	@Override
	public void invokeRuleEngine(ArtifactChangeEvent evt) {
		// TODO Auto-generated method stub
		
		//ProcessInstance Process = evt.getProcessInstance();
		RuleEngine.executeRules(evt, this.Pcontroller);
		
		
	}
	@Override //I just worked to this part
	public void invokeRuleEngine(MessageEvent evt) {
		// TODO Auto-generated method stub
		
		//OMElement Message = evt.getMessage();
		
		//System.out.println("message event is here at event listener");
		
		RuleEngine.executeRules(evt, this.Pcontroller);
		
		
	}

	@Override
	public void invokeRuleEngine(CombineEvent evt) {
		// TODO Auto-generated method stub
		
	//	OMElement Message = evt.getMessage();
		
	//	System.out.println("message event is here at event listener");
		
		RuleEngine.executeRules(evt, this.Pcontroller);
		
		
		
		
	}

	@Override
	public void setRuleEngine(AcpRuleImpl RuleEngine) {
		// TODO Auto-generated method stub
		
		this.RuleEngine = RuleEngine;
		
		
		
	}

	@Override
	public void setProcessController(AcpProcessController Pcontroller) {
		// TODO Auto-generated method stub
		this.Pcontroller = Pcontroller;
		this.Pcontroller.addEventListener(this);
		
		
		
	}
	


	
	
	

}
