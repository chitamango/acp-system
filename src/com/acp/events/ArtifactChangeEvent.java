package com.acp.events;
/*
 *  @version 1.1
 */

import java.util.EventObject;

import com.acp.instance.ArtifactInstance;
import com.acp.instance.AttributeInstance;
import com.acp.instance.ProcessInstance;

public class ArtifactChangeEvent extends EventObject {
	
	protected ProcessInstance InstanceOfProcess;

	public ArtifactChangeEvent(Object Source,ProcessInstance Process) {
		super(Source);
		// TODO Auto-generated constructor stub
		this.InstanceOfProcess = Process;
		
	}
	
	
	public ProcessInstance getProcessInstance()
	{
		
		
		
		return this.InstanceOfProcess;
		
	}
	
	
	public String getArtifactData(String ArtifactName, String AttributeName) 
	{
		String value = null;
		
		if( this.InstanceOfProcess.getArtifactInstance(ArtifactName) != null)
		{
			ArtifactInstance TargetArtifact = this.InstanceOfProcess.getArtifactInstance(ArtifactName);
			
			if(TargetArtifact.getAttributeInstance(AttributeName) != null)
			{
				AttributeInstance TargetAttribute = TargetArtifact.getAttributeInstance(AttributeName);
				
				if(TargetAttribute.getAttributeStructure().equalsIgnoreCase("pair"))
				{
					try {
						
						if(TargetAttribute.Size() > 0)
						{
						
							value = TargetAttribute.get(0).toString();
							
						}	
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else if(TargetAttribute.getAttributeStructure().equalsIgnoreCase("list"))
				{
					
				
						
					
				}
				
				
				
			}
			
			
		
		
		}
		
		return value;
	}
	
	
	public String getArtifactState(String ArtifactName)
	{
		
		String ArtifactState = null;
		
		if(this.InstanceOfProcess.getArtifactInstance(ArtifactName) != null)
		{
		 ArtifactState = this.InstanceOfProcess.getArtifactInstance(ArtifactName).getCurrentState();
		
		}
		
		return ArtifactState;
		

	}
	
	public String getProcessId()
	{
		
		return this.InstanceOfProcess.getProcessId();
		
	}
	
	

}
