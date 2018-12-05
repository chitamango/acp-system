package com.acp.events;
/*
 *  @version 1.1
 */

import java.util.EventObject;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;

import com.acp.instance.ArtifactInstance;
import com.acp.instance.AttributeInstance;
import com.acp.instance.ProcessInstance;

public class CombineEvent extends EventObject {

	protected OMElement MessageElement;
	protected ProcessInstance InstanceOfProcess;
	
	public CombineEvent(Object Source, OMElement Message, ProcessInstance Process) {
		super(Source);
		// TODO Auto-generated constructor stub
		this.MessageElement = Message;
		this.InstanceOfProcess =Process;
		
		
	}
	
	public String getValueFromPart(String PartName)
	{
		String PartValue = null;
		
		OMElement Part = this.MessageElement.getFirstChildWithName(new QName(this.MessageElement.getNamespace().getNamespaceURI(),PartName));
		
		if(Part != null)
		{	
			PartValue = Part.getText();
		}
		
		return PartValue;
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
						
						value = TargetAttribute.get(0).toString();
						
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
	
	
	public OMElement getMessage()
	{
	
		return this.MessageElement;
		
	}
	
	public String getProcessId()
	{
		
		return this.InstanceOfProcess.getProcessId();
		
	}
	
	public ProcessInstance getProcessInstance()
	{
		
		return this.InstanceOfProcess;
		
	}
	

}
