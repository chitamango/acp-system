package com.acp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

import com.acp.events.CombineEvent;
import com.acp.events.MessageEvent;
import com.acp.factory.SharingRuleFactory;
import com.acp.instance.ArtifactInstance;
import com.acp.instance.ProcessInstance;
import com.acp.mapper.Mapper;
import com.acp.rule.AcpRuleImpl;
import com.acp.sharingrule.ArtifactChangedType;
import com.acp.sharingrule.ArtifactType;
import com.acp.sharingrule.AttributeType;
import com.acp.sharingrule.ClientSharingRule;
import com.acp.sharingrule.RuleType;
//going to implement thread
public class SharedArtifactClientManager extends Thread{
	//coId + roleName ->processId
	private HashMap<HashMap<String,String>,String> Correlationtable; //process id, coordination id and  rule id
	private AcpRuleImpl RuleEngine; //need to be set 
	private AcpProcessController PController;// need to be set
	private ArrayList<ClientSharingRule> SharingRuleList;
	private Mapper mapper; //need to be set
	private MonitorObject Monitor;// need to be set
	
	public SharedArtifactClientManager()
	{
		
		
		
		SharingRuleFactory  sharingRuleFac = new SharingRuleFactory();
		this.SharingRuleList = sharingRuleFac.getSharingRules();
		Correlationtable = new HashMap<HashMap<String,String>,String>();
		
		
	}
	
	
	public void run()
	{
		
		
		while(true)
		{
			
			
			
		}
		
		
		
		
	}
	
