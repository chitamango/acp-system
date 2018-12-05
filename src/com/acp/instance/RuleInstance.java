package com.acp.instance;
/*
 *  @version 1.1
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import java.text.DateFormat;

import com.acp.process.MappingType;
import com.acp.process.Maptype;
import com.acp.process.RuleType;
import com.acp.process.TransitionType;

public class RuleInstance {

	private String ruleId;
	private String ruleName;
	private  String timeStamp; 
	private  String InvokingService; 
	private String oprationName;
	private  ArrayList<String> InputValue;
	private ArrayList<TransitionRule> transitions = new ArrayList<TransitionRule>();
	private ArrayList<MapRule> Maps = new ArrayList<MapRule>();
	//need to add input value to invoke the process
	
	public RuleInstance(RuleType xmlBindingRule)
	{
		this.ruleName = xmlBindingRule.getName();
		this.InvokingService = xmlBindingRule.getDo().getInvoke().getService();
		this.oprationName = xmlBindingRule.getDo().getInvoke().getOperation();
		this.extractTransitionRule(xmlBindingRule.getDo().getInvoke().getTransitions().getTransition());
		this.extractMapRule(xmlBindingRule.getDo().getInvoke().getMapping().getMap());
		
		
	}
	
	private void extractTransitionRule(List<TransitionType> ListOfTransition)
	{
		ListIterator<TransitionType> itr = ListOfTransition.listIterator();
		
		while(itr.hasNext())
		{
			TransitionType CurrentTransitionType = itr.next();
			
			TransitionRule newTransitionRule = new TransitionRule(CurrentTransitionType);
			
			transitions.add(newTransitionRule);
			
		}
		
		
		
	}
	
	private void extractMapRule(List<Maptype> ListOfMap)
	{
		
		ListIterator<Maptype> itr = ListOfMap.listIterator();
		
		while(itr.hasNext())
		{
			Maptype CurrentMaptype = itr.next();
			
			MapRule newMapRule = new MapRule(CurrentMaptype);
			
			Maps.add(newMapRule);
			
		}
		
		
		
		
	}
	
	
	public String getRuleName()
	{
		
		return this.ruleName;
		
	}
	
	public String getInvokingServiceName()
	{
		
		return this.InvokingService;
		
		
	}
	
	public String getOperationName()
	{
		
		return this.oprationName;
		
	}
	
	public String getTimeStamp()
	{
		
		return this.timeStamp;
		
	}
	
	public ArrayList<TransitionRule> getTransitionRules()
	{
		
		return this.transitions;
		
		
	}
	
	public ArrayList<MapRule> getMapRules()
	{
		
		
		return this.Maps;
		
	}
	
	
	
	
	
	public void initialRuleInstance(String RuleId)
	{
		this.timeStamp = DateFormat.getDateTimeInstance().format(new Date());
		this.ruleId = RuleId;
		
	}
	
	public String getRuleId()
	{
		
		return this.ruleId;
		
	}
	
	
	
	
}
