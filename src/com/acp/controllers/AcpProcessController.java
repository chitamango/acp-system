package com.acp.controllers;


import java.text.DateFormat;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.swing.event.EventListenerList;

import com.acp.events.ArtifactChangeEvent;
import com.acp.factory.AcpFactory;
import com.acp.instance.ArtifactInstance;
import com.acp.instance.AttributeInstance;
import com.acp.instance.ProcessInstance;
import com.acp.instance.RuleInstance;
import com.acp.instance.ServiceInstance;
import com.acp.listeners.ArtifactChangeEventListener;
import com.acp.log.AcpLogger;
import com.acp.mapper.Mapper;
import com.acp.sharingrule.RuleType;
import com.test.eventlistener.EventDispatcher;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;





public class AcpProcessController extends Thread {

	private AcpFactory factory; 
	private ArrayList<ProcessInstance> ProcessInstanceList;
	private ArtifactController artifactController;
	private ServiceController serviceController;
	private EventListenerList listenerList ;
	private Mapper DataMapper ;
	private static Logger log;
	private AcpLogger Acplogger;
	private SharedArtifactClientManager ClientManager;
	private MonitorObject MTObject;
	private EventDispatcher EDispatcher;

	
	
	public AcpProcessController()
	{
		this.factory = new AcpFactory();
		this.ProcessInstanceList = new ArrayList<ProcessInstance>();
		this.DataMapper = new Mapper();
		this.artifactController = new ArtifactController(this.DataMapper);	
		this.serviceController = new ServiceController(this.DataMapper);
		this.listenerList = new EventListenerList();
		this.log = Logger.getLogger(AcpProcessController.class) ;
		PropertyConfigurator.configure("config/log4j.properties"); //have to change in the future 
		
		this.Acplogger = new AcpLogger();
		
		System.out.println("initialize process controller");
		System.out.println("initialize artifact controller");
		System.out.println("initialize service controller");
		
	}
	
	public void run()
	{
		
		
		
		while(true)
		{  //here
			
			
			if(this.MTObject.isRuleSatisfied() == true)	
			{
				System.out.println("time to notify");
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				this.MTObject.doNotify();
				
			}	
				
			
		}
		
		//	System.out.println("this class will do another thing");
		
		//this.stop();
		
		
	}
	

	
	/**
	 * 
	 *  to start the process
	 * 
	 * @param artifactNames
	 * @param ExecutedRule
	 * @param ProcessName
	 * @param request
	 * @throws Exception 
	 * 
	 * 
	 */
	public void startProcess(ArrayList<String> ArtifactNames, String ServiceName,String OprationName, RuleInstance ExecutedRule,String ProcessName, OMElement userMessage) throws Exception
	{
		ServiceInstance InvokingService = null;
		
		//get process instance of particular process and set processID
		ProcessInstance Process = factory.getProcessInstance(ProcessName);
	
		
		Process.setProcessId(Process.getProcessName()+"-P"+(this.ProcessInstanceList.size()+1));
		
		//System.out.println(Process.getProcessId());
		
		//add process instance to the process instance list
		
		ProcessInstanceList.add(Process);
		
		//start the thread
		
		
		Process.start();
		
		//create process log
		Acplogger.createProcessLog(Process);
		
		//set ruleID of a rule instance here and add ruleInstance
		
		
		
		Process.addRuleInstance(this.initialRuleWithIdAndTimeStamp(Process, ExecutedRule));
		
		
		//this loop retrieve artifact name in order to create artifact instances
		ListIterator<String> itr = ArtifactNames.listIterator();
		
	
		while(itr.hasNext())
		{
			String ArtifactName = itr.next().toString();
			//Artifact is created here, basically it has no business data
			ArtifactInstance Artifact = factory.getArtifactInstance(ProcessName, ArtifactName);
			
			artifactController.AddArtifactInstance(Artifact, Process);
			
			
		}
		
		//create service instance and invoke a webservice using Client Message data
		
		InvokingService = factory.getServiceInstance(Process.getProcessName(), ServiceName, OprationName ); // have to fix this to support operation
		
		Process.addServiceInstance(this.initialServiceWithId(Process, InvokingService));
			
		Acplogger.createProcessLogRecord(Process.getProcessLog(), Process.getArtifactInstanceList(), ExecutedRule.getRuleId(), InvokingService.getServiceID(), ExecutedRule.getTimeStamp());
		
		serviceController.invokeService(Process.getArtifactInstanceList(),userMessage,ServiceName,OprationName, ExecutedRule, Process,InvokingService); 
		
		//update artifact 
		
		artifactController.updateArtifact(Process.getArtifactInstanceList(), InvokingService.getSoapOutputMessage(), Process, ExecutedRule);
		
		//inital artifact id
		ListIterator<ArtifactInstance> ArtifactInstanceListIterator = Process.getArtifactInstanceList().listIterator();
		while(ArtifactInstanceListIterator.hasNext())
		{
			ArtifactInstance CurrentArtifactInstance = ArtifactInstanceListIterator.next();
			if(CurrentArtifactInstance.getArtifactId() == null)
			{
				this.initialArtifactInstancewithid(CurrentArtifactInstance);
				
				
			}
			
			
		}
		
		
		
		
		//should fire event here
		Acplogger.UpdateProcessLogRecord(Process.getProcessLog(), Process.getArtifactInstanceList());
		
		//start
		
		
		if(this.ClientManager != null)
		{	
			Thread.State state = this.getState();
			if(state == Thread.State.NEW) {
			    
				
				this.start();
				
			} 
			
			
			
			//verify with sharing rule
			RuleType matchedRule = this.ClientManager.verifyStatechangedWithSharingRule(Process);
			
			
			
			if(matchedRule != null)
			{
				
				
				//do something here
			
				this.MTObject.setRule(matchedRule);
				this.MTObject.setProcessInstance(Process);
				this.MTObject.setRuleSatisfiedStatusTrue();
				
				
			}
		}	
		
		this.EDispatcher.setProcessId(Process.getProcessId());
		
		this.fireArtifactChangeEvent(new ArtifactChangeEvent(this,Process));
	
		
		
	}
	
