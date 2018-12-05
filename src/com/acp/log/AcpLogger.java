package com.acp.log;
/*
 *  @version 1.1
 */

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.util.ArrayList;
import java.util.ListIterator;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

import com.acp.instance.ArtifactInstance;
import com.acp.instance.AttributeInstance;
import com.acp.instance.ProcessInstance;
import com.acp.instance.RuleInstance;
import com.acp.instance.ServiceInstance;

public class AcpLogger {
	
	DocumentBuilderFactory domFactory; 
    DocumentBuilder docBuilder; 
    Document doc;
    
    
    public AcpLogger()
    {
    	
    	
	    try {
	    	
	    	domFactory = DocumentBuilderFactory.newInstance();
	    	
		  //  domFactory.setNamespaceAware(true); // never forget this
		    
		    docBuilder = domFactory.newDocumentBuilder();
		    doc = docBuilder.newDocument();
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
    public void createProcessLog(ProcessInstance InstanceOfProcess)
    {
    	
    	ProcessLog LogOfProcess = new ProcessLog(InstanceOfProcess.getProcessId());
    		
    	InstanceOfProcess.setProcessLog(LogOfProcess);
    
    	
    	
    }
    
    public void createProcessLogRecord(ProcessLog LogOfProcess,ArrayList<ArtifactInstance> ListOfArtifactInstance,String RuleId, String ServiceId, String Timestamp)
    {
    	Integer currentLogRecordNumber = LogOfProcess.getCurrentLogRecordNumber();
    
    	Integer NextLogRecordNumber = currentLogRecordNumber+1;
    	
    	LogRecord Record = new LogRecord(NextLogRecordNumber);
    	
    	Record.setTimestamp(Timestamp);
    	
    	Record.setRuleId(RuleId);
    	
    	Record.setServiceId(ServiceId);
    	
    	ListIterator<ArtifactInstance> ListOfArtifactInstanceIterator =ListOfArtifactInstance.listIterator();
    	
    	while(ListOfArtifactInstanceIterator.hasNext())
    	{
    		ArtifactInstance CurrentArtifact = ListOfArtifactInstanceIterator.next();
    		
    		Record.setPreArtifacts(this.transformToDOM(CurrentArtifact));
    			
    		
    	}
    	
    	LogOfProcess.addLogRecord(Record);
    	
    	
    	
    	this.writeLogfile(LogOfProcess);
    	
    	
    }
    
    public void UpdateProcessLogRecord(ProcessLog processLog,ArrayList<ArtifactInstance> ListOfArtifactInstance )
    {
    	Integer CurrentProcessLogRecordNumber = processLog.getCurrentLogRecordNumber();
    	
    	LogRecord Record = processLog.getLogRecord(CurrentProcessLogRecordNumber); 
    	
    	ListIterator<ArtifactInstance> ListOfArtifactInstanceIterator =ListOfArtifactInstance.listIterator();
    	
    	while(ListOfArtifactInstanceIterator.hasNext())
    	{
    		ArtifactInstance CurrentArtifact = ListOfArtifactInstanceIterator.next();
    		
    		Record.setPostArtifacts(this.transformToDOM(CurrentArtifact));
    			
    		
    	}
    	
    	
    	
    	
    	this.writeLogfile(processLog);
    	
    }
    
    private void writeLogfile(ProcessLog processLog)
    {
    	
    	try {
    	//why relative path doesn't work
 
    	//this part is location of deployed xml file
    	File logfolder= new File("C:/Users/KanPhd/Dropbox/workplace2/Acpsystem/log");//have to change this as well
    	
    	File logFile = new File("C:/Users/KanPhd/Dropbox/workplace2/Acpsystem/log/"+processLog.getProcessId()+".xml");//have to change this as well
    	    
//    	File processfolder= new File("deployed/process/");//have to change this as well
//    	File deployedfolder = new File("deployed/");//have to change this as well
//    	File processFile = new File("deployed/process/"+InputFile.getName());//have to change this as well
    	
    	
    	    
    	    
    	    if(logfolder.exists()== false)
    	    {
    	
    	    	logfolder.mkdir();
    	    		
    	    }
    	  
    	
    	//save xml file 
	    //create a transformer factory
	    TransformerFactory factory = TransformerFactory.newInstance();
	    //create a new transformer
	    Transformer transformer = factory.newTransformer();
		//format OutPut
	    
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    
	    //create a stream result
	    Result result = new StreamResult(logFile);
	    //create new dom source
	    Source source = new DOMSource(processLog.getCompleteLog());
	    //transform and save to xml file
	    
		transformer.transform(source, result);
		
		
			
			
    	}catch (TransformerConfigurationException e) {
			
    		// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
		catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    
	    //save a process file to process folder
	  
    	
    	
    	
    }
    
    private Element transformToDOM(ArtifactInstance InstanceOfArtifact)
    {
    	
    	
    	   	
    	Element XmlArtifact = doc.createElement(InstanceOfArtifact.getArtifactName());
    	
    	XmlArtifact.setAttribute("id", InstanceOfArtifact.getArtifactId());
    	
    	XmlArtifact.setAttribute("state", InstanceOfArtifact.getCurrentState());
		
    	ArrayList<AttributeInstance> listOfAtt = InstanceOfArtifact.getAttributeList();
		
		ListIterator<AttributeInstance> ListOfAttIterator = listOfAtt.listIterator();
		
		
		while(ListOfAttIterator.hasNext())
		{
			AttributeInstance currentAtt = ListOfAttIterator.next();
			String attName = currentAtt.getAttributeName();
			Element XmlAttribute = doc.createElement(attName);

			if(currentAtt.isEmpty())
			{
				XmlAttribute.setTextContent("null");
				
			}
			else
			{
				try {
					
					XmlAttribute.setTextContent(currentAtt.get(0).toString());
					
				} catch (DOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
				
			XmlArtifact.appendChild(XmlAttribute);
		}
		
    
		
    	return XmlArtifact;
    	
    }

}
