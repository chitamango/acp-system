package com.acp.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.acp.process.Maptype;
import com.acp.process.CopyType;
import com.acp.process.FromType;
import com.acp.process.ToType;

public class MapRule {

	private String Type;
	private ArrayList<copy> MappingList = new ArrayList<copy>();
	
	
	
	public MapRule(Maptype xmlBindingMap)
	{ 
		
		this.Type =  xmlBindingMap.getType();
		this.extractMapping(xmlBindingMap.getCopy());
		
		
	}
	
	private void extractMapping(List<CopyType> ListOfCopyType)
	{
		ListIterator<CopyType> itr = ListOfCopyType.listIterator();
		
		
		while(itr.hasNext())
		{
			CopyType currentCopyType = itr.next();
			
			copy newcopy = new copy(currentCopyType);
			
			MappingList.add(newcopy);
			
				
		}
		
		
		
	}
	
	public String getType()
	{
		
		return this.Type;
		
	}
	
	public ArrayList<copy> getMappingList()
	{
		
		return this.MappingList;
		
	}
	
	
	
	
	
	//inner class
	public static class copy {
		
		
		private String fromArtifact;
		private String fromMessage;
		private String fromPart;
		private String fromAttribute;
		private String toArtifact;
		private String toMessage;
		private String toPart;
		private String toAttribute;
		
		
		public copy(CopyType xmlBindingCopy)
		{
			this.fromArtifact = xmlBindingCopy.getFrom().getArtifact();
			this.fromMessage = xmlBindingCopy.getFrom().getMessage();
			this.fromPart = xmlBindingCopy.getFrom().getPart();
			this.fromAttribute = xmlBindingCopy.getFrom().getAttribute();
			this.toArtifact = xmlBindingCopy.getTo().getArtifact();
			this.toMessage = xmlBindingCopy.getTo().getMessage();
			this.toPart = xmlBindingCopy.getTo().getPart();	
			this.toAttribute = xmlBindingCopy.getTo().getAttribute();
					
		}
		
		public String getFromArtifact()
		
		{
			return this.fromArtifact;
			
		}
		
		
		public String getFromMessage()
		
		{
			return this.fromMessage;
			
		}
		
		public String getFromPart()
		
		{
			return this.fromPart;
			
		}
		
		public String getFromAttribute()
		{
			return this.fromAttribute;
		}
		
		public String getToArtifact()
		
		{
			return this.toArtifact;
			
		}
		
		
		public String getToMessage()
		
		{
			return this.toMessage;
			
		}
		
		public String getToPart()
		
		{
			return this.toPart;
			
		}
		
		public String getToAttribute()
		{
			return this.toAttribute;
			
		}
		
	}
	
	
	
	
	
	
	
}
