package com.test;

import java.util.ArrayList;

import com.acp.controllers.SharedArtifactClientManager;
import com.acp.factory.AcpFactory;
import com.acp.factory.SharingRuleFactory;
import com.acp.instance.ProcessInstance;
import com.acp.rule.AcpRuleImpl;
import com.acp.sharingrule.ClientSharingRule;

public class restRule {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

	/*	AcpRuleImpl ruleEngine = new AcpRuleImpl(); 
		
		
		AcpFactory factory = new AcpFactory();
		
		
		ProcessInstance process = factory.getProcessInstance("order_process");
		
		System.out.println(process.getProcessName()); */
		
		SharingRuleFactory  sharefac = new SharingRuleFactory();
		
		System.out.println(sharefac.SharedRuleFiles.get(0));
		
		ArrayList<ClientSharingRule> listofrule =     sharefac.getSharingRules();
		
		System.out.println(listofrule.get(0).getProcessType());
		
		System.out.println(listofrule.get(0).getRule().get(0).getWhen().getArtifactChanged().get(0).getName());
		System.out.println(listofrule.get(0).getRule().get(0).getShare().getArtifact().get(0).getName());
		System.out.println(listofrule.get(0).getRule().get(0).getShare().getArtifact().get(0).getAttributes().getAttribute().get(0));
	
	//	SharedArtifactClientManager clientManager = new SharedArtifactClientManager();
	
	//	System.out.println(clientManager.SharingRuleList.get(0).getProcessType());
	
	}
	
	
	
	

}
