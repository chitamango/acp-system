package com.acp.events;

import java.util.EventObject;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;


public class MessageEvent extends EventObject {
	
	protected OMElement MessageElement;
	private String MessageEventName; 
	
	
	public MessageEvent(Object Source,OMElement Message) {
		super(Source);
		
		// TODO Auto-generated constructor stub
		this.MessageEventName = Message.getLocalName();
	
		this.MessageElement = Message;
		
		
		
		
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
	
	
	public OMElement getMessage()
	{
	
		return this.MessageElement;
		
	}

}