	public void runProcess(ArrayList<String> ArtifactNames,OMElement userMessage ,String ServiceName,String OprationName,RuleInstance ExecutedRule,String ProcessID) throws Exception
	{
		Thread.sleep(2000);
		
		ProcessInstance Process = null;
		ServiceInstance InvokingService = null;
		ListIterator<ProcessInstance> Processitr = this.ProcessInstanceList.listIterator();
		
		
		while(Processitr.hasNext())
		{
			 ProcessInstance CurrentIteratedProcess = Processitr.next();
			if(ProcessID.equals(CurrentIteratedProcess.getProcessId()))
			{
				
				Process = CurrentIteratedProcess;
				
			}
			else
			{
			
				// do something here ie. show error message
		
			}
			
			
			
		}
		
		//add ruleInstance 
		

		
		Process.addRuleInstance(this.initialRuleWithIdAndTimeStamp(Process, ExecutedRule));
		
		
		if(ArtifactNames.size() != 0)
		{
			ListIterator<String> itr = ArtifactNames.listIterator();
			
			
			//artifact is created at this point
			while(itr.hasNext())
			{
				String ArtifactName = itr.next().toString();
				
				ArtifactInstance Artifact = factory.getArtifactInstance(Process.getProcessName(), ArtifactName);
				
				artifactController.AddArtifactInstance(Artifact, Process); //?? not consistent
	
				
			}
		
		}

		
		//create service instance and invoke a web service using Artifact data or client message data
		
		InvokingService = factory.getServiceInstance(Process.getProcessName(), ServiceName, OprationName ); // have to fix this to support operation
		
		Process.addServiceInstance(this.initialServiceWithId(Process, InvokingService));
			
		Acplogger.createProcessLogRecord(Process.getProcessLog(), Process.getArtifactInstanceList(), ExecutedRule.getRuleId(), InvokingService.getServiceID(), ExecutedRule.getTimeStamp());
	//problem with service controller	
		serviceController.invokeService(Process.getArtifactInstanceList(),userMessage,ServiceName,OprationName, ExecutedRule, Process,InvokingService); 
		
		//update artifact 
		
		artifactController.updateArtifact(Process.getArtifactInstanceList(), InvokingService.getSoapOutputMessage(), Process, ExecutedRule);
		
		//inital artifact id
		ListIterator<ArtifactInstance> ArtifactInstanceListIterator = Process.getArtifactInstanceList().listIterator();
		while(ArtifactInstanceListIterator.hasNext())
		{
			ArtifactInstance CurrentArtifactInstance = ArtifactInstanceListIterator.next();
			if(CurrentArtifactInstance.getArtifactId() == null)
			{
				this.initialArtifactInstancewithid(CurrentArtifactInstance);
				
				
			}
			
			
		}
		
		
		//updatelog again
		
		Acplogger.UpdateProcessLogRecord(Process.getProcessLog(), Process.getArtifactInstanceList());
		
		if(this.ClientManager != null)
		{
			Thread.State state = this.getState();
			if(state == Thread.State.NEW) {
			    
				
				this.start();
				
			} 
			
			
			//verify with sharing rule
			RuleType matchedRule = this.ClientManager.verifyStatechangedWithSharingRule(Process);
			
			if(matchedRule != null)
			{
				
				//do something here
				
				this.MTObject.setRule(matchedRule);
				this.MTObject.setProcessInstance(Process);
				this.MTObject.setRuleSatisfiedStatusTrue();
				
				
			}
		}
		
		
		//fire event
		this.fireArtifactChangeEvent(new ArtifactChangeEvent(this,Process));
		
		
		
	}
	

	
	public void endProcess(String ProcessID, RuleInstance ExecutedRule) throws Exception
	{
		
		ProcessInstance Process = null;
		ListIterator<ProcessInstance> Processitr = this.ProcessInstanceList.listIterator();
		
		while(Processitr.hasNext())
		{
			 ProcessInstance CurrentIteratedProcess = Processitr.next();
			if(ProcessID.equals(CurrentIteratedProcess.getProcessId()))
			{
				
				Process = CurrentIteratedProcess;
				
			}
			else
			{
			
				// do something here ie. show error message
		
			}
			
			
			
		}
		
		
		//add ruleInstance 
		
		Process.addRuleInstance(this.initialRuleWithIdAndTimeStamp(Process, ExecutedRule));
		
		Acplogger.createProcessLogRecord(Process.getProcessLog(), Process.getArtifactInstanceList(), ExecutedRule.getRuleId(), "system", ExecutedRule.getTimeStamp());
		
		artifactController.updateArtifact(Process.getArtifactInstanceList(), Process, ExecutedRule);
		
		// should change state of artifact to end state
		
	
		
		Acplogger.UpdateProcessLogRecord(Process.getProcessLog(), Process.getArtifactInstanceList());
		
		//generate log file ??
		
		Thread.State state = this.getState();
		if(state == Thread.State.NEW) {
		    
			
			this.start();
			
		} 
		
		
		//verify with sharing rule
		RuleType matchedRule = this.ClientManager.verifyStatechangedWithSharingRule(Process);
		
		if(matchedRule != null)
		{
			
			//do something here
			
			this.MTObject.setRule(matchedRule);
			this.MTObject.setProcessInstance(Process);
			this.MTObject.setRuleSatisfiedStatusTrue();
			
			
		}
		
		//endprocess here
		
		
	}
	
