package com.acp.log;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ProcessLog {
	
	private ArrayList<LogRecord> ListOfProcessRecord;
	private String ProcessID;
	private Integer currentRecordNo;
	private DocumentBuilderFactory domFactory;
	private  DocumentBuilder Dbuilder;
	private Document doc;
	
	
	
	public ProcessLog(String ProcessId)
	{
		 
		
		
		try {
			currentRecordNo = 0;
			ProcessID = ProcessId;
			ListOfProcessRecord = new ArrayList<LogRecord>();
			
			domFactory = DocumentBuilderFactory.newInstance();
		   // domFactory.setNamespaceAware(true); // never forget this
			Dbuilder = domFactory.newDocumentBuilder();
			doc = Dbuilder.newDocument();
			
			
			
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
	}
	
	public ArrayList<LogRecord> getLogRecords()
	{
		
		return this.ListOfProcessRecord;
		
		
	}
	
	public void addLogRecord(LogRecord Record)
	{
		
		this.ListOfProcessRecord.add(Record);
		this.currentRecordNo = Record.getRecordNumber();
		
		
	}
	
	public LogRecord getLogRecord(Integer RecordNo)
	{
		LogRecord ReturnedLogrecord = null;
		ListIterator<LogRecord> LogrecordListIterator = this.ListOfProcessRecord.listIterator();
		while(LogrecordListIterator.hasNext()) 
		{
			LogRecord currentRecord =  LogrecordListIterator.next();
			
			if(currentRecord.getRecordNumber() == RecordNo );
			{
				ReturnedLogrecord = currentRecord;
				
			}
	
			
		}
		
		
		
		return ReturnedLogrecord;
		
	}
	
	public Integer getCurrentLogRecordNumber()
	{
		
		
		return this.currentRecordNo;
		
	}
	
	public Integer size()
	{
		return this.ListOfProcessRecord.size();
		
		
		
	}
	
	
	public String getProcessId()
	{
		
		return this.ProcessID;
		
	}
	
	
	public Element getCompleteLog()
	{
		
		ListIterator<LogRecord> LogrecordListIterator = this.ListOfProcessRecord.listIterator();
		
		Element Log = doc.createElement("log");
		Log.setAttribute("process_id", ProcessID);
			
		while(LogrecordListIterator.hasNext())
		{
			LogRecord CurrentRecord = LogrecordListIterator.next();
			
			Node importedNode = doc.importNode(CurrentRecord.getLogRecord(), true);
			
			Log.appendChild(importedNode);
				
			
			
		}
		
		
		return Log;
		
		
	}
	
	

}
