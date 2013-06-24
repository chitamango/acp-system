package com.acp.instance;

import java.util.ArrayList;
import java.util.ListIterator;

public class AttributeInstance {
	
	
	private String AttributeName;
	private String dataType;
	private String Structure;
	private ArrayList<Object> ListOfValue;
	private String Unique_Id; // need this for define artifacy id 

	
	
	/**
	 * 
	 * @param Name
	 * @param Type
	 * @param Structure
	 */
	public AttributeInstance(String Name,String Type,String Structure, String UniqueId)
	{
		this.AttributeName = Name;
		this.dataType =Type;
		this.Structure = Structure;
		this.Unique_Id = UniqueId;	
		this.ListOfValue = new ArrayList<Object>();
		
	}
	/**
	 * 
	 * @return
	 */
	public String getAttributeType()
	{
		
		return this.dataType;
	}
	/**
	 * 
	 * @return
	 */
	public String getAttributeStructure()
	{
		return this.Structure;
		
	}
	/**
	 * 
	 * 
	 * @return
	 */
	public String getAttributeName()
	{
		
		return this.AttributeName;
	}
	
		
	/**
	 * 
	 * 
	 * 
	 * @param Value
	 * @throws Exception
	 */
	public void addAll(ArrayList<?> Value) throws Exception
	{
		
		if(Value.getClass().equals(ArrayList.class)) //this line
    	{
    		@SuppressWarnings("unchecked")
			ArrayList<Object> tempArray	= (ArrayList<Object>) Value;
    		ListIterator<Object> iter = tempArray.listIterator();
    		Object listItem;
    		while(iter.hasNext())
    		{
    			
    			listItem = iter.next();
    			if(listItem.getClass().getName().toLowerCase().contains(dataType.toLowerCase())== false)
    			{
    				
    				throw new Exception("Data Type is not match with specified data type in the defintion file");
    			} 			
    			
    		}
    		
    		ListOfValue.addAll(Value);
    		
    		
    	}
		
		
	}
	/**
	 * 
	 * 
	 * 
	 * @param Value
	 * @throws Exception
	 */
	public void add(Object Value) throws Exception
	{
		if(Value.getClass().getName().toLowerCase().contains(dataType.toLowerCase())== true)
		{
			ListOfValue.add(Value);
		
		}
		else
		{
			
			
			throw new Exception("Data Type is not match with specified data type in the defintion file");// should have Exception class
		
		}
			
		
	}
	/**
	 * 
	 * @param Value
	 * @throws Exception
	 */
	public void remove(Object Value) throws Exception
	{
		
		if(ListOfValue.contains(Value)== true)
		{
			ListOfValue.remove(Value);
			ListOfValue.trimToSize();
		
		}
		else
		{
			
			
			throw new Exception("Provided data is not match. Nothing removed");// should have Exception class
		
		}
		

		
	}
	
	public void remove(int index) throws Exception
	{
		
		
		if(index <= ListOfValue.size()-1)
		{
			
			ListOfValue.remove(index);
			ListOfValue.trimToSize();	
	
		}
		else
		{
			
			
			throw new Exception("Spefied index is larger than the Last index of Value List");
		}
					
		
	}	
	
	
	
	/**
	 * 
	 * @throws Exception 
	 * 
	 */
	public void removeAll() throws Exception
	{
		if(ListOfValue.isEmpty() == false)
		{	
			
			ListOfValue.removeAll(ListOfValue);
			
		}
		else
		{
			
			throw new Exception("Empty List. Cannot remove");
			
		}
			
	}
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 * @throws Exception 
	 */
	public Object get(Object Value) throws Exception
	{
		
		if(ListOfValue.contains(Value) == true)
		{
			int index = ListOfValue.indexOf(Value);
			
			return ListOfValue.get(index);
		}
		else
		{
			
			throw new Exception("Provided data is not match. Nothing return");
			
		}
			
				
					
	}
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 * @throws Exception 
	 */
	public Object get(int index) throws Exception
	{
		if(index <= ListOfValue.size()-1)
		{
			
			return ListOfValue.get(index);	
	
		}
		else
		{
			
			throw new Exception("Spefied index is larger than the Last index of Value List");
			
		}
		
			
		
	}
	
	
	
	
	/**
	 * 
	 * 
	 * 
	 * @return
	 */
	public int Size()
	{
		
		return ListOfValue.size();
		
		
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param index
	 * @param Value
	 * @throws Exception
	 */
	public void set(int index,Object Value) throws Exception
	{
		if(Value.getClass().getName().toLowerCase().contains(dataType.toLowerCase())== true)
		{
			ListOfValue.set(index, Value);
		
		}
		else
		{
			
			
			throw new Exception("Data Type is not match with specified data type in the defintion file");// should have Exception class
		
		}
	
			
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	public ListIterator<Object> attributeInterator()
	{
		
		return ListOfValue.listIterator();
	
		
		
	}
	/**
	 * 
	 * 
	 * 
	 * @param Value
	 * @return
	 */
	public Boolean contains(Object Value)
	{
		if(ListOfValue.contains(Value) == true)
		{
			return true;
			
		}
			
		return false;
		
	}
	/**
	 * 
	 * 
	 * 
	 * @param Value
	 * @return
	 */
	public Boolean containsAll(ArrayList<?> Value)
	{
		if(ListOfValue.containsAll(Value)== true)
		{
			return true;
			
		}
			
		
		return false;
		
	}
	/**
	 * 
	 * 
	 * 
	 * @return
	 */
	
	public Boolean isEmpty()
	{
		if(ListOfValue.isEmpty() == true)
		{
			return true;
			
		}
			
		return false;
		
		
		
	}
	/**
	 * 
	 * 
	 * 
	 * @param Value
	 * @return
	 */
	public int indexOf(Object Value)
	{
					
		return ListOfValue.indexOf(Value);
		
		
		
	}
	
	
	public Boolean isUniqueId()
	{
		Boolean checkResult = false;
		
		if(this.Unique_Id == null)
		{
			return checkResult;
			
		}
		else if(this.Unique_Id.equalsIgnoreCase("yes"))
		{
			checkResult = true;
			
			
		}
		return checkResult;
		
		
	}
	
	
	
	
	
	
	
	
	

}
