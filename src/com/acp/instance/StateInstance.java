package com.acp.instance;

public class StateInstance {
	
	private String Name;
	private String Type;
	
	
	public StateInstance(String StateName,String StateType)
	{
		this.Name = StateName;
		this.Type = StateType;
				
	}
	
	public StateInstance(String StateName)
	{
		this.Name = StateName;
		
				
	}
	
	
	
	public void setStateName(String StateName)
	{
		
		this.Name = StateName;
		
		
	}
	
	public String getStateName()
	{
		
		return this.Name;
		
	}
	
	public void setStateType(String StateType)
	{
		
		this.Type = StateType;
		
	}
	
	public String getStateType()
	{
		return this.Type;
		
		
	}
	
	

}
