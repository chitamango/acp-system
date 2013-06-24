package com.acp.log;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class LogRecord {
	
	private Integer RecordOrderNumber;
	private Element Timestamp;
	private Element RuleId;
	private Element ServiceId;
	private Element pre_Artifacts;
	private Element post_Artifacts;
	private Element Record;
	private DocumentBuilderFactory domFactory;
	DocumentBuilder Dbuilder;
	Document doc;
	
	
	public LogRecord(Integer RecordOrderNo )
	{
		
		try {
			
			domFactory = DocumentBuilderFactory.newInstance();
		   // domFactory.setNamespaceAware(true); // never forget this
			Dbuilder = domFactory.newDocumentBuilder();
			doc = Dbuilder.newDocument();
		
			this.RecordOrderNumber = RecordOrderNo;
			this.Timestamp = doc.createElement("timestamp");
			this.RuleId = doc.createElement("ruleId");
			this.ServiceId = doc.createElement("serviceId");
			this.pre_Artifacts = doc.createElement("pre_artifact");
			this.post_Artifacts = doc.createElement("post_artifact");
			this.Record = doc.createElement("record");
			this.Record.setAttribute("no", this.RecordOrderNumber.toString());
			
			this.Record.appendChild(Timestamp);
			this.Record.appendChild(RuleId);
			this.Record.appendChild(ServiceId);
			this.Record.appendChild(pre_Artifacts);
			this.Record.appendChild(post_Artifacts);
			
			
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	
	}
	
	public void setTimestamp(String timestamp)
	{
		this.Timestamp.setTextContent(timestamp);
		
		
	}
	
	public void setRuleId(String RuleId)
	{
		
		this.RuleId.setTextContent(RuleId);
		
		
	}
	
	public void setServiceId(String ServiceId)
	{
		
		this.ServiceId.setTextContent(ServiceId);
		
		
	}
	
	public void setPreArtifacts(Element Artifact)
	{
		
		Node importedNode = doc.importNode(Artifact, true);
		//test
	
		
		this.pre_Artifacts.appendChild(importedNode);
		
		
	}
	
	public void setPostArtifacts(Element Artifact)
	{
		
		
		Node importedNode = doc.importNode(Artifact, true);
		this.post_Artifacts.appendChild(importedNode);
		
	}
	
	public String getTimestamp()
	{
		
		return this.Timestamp.getTextContent();
		
	}
	
	public String getRuleId()
	{
		
		return this.RuleId.getTextContent();
		
	}
	
	public String getServiceId()
	{
		
		return this.ServiceId.getTextContent();
		
	}
	
	public Element getPreArtifact()
	{
		
		return this.pre_Artifacts;
		
	}
	
	public Element getPostArtifact()
	{
		
		return this.post_Artifacts;
		
	}
	
	public Element getLogRecord()
	{
		
		return this.Record;
		
	}
	
	public Integer getRecordNumber()
	{
		return this.RecordOrderNumber;
		
	}
	
	
	
}