	//still thinking about this here
	public OMElement recieverReplyCoodinationMessage(OMElement CoordinationMessage) throws Exception
	{
		
		OMElement payload = null;
		OMElement notifacationMessage = null;
		RuleType rule = null;
		ProcessInstance processInstance = null;
		
		//should record id or whatever to create correlation
		QName CoordinationIdElementQname = new QName(CoordinationMessage.getNamespace().getNamespaceURI(), "coordinateId");
		QName RuleIdElementQname = new QName(CoordinationMessage.getNamespace().getNamespaceURI(), "ruleId");
		QName RoleElementQname = new QName(CoordinationMessage.getNamespace().getNamespaceURI(), "role");
		QName payloadMessageElementQname = new QName(CoordinationMessage.getNamespace().getNamespaceURI(), "messagePayload");
		
		//correlation information
		String CoordinationId = CoordinationMessage.getFirstChildWithName(CoordinationIdElementQname).getText();
		String RuleId =	CoordinationMessage.getFirstChildWithName(RuleIdElementQname).getText();
		String role = 	CoordinationMessage.getFirstChildWithName(RoleElementQname).getText();
		OMElement payloadMessage = CoordinationMessage.getFirstChildWithName(payloadMessageElementQname).getFirstElement();
		
		System.out.println(payloadMessage);
		
		HashMap<String,String> CorrelationtTableKey = new HashMap<String,String>();
		
		CorrelationtTableKey.put(CoordinationId, role);
		
		
		//check whether Coordination id exist or not if yes retrieve existing process instance  
		if(Correlationtable.containsKey(CorrelationtTableKey))
		{
			
			//System.out.println(payloadMessage.getFirstChildWithName(new QName(payloadMessage.getNamespace().getNamespaceURI(),"item")).getText());
			//System.out.println(payloadMessage.getFirstChildWithName(new QName(payloadMessage.getNamespace().getNamespaceURI(),"quantity")).getText());
			
			String ProcessId = Correlationtable.get(CorrelationtTableKey);
			
			ProcessInstance TargetProcess = PController.getProcessInstance(ProcessId);
			
			CombineEvent CEvent = new CombineEvent(this,payloadMessage,TargetProcess);
			
			RuleEngine.executeRules(CEvent, PController);
			//RuleEngine.executeRules(payloadMessage, PController);
			
			System.out.println("invoking rule engine and waiting");
			this.Monitor.doWait();
			
		}
		else
		{	
			
			//add new correlation in the correlation table
			Correlationtable.put(CorrelationtTableKey, null);
			
			MessageEvent MEvent = new MessageEvent(this, payloadMessage);
			
			//should be able to check for ProcessId if the same role is invoked
			RuleEngine.executeRules(MEvent, PController);
			
			System.out.println("invoking rule engine and waiting");
			this.Monitor.doWait();
		
			
		}
		
		//System.out.println("invoking rule engine and waiting");
		
		//make this waiting for signal of process controller by calling wait method of monitor object
		//this.Monitor.doWait();
		
		
		
		
		
		
		//need to know what process instance need to know
		
		System.out.println("the notification receieved from the process controller");
		
		processInstance = Monitor.getProcessInstane();
		
		rule = Monitor.getRule();
		
		ListIterator<ArtifactInstance> ArtifactListIterator = processInstance.getArtifactInstanceList().listIterator();
		
		//create payload and do mapping
		String CorrelationTableValue = Correlationtable.get(CorrelationtTableKey);
		
		//if the coordinationId + processId is not in the correlation table
		if(CorrelationTableValue == null)
		{
			Correlationtable.remove(CorrelationtTableKey);
			
			Correlationtable.put(CorrelationtTableKey, processInstance.getProcessId());
			
			
		}
		
		//create a payload
		payload = this.createPayload(rule, CoordinationMessage);
		//do mapping
		while(ArtifactListIterator.hasNext())
		{
			ArtifactInstance CurrentArtifact = ArtifactListIterator.next();
		
			mapper.mapppingArtifactToPayload(this.Monitor.getRule(), CurrentArtifact,payload ); //use the same sharing rule
		
		 
		}
		
		//create notification message
		notifacationMessage = this.createNotificationMessage(payload, CoordinationId, RuleId);
		
		//reset monitor object
		Monitor.setProcessInstance(null);
		Monitor.setRule(null);
		Monitor.setRuleSatisfiedStatusFalse();
		//return notification message
		return notifacationMessage;
		
		
		
		
	}
	
	
	
	
	private OMElement createNotificationMessage(OMElement payload,String CoordinationId, String RuleId)
	{
		
		OMFactory fac = OMAbstractFactory.getOMFactory();
		//create the elements
		OMNamespace omNs = fac.createOMNamespace(payload.getNamespace().getNamespaceURI(), "");
		OMElement NotificationMessageElement = fac.createOMElement("notificationMessage", omNs);
		OMElement coordinationIdElement = fac.createOMElement("coordinateId", omNs);
		//should have role as well
		OMElement RuleIdElement = fac.createOMElement("ruleId", omNs);
		
		
		//assign value
		coordinationIdElement.setText(CoordinationId);
		RuleIdElement.setText(RuleId);
		
		
		//build the complete coordinationmessage
		NotificationMessageElement.addChild(coordinationIdElement);
		NotificationMessageElement.addChild(RuleIdElement);
		NotificationMessageElement.addChild(payload);
		
		
		
		
		return NotificationMessageElement;
		
		
		
	
		
	}
	
	private OMElement createPayload(RuleType SharingRule,OMElement CoordinationMessage)
	{
		
		OMElement Payload = null; 
		
		ListIterator<ArtifactType> sharingArtifactListIterator =  SharingRule.getShare().getArtifact().listIterator();
		
		OMFactory fac = OMAbstractFactory.getOMFactory();
		
		//create the elements
		
		OMNamespace omNs = fac.createOMNamespace(CoordinationMessage.getNamespace().getNamespaceURI(), "");
		
	//	OMNamespace omNs = CoordinationMessage.getDefaultNamespace();
		
		OMElement ArtifactsElement = fac.createOMElement("artifacts", omNs);
		
		
		
		
		while(sharingArtifactListIterator.hasNext())
		{
			ArtifactType CurrentArtifactType = sharingArtifactListIterator.next();
			
			String ArtifactName = CurrentArtifactType.getName();
				
			OMElement localArtifactElement = fac.createOMElement("local_artifact", omNs);
					
			OMAttribute localArtifactNameAttribute = fac.createOMAttribute("name", omNs, ArtifactName);
			
			localArtifactElement.addAttribute(localArtifactNameAttribute);
			
			ListIterator<String> AttributeTypeListIterator = CurrentArtifactType.getAttributes().getAttribute().listIterator();
			
			while(AttributeTypeListIterator.hasNext())
			{
				String AttributeName = AttributeTypeListIterator.next();
					
				OMAttribute AttributeNameAttribute = fac.createOMAttribute("name", omNs, AttributeName);
				
				OMElement AttributeElement = fac.createOMElement("attribute", omNs);
	
				AttributeElement.addAttribute(AttributeNameAttribute);
				
				localArtifactElement.addChild(AttributeElement);
				
				
				
			}
			
			
			ArtifactsElement.addChild(localArtifactElement);
			
			
		}
		
		
		
		Payload = ArtifactsElement;
		
	
		
		
		
		return Payload;
		
	}
	
