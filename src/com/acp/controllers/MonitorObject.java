package com.acp.controllers;
/*
 *  @version 1.1
 */

import com.acp.instance.ProcessInstance;
import com.acp.sharingrule.RuleType;

public class MonitorObject {

	public boolean  isSharingRuleSatisfied = false;
	public RuleType MatchedRule;
	public ProcessInstance Pinstance;
	
	public void doWait() throws InterruptedException{
		  
	    synchronized(this){
	     
	        
	        	
	        	
	        	
				this.wait();
			
	      
	        
	    } 
	  }

	  public void doNotify(){
		  
	    synchronized(this){
	    	
	    this.isSharingRuleSatisfied  = false;
	  
	      this.notify();
	    }
	  
	  }
	
	
	  synchronized public Boolean isRuleSatisfied()
	  {
		  
		  return this.isSharingRuleSatisfied;
		  
		  
	  }
	
	
	public ProcessInstance getProcessInstane()
	{
		
		
		return this.Pinstance;
	}
	
	public void setProcessInstance(ProcessInstance processInstance)
	{
		this.Pinstance = processInstance;
		
	}
	
	public RuleType getRule()
	{
		
		
		return this.MatchedRule;
	}
	
	public void setRule(RuleType Rule)
	{
		this.MatchedRule = Rule;
		
	}
	
	public void setRuleSatisfiedStatusTrue()
	{
		
		this.isSharingRuleSatisfied = true;
		
		
		
	}
	
	public void setRuleSatisfiedStatusFalse()
	{
		
		this.isSharingRuleSatisfied = false;
		
		
		
	}
	
	
}