	private RuleInstance initialRuleWithIdAndTimeStamp(ProcessInstance InstanceOfProcess,RuleInstance InstanceOfRule)
	{
		Integer RuleInstanceListSize = InstanceOfProcess.getRuleInstanceList().size();
		
		String GeneratedRuleID = InstanceOfRule.getRuleName()+":R"+(RuleInstanceListSize+1);
		
		InstanceOfRule.initialRuleInstance(GeneratedRuleID);
		
		return InstanceOfRule;
		
		
	}
	
	
	private ServiceInstance initialServiceWithId(ProcessInstance InstanceOfProcess, ServiceInstance InstanceOfService)
	{
		Integer ServiceInstanceListSize = InstanceOfProcess.getServiceInstanceList().size();
		
		String GeneratedServiceID = InstanceOfService.getServiceName()+":S"+((ServiceInstanceListSize+1));
		
		InstanceOfService.setServiceID(GeneratedServiceID);
		
		return InstanceOfService;
		
	}
	
	public void setClientManager(SharedArtifactClientManager clientManager)
	{
		
		this.ClientManager = clientManager;
		
		
	}
	
	public SharedArtifactClientManager getClientManager()
	{
		
		return this.ClientManager;
		
	}
	
	private void initialArtifactInstancewithid( ArtifactInstance InstanceOfArtifact)
	{
		ListIterator<AttributeInstance> AttributeInstanceListIterator = InstanceOfArtifact.getAttributeList().listIterator();
		String Artifactid = InstanceOfArtifact.getArtifactName()+ ":";
		
		while(AttributeInstanceListIterator.hasNext())
		{
			AttributeInstance CurrentAttributeInstance = AttributeInstanceListIterator.next();
			if(CurrentAttributeInstance.isUniqueId() == true)
			{
				try {
					
					Artifactid = Artifactid + CurrentAttributeInstance.get(0).toString();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
			
		}
		
		InstanceOfArtifact.setArtifactId(Artifactid);
		
		
	}

	
	public void addEventListener(ArtifactChangeEventListener listener) {
	    listenerList.add(ArtifactChangeEventListener.class, listener);
	    
	  }
	
	  public void removeEventListener(ArtifactChangeEventListener listener) {
	    listenerList.remove(ArtifactChangeEventListener.class, listener);
	  }
	  
	  private void fireArtifactChangeEvent(ArtifactChangeEvent evt) {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) {
	      if (listeners[i] == ArtifactChangeEventListener.class) {
	    	  
	        ((ArtifactChangeEventListener) listeners[i+1]).invokeRuleEngine(evt);
	        
	        
	        
	        
	      }
	    }
	
	  }
	  
	  public ProcessInstance getProcessInstance(String ProcessId)
	  {
		  ListIterator<ProcessInstance> Processitr = this.ProcessInstanceList.listIterator();
		  ProcessInstance Process = null;
		  
			while(Processitr.hasNext())
			{
				 ProcessInstance CurrentIteratedProcess = Processitr.next();
				if(ProcessId.equals(CurrentIteratedProcess.getProcessId()))
				{
					
					Process = CurrentIteratedProcess;
					
					break;
				}
			
				
				
			}
		  
		  
		  
		  
		  return Process;
		  
		  
	
		  
	  }
	  
	  public Mapper getMapper()
	  {
		  
		  
		  return this.DataMapper;
		  
	  }
	 
	  public void setMonitor(MonitorObject Monitor)
	  {
		  
		  this.MTObject = Monitor;
		  
	  }
	  
	  public void setEventDispatcher(EventDispatcher Edispatcher)
	  {
		  
		  this.EDispatcher = Edispatcher;
		  
	  }
	
	
	
}
