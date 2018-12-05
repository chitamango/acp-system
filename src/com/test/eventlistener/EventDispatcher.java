package com.test.eventlistener;
/*
 *  @version 1.1
 */

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.swing.event.EventListenerList;

import org.apache.axiom.om.OMElement;

import com.acp.controllers.AcpProcessController;
import com.acp.events.CombineEvent;
import com.acp.events.MessageEvent;
import com.acp.events.RequestEvent;
import com.acp.instance.ProcessInstance;
import com.acp.listeners.MessageEventListener;
import com.acp.listeners.RequestEventListener;

public class EventDispatcher {
	
	protected EventListenerList listenerList = new EventListenerList();
	protected AcpProcessController ProcessController;  
	protected String ProcessId;
	
	
	/*
	 * 
	 * function to add eventlistener (request & message event)
	 * 
	 */
	public void addEventListener(RequestEventListener listener) {
	    listenerList.add(RequestEventListener.class, listener);
	   
	    
	  }
	
	public void addEventListener(MessageEventListener listener) {
	    listenerList.add(MessageEventListener.class, listener);
	   
	    
	  }
	
	
	/*
	 * 
	 * function to remove eventlistener (request & message event)
	 * 
	 * 
	 */
	  public void removeEventListener(RequestEventListener listener) {
	    listenerList.remove(RequestEventListener.class, listener);
	  }
	  
	  public void removeEventListener(MessageEventListener listener) {
		    listenerList.remove(MessageEventListener.class, listener);
		  }
	  
	  /*
	   * 
	   * private function to fire an event
	   * 
	   */
	  private void fireCombineEvent(CombineEvent evt) {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) {
	      if (listeners[i] == MessageEventListener.class) {
	        ((MessageEventListener) listeners[i+1]).invokeRuleEngine(evt);
	      }
	    }
	
	  }
	  
	  private void fireMessageEvent(MessageEvent evt) {
		    Object[] listeners = listenerList.getListenerList();
		    for (int i = 0; i < listeners.length; i = i+2) {
		      if (listeners[i] == MessageEventListener.class) {
		        ((MessageEventListener) listeners[i+1]).invokeRuleEngine(evt);
		      }
		    }
		
		  }
	  
	  /*
	   * 
	   *  function to capture an event 
	   * 
	   * 
	   */
	/* 
	  public void requestReceive(Object Source, HttpServletRequest request)
	  {
		  RequestEvent event = new RequestEvent(Source, request);
		  
		  this.fireRequestEvent(event);
		  
		  
		  
		  
		  
	  }
	  
	 */ 
	
	  /**
	   * 
	   * receive an object event and determine event type and fire event
	   * 
	   * 
	   */
	  public void eventReceived(Object Source,Object eventdata)
	  {
		
				  
		  if(eventdata.getClass().getInterfaces()[0].equals(HttpServletRequest.class))
		  {
			
			  HttpServletRequest httpRequest = (HttpServletRequest)eventdata;
			  
			  RequestEvent requestevent = new RequestEvent(Source, httpRequest);
			  
			//  this.fireRequestEvent(requestevent);
			  
			  
			  
		  }
		  else if(eventdata.getClass().getInterfaces()[0].equals(OMElement.class))
		  {
			  
			 
			  
			  OMElement MessageData = (OMElement)eventdata;
			 
			  if(this.checkProcessInstance(MessageData) == null)
			  {	  
			  
				  MessageEvent messageEvent = new MessageEvent(Source, MessageData);
				  
				  this.fireMessageEvent(messageEvent);
			  
			  }
			  else 
			  {
				  
				  ProcessInstance TargetProcessInstance = this.checkProcessInstance(MessageData);
				  
				  CombineEvent comEvent = new CombineEvent(Source, MessageData, TargetProcessInstance);
				  
				  this.fireCombineEvent(comEvent);
				  
			  }
			  
			  
		  }
		  else
		  {
			  
			  System.out.println("Message Event could pass if-condition");
			  
		  }
		  
	
		  
		 	  
		  
	  }
	
	public void setProcessController(AcpProcessController ProcessController)
	{
		this.ProcessController = ProcessController;
		
		
	}
	
	public AcpProcessController getProcessController()
	{
		return this.ProcessController;
		
		
	}
	
	  public synchronized void setProcessId(String ProcessId)
	{
		
		this.ProcessId = ProcessId;
		
	}
	
	public String getProcessId()
	{
		
		 return this.ProcessId;
		
	}
	
	private ProcessInstance checkProcessInstance(OMElement MessageData)
	{
		ProcessInstance targetProcessInstance = null; 
		
		Iterator<OMElement> messageIterator = MessageData.getChildElements();
		
		while(messageIterator.hasNext())
		{
			OMElement MessageElement = messageIterator.next();
			
			String MessageParameterdata = MessageElement.getText();
			
			if(this.ProcessController.getProcessInstance(MessageParameterdata) != null)
			{
				
				targetProcessInstance = this.ProcessController.getProcessInstance(MessageParameterdata);
				
			}
			
			
			
		}
		
		
		
		
		
		return targetProcessInstance;
	}
	
	

}
