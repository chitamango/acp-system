package com.acp.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.acp.process.ArtifactType;
import com.acp.process.AttributesType.Attribute;
import com.acp.process.StatesType.State;


public  class ArtifactInstance {
	
	private String ArtifactId; // what i have to do with this ?? this should be combination between attribute
	private String ArtifactName;
	private ArrayList<AttributeInstance> AttributeList; 
	private ArrayList<StateInstance> StateList;
	private String CurrentState;

	
	/**
	 * 
	 * constructor method
	 * 
	 * @param XmlBindingArtifact
	 */
	public ArtifactInstance(ArtifactType xmlBindingArtifact)
	{
		//should be around here
		this.ArtifactName = xmlBindingArtifact.getName();
		this.AttributeList = new ArrayList<AttributeInstance>();
		this.StateList = new ArrayList<StateInstance>();	
		this.extractXmlBindingArtifactData(xmlBindingArtifact);
		this.setInitialstate();
		//should check from state type from xml definition
		
		
	}
	/**
	 * 
	 * this private method to populate data from xml binding artifact class to artifact instance class
	 * 
	 * 
	 * @param XmlBindingArtifact
	 */
	
	private void extractXmlBindingArtifactData(ArtifactType XmlBindingArtifact)
	{
		
		List<Attribute> bindingAttList = XmlBindingArtifact.getAttributes().getAttribute();
		List<State> bindingStateList= XmlBindingArtifact.getStates().getState();
		
		//extract attribute data
		ListIterator<Attribute> AttItr = bindingAttList.listIterator();
		
		while(AttItr.hasNext())
		{
			Attribute currentAttr = AttItr.next();
			String attType = currentAttr.getType();
			String attStructure = currentAttr.getStructure();
			String attName = currentAttr.getName();
			String attUniqueId = currentAttr.getUniqueId();
			AttributeInstance newAttr = new AttributeInstance(attName,attType,attStructure,attUniqueId);
			this.AttributeList.add(newAttr);
				
			
		}
		
		//extract state
		
		ListIterator<State> StateItr = bindingStateList.listIterator();
		
		while(StateItr.hasNext())
		{
			State previousState = StateItr.next();
			
			String stateType = previousState.getType();
			String stateName = previousState.getName();
			
			StateInstance newState = new StateInstance(stateName, stateType);
			
			this.StateList.add(newState);
			
							
		}	
		
	}
	
	public void setCurrentState(String StateName)
	{
		
		
		this.CurrentState = StateName;
		
		
	}
	
	public String getCurrentState()
	{
		
		
		return this.CurrentState;
		
	}
	
	
	public ArrayList<AttributeInstance> getAttributeList()
	{
		
		
		return this.AttributeList;
		
	}
	
	public ArrayList<StateInstance> getStateList()
	{
		
		
		
		return this.StateList;
		
	}
	
	public String getArtifactId()
	{
		return this.ArtifactId;
		
	}
	
	
	public void setArtifactId(String Id)
	{
		
		this.ArtifactId = Id;
		
	}
	
	public String getArtifactName()
	{
		
		
		return this.ArtifactName;
		
	}
	
	public AttributeInstance getAttributeInstance(String AttributeName)
	{
		AttributeInstance returnAttribute = null;
		
		ListIterator<AttributeInstance> itr = this.AttributeList.listIterator();
		while(itr.hasNext())
		{
			AttributeInstance attribute = itr.next();
			String Name = attribute.getAttributeName();
			if(AttributeName.equalsIgnoreCase(Name))
			{
				
				returnAttribute = attribute;
				break;
			}
					
			
		}
		
		return returnAttribute;
	}
	
	public StateInstance getStateInstance(String StateName)
	{
		StateInstance returnState = null;
		
		ListIterator<StateInstance> itr = this.StateList.listIterator();
		while(itr.hasNext())
		{
			StateInstance state = itr.next();
			String Name = state.getStateName();
			if(StateName.equalsIgnoreCase(Name))
			{
				
				returnState  = state;
				break;
			}
					
			
		}
		
		return returnState;
		
	
		
		
	}
	/**
	 * 
	 * 
	 *  The method to set initial state of artifact
	 * 
	 * 
	 * 
	 */
	private void setInitialstate()
	{
		ListIterator<StateInstance> itr = this.StateList.listIterator();
		while(itr.hasNext())
		{
			StateInstance InspectedState = itr.next();
			String StateType = InspectedState.getStateType();
			
			if(StateType.equalsIgnoreCase("init"));
			{
				this.CurrentState = InspectedState.getStateName();
				break;
			}
			
			
			
			
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	


}
