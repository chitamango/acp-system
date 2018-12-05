package com.acp.instance;
/*
 *  @version 1.1
 */

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Logger;
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;
//import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.PropertyConfigurator;
import com.acp.log.ProcessLog;
import com.acp.process.Process;


public class ProcessInstance extends Thread {

	private String processName;
	private String ProcessId;
	private ArrayList<ServiceInstance> serviceInstanceList;
	private ArrayList<ArtifactInstance> artifactInstanceList;
	private ArrayList<RuleInstance> ruleInstanceList; 
	private static Logger log = Logger.getLogger(ProcessInstance.class.getName());
	private Boolean ProcessStatusRunning;
	private ProcessLog processLog;
	// have use log4j
	
	
	
	public ProcessInstance(Process xmlBindingProcess)
	{
		processName = xmlBindingProcess.getName();
		serviceInstanceList = new ArrayList<ServiceInstance>();
		artifactInstanceList = new ArrayList<ArtifactInstance>();
		ruleInstanceList = new ArrayList<RuleInstance>();
		this.ProcessStatusRunning = false;
		

		
		
	}
	
	
	public String getProcessName()
	{
		
		return this.processName; 
			
	}
	
	public void addArtifactInstance(ArtifactInstance Artifact)
	{
		//should perform duplicate artifact checking 
		this.artifactInstanceList.add(Artifact);
			
	}
	
	
	public void addServiceInstance(ServiceInstance Service)
	{
		
		this.serviceInstanceList.add(Service);
		
	}
	
	public void addRuleInstance(RuleInstance Rule)
	{
		
		this.ruleInstanceList.add(Rule);
	}
	

	public String getProcessId()
	{
		
		
		return this.ProcessId;
		
	}
	
	public void setProcessId(String Id)
	{
		
		this.ProcessId = Id;
		
		
	}


	@Override
	// have to think about this as well
	public void run() {
		// TODO Auto-generated method stub
		Boolean AllArtifactInFinalState = false;
		
		log.info("Process is starting");
		
		this.ProcessStatusRunning = true;
		
	
		
		/*while(this.ProcessStatusRunning == true)
		{
			
			ListIterator<ArtifactInstance> ArtifactInstanceListIterator = this.artifactInstanceList.listIterator();
			
			while(ArtifactInstanceListIterator.hasNext())
			{
				ArtifactInstance CurrentArtifactInstance = ArtifactInstanceListIterator.next();
				
				String StateOfCurrentArtifact = CurrentArtifactInstance.getCurrentState();
				
				StateInstance StateInstanceOfCurrentArtifact = CurrentArtifactInstance.getStateInstance(StateOfCurrentArtifact);
				
				if(StateInstanceOfCurrentArtifact.getStateType() != null)
				{	
					if(StateInstanceOfCurrentArtifact.getStateType().equalsIgnoreCase("end"))
					{
						AllArtifactInFinalState = true;
						
					}
					
				}
				else
				{
					
					
					AllArtifactInFinalState = false;
					
				}
			}
			
			
			
			if(AllArtifactInFinalState == true)
			{
				this.ProcessStatusRunning = false;
				
			}
			
				
		}
	 
	
		log.info("Process is completed!!!!");	
		
		*/
		
	}
	
	
	//should get artifact form id
	public ArtifactInstance getArtifactInstance(String ArtifactName)
	{
		
		ArtifactInstance returnArtifact = null;
		
		ListIterator<ArtifactInstance> itr = this.artifactInstanceList.listIterator();
		while(itr.hasNext())
		{
			ArtifactInstance artifact = itr.next();
			String Name = artifact.getArtifactName();
			if(ArtifactName.equalsIgnoreCase(Name))
			{
				
				returnArtifact = artifact;
				break;
			}
					
			
		}
		
		return returnArtifact;
		
		
		
		
	}
	
	public RuleInstance getRuleInstance(String RuleName)
	{
		
		RuleInstance returnRule = null;
		
		ListIterator<RuleInstance> itr = this.ruleInstanceList.listIterator();
		while(itr.hasNext())
		{
			RuleInstance Rule = itr.next();
			String Name = Rule.getRuleName();
			if(RuleName.equalsIgnoreCase(Name))
			{
				
				returnRule = Rule;
				break;
			}
					
			
		}
		
		return returnRule;
		
		
		
		
	}
	
	
	public ArrayList<ArtifactInstance> getArtifactInstanceList()
	{
		
		return this.artifactInstanceList;
		
		
	}
	
	public ArrayList<RuleInstance> getRuleInstanceList()
	{
		
		return this.ruleInstanceList;
	}
	
	
	public ArrayList<ServiceInstance> getServiceInstanceList()
	{
		
		return this.serviceInstanceList;
		
	}
	
	public ServiceInstance getServiceInstance(String ServiceName, String OperationName)
	{
		ServiceInstance returnService = null;
		
		ListIterator<ServiceInstance> itr = this.serviceInstanceList.listIterator();
		while(itr.hasNext())
		{
			ServiceInstance Service = itr.next();
			String Name = Service.getServiceName();
			String Operation = Service.getOperation();
			if(ServiceName.equalsIgnoreCase(Name) && OperationName.equalsIgnoreCase(Operation))
			{
				
				returnService = Service;
				break;
			}
					
			
		}	
		
		return returnService;
	}
	
	public void setProcessLog(ProcessLog Log)
	{
	
		processLog = Log;
		
		
	}
	
	public ProcessLog getProcessLog()
	{
		return this.processLog;
		
		
	}
	

}