	public void setRuleEngine(AcpRuleImpl ruleEngine)
	{
		
		this.RuleEngine = ruleEngine;
		
		
		
	}
	
	public AcpRuleImpl getRuleEngine()
	{
		
		
		return this.RuleEngine;
		
	}
	
	public void setProcessController(AcpProcessController processController)
	{
		
		
		this.PController = processController;
		
	}
	
	public AcpProcessController getProcessController()
	{
		
		
		return this.PController;
	}
	
	//still need to do something here
	public RuleType verifyStatechangedWithSharingRule(ProcessInstance process)
	{
		
		RuleType MatchedRule = null; 
		
		String ProcessName = process.getProcessName();
		
		
		ClientSharingRule TargetSetofSharingRule = null;
		
		ArrayList<ArtifactInstance> ListOfArtifactInstance = process.getArtifactInstanceList();
		
		//select the right rule using the process name
		
		ListIterator<ClientSharingRule> ListIteratorOfsharingRules = this.SharingRuleList.listIterator();
		
		while(ListIteratorOfsharingRules.hasNext())
		{
			ClientSharingRule CurrentSetOfSharingRule = ListIteratorOfsharingRules.next();
			
			String TypeOfProcess = CurrentSetOfSharingRule.getProcessType();
			
			System.out.println(ProcessName + "&" + TypeOfProcess);
			
			if(TypeOfProcess.equalsIgnoreCase(ProcessName))
			{
				TargetSetofSharingRule = CurrentSetOfSharingRule;
				break;
				
				
			}
				
			
		}
		
		
		
		//try to validate a rule
		ListIterator<RuleType> SharingRuleListIterator = TargetSetofSharingRule.getRule().listIterator();
		
	//	System.out.println("sharing rule has " + TargetSetofSharingRule.getRule().size() + " rule(s)");
		
		while(SharingRuleListIterator.hasNext())
		{
			
			RuleType CurrentSharingRule = SharingRuleListIterator.next();
			
			Integer totalCondition = CurrentSharingRule.getWhen().getArtifactChanged().size();
			
			Integer totalPassed = 0;
			
			ListIterator<ArtifactChangedType> ConditionListIterator = CurrentSharingRule.getWhen().getArtifactChanged().listIterator();
			
			
			while(ConditionListIterator.hasNext())
			{
				ArtifactChangedType CurrentCondition = ConditionListIterator.next();
				
				String ArtifactName = CurrentCondition.getName();
				
				String ArtifactState = CurrentCondition.getState();
				
				
				ListIterator<ArtifactInstance> ListIteratorOfArtifactInstance = ListOfArtifactInstance.listIterator();
				
				while(ListIteratorOfArtifactInstance.hasNext())
				{
					ArtifactInstance CurrentArtifactInstance = ListIteratorOfArtifactInstance.next();
					
					String ArtifactInstanceName = CurrentArtifactInstance.getArtifactName();
					
					String ArtifactInstanceState = CurrentArtifactInstance.getCurrentState();
					
					
					if(ArtifactName.equalsIgnoreCase(ArtifactInstanceName)&& ArtifactState.equalsIgnoreCase(ArtifactInstanceState))
					{
						totalPassed = totalPassed +1;
						
					}
					
					
					
					
				}
				
			
			}
			
			if(totalCondition == totalPassed)
			{
				
				MatchedRule = CurrentSharingRule;
				
				
			}
			
			
		}
			
		
		return MatchedRule;
		

	}
	
	public void setMonitor(MonitorObject monitor)
	{
		this.Monitor = monitor;
		
		
	}
	
	public void setMapper(Mapper mapper)
	{
		
		this.mapper = mapper;
		
	}

}
