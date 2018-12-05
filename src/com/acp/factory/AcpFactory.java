package com.acp.factory;
/*
 *  @version 1.1
 */


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.acp.instance.ArtifactInstance;
import com.acp.instance.ProcessInstance;
import com.acp.instance.RuleInstance;
import com.acp.instance.ServiceInstance;
import com.acp.process.ArtifactType;
import com.acp.process.ArtifactsType;
import com.acp.process.BusinessRulesType;
import com.acp.process.ObjectFactory;
import com.acp.process.Process;
import com.acp.process.RuleType;
import com.acp.process.ServiceType;
import com.acp.process.ServicesType;



public class AcpFactory {
	
	private JAXBContext  jaxbContext;
	private  Unmarshaller unmarshaller;
	
	/**
	 * 
	 * Constructor method
	 * 
	 */
	
	public AcpFactory()
	{
		
		try {
			jaxbContext = JAXBContext.newInstance("com.acp.process");
			unmarshaller = jaxbContext.createUnmarshaller();
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			
	}
	
	/**
	 * 
	 * this method return a complete Process Instance
	 * 
	 * @param istrm
	 * @return
	 * @throws JAXBException 
	 */

	private Process  getXmlBindingProcess(File ProcessLocation) 
	{
		
	

			Process ProcessInstance = null;
			
			try {
				
			
				
				if(ProcessInstance == null)
				{
					
					Object obj = unmarshaller.unmarshal(ProcessLocation) ;
			
					
					 ProcessInstance = (Process)obj;
							
										
				}
					
						
				
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return ProcessInstance;	
								
	}
	/**
	 * 
	 * 
	 * get process file location using xml file
	 * 
	 * 
	 * @param ProcessName
	 * @return
	 * @throws Exception 
	 */
	
	private File getProcessLocation(String ProcessName) throws Exception
	{
		
		File ProcessLocation = null;
	File registerProcessFile = new File("definition/deployedprocess.xml");
		
	//	File registerProcessFile = new File("deployed/deployedprocess.xml");
		if (registerProcessFile.exists()== false)
		{
			throw new Exception("Acp Factory Error: Please deploy process first");
		
			
		}
		
	    try {
	    	
	    	   	
	    	DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		    domFactory.setNamespaceAware(true); // never forget this
		    DocumentBuilder Dbuilder = domFactory.newDocumentBuilder();
	    	Document doc = Dbuilder.parse(registerProcessFile);
	    	
	    	NodeList ProcessList = doc.getElementsByTagName("process");
	    	
	    	for(int i=0; i<ProcessList.getLength();i++)
	    	{
	    		String registerProcessName = ProcessList.item(i).getFirstChild().getTextContent();
	    		
	    		if(ProcessName.equalsIgnoreCase(registerProcessName) == true )
	    		{
	    			
	    			String Location = ProcessList.item(i).getLastChild().getTextContent();
	    			
	    			ProcessLocation = new File(Location);
	    			
	    			break;
	    						
	    		}
	    		    			
	    	}
	    	
	    	
	    	   	
	    	
	    	
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return ProcessLocation;
		
		
		
	}
	

	
	/**
	 * 
	 * 
	 * this method will return process instance include a list of rule,service and artifact
	 * @throws Exception 
	 * 
	 * 
	 * 
	 */
	
	public ProcessInstance getProcessInstance(String ProcessName) throws Exception
	{
		//get process instance
		File ProcessLocation = getProcessLocation(ProcessName);
		Process CompleteProcessInstance = getXmlBindingProcess(ProcessLocation);
		Process XmlBindingProcess = null;
		ProcessInstance returnProcessInstance = null;
		
		//break complete processInstance into piece
		
		CompleteProcessInstance.getBusinessRules().getRule().clear();
		CompleteProcessInstance.getServices().getService().clear();
		CompleteProcessInstance.getArtifacts().getArtifact().clear();
		
		
		XmlBindingProcess = CompleteProcessInstance;
		
		returnProcessInstance = new ProcessInstance(XmlBindingProcess);
		
		//create new  instance here and populate with its data	
		
		return returnProcessInstance;
		
		
	}
	/**
	 * 
	 *  get artifact instance by processName and artifactName
	 * 
	 * @return
	 * @throws Exception 
	 */
	
	public ArtifactInstance getArtifactInstance(String ProcessName, String ArtifactName) throws Exception
	{
		
		//get process instance
		File ProcessLocation = getProcessLocation(ProcessName);
		Process CompleteProcessInstance = getXmlBindingProcess(ProcessLocation);
		ArtifactType xmlbindingArtifact = null;
		ArtifactInstance returnArtifactInstance = null;
		
		List<ArtifactType> ArtifactList = CompleteProcessInstance.getArtifacts().getArtifact();
		
		Iterator<ArtifactType> Aitr =   ArtifactList.iterator();
		
		while(Aitr.hasNext())
		{
			ArtifactType InspectedArtifact = Aitr.next();
			String InspectedArtifactName = InspectedArtifact.getName();
			
			if(InspectedArtifactName.equalsIgnoreCase(ArtifactName))
			{
				Integer removeIndex = CompleteProcessInstance.getArtifacts().getArtifact().indexOf(InspectedArtifact);
				
				xmlbindingArtifact = InspectedArtifact;
				
				CompleteProcessInstance.getArtifacts().getArtifact().remove(removeIndex);
				
				
				break;
				
			}
			
		
		}
		
		returnArtifactInstance = new ArtifactInstance(xmlbindingArtifact); 
		
		//create new  instance here and populate with its data	
		return returnArtifactInstance;
				
	}
	
	
	public RuleInstance getRuleInstance(String ProcessName, String RuleName) throws Exception
	{
		
		//get process instance
		File ProcessLocation = getProcessLocation(ProcessName);
		Process CompleteProcessInstance = getXmlBindingProcess(ProcessLocation);
		RuleType xmlBindingRule= null;
		RuleInstance returnRule= null;
		
		List<RuleType> RuleList = CompleteProcessInstance.getBusinessRules().getRule();
		
		Iterator<RuleType> Ritr =   RuleList.iterator();
		
		while(Ritr.hasNext())
		{
			RuleType InspectedRule = Ritr.next();
			String InspectedRuleName = InspectedRule.getName();
			
			if(InspectedRuleName.equalsIgnoreCase(RuleName))
			{
				Integer removeIndex = CompleteProcessInstance.getBusinessRules().getRule().indexOf(InspectedRule);
				
				xmlBindingRule = InspectedRule;
				
				CompleteProcessInstance.getBusinessRules().getRule().remove(removeIndex);
				
				
				break;
				
			}
			
		
		}
		
		returnRule = new RuleInstance(xmlBindingRule);
		
		//create new  instance here and populate with its data		
		return returnRule;
				
	}
	
	public ServiceInstance getServiceInstance(String ProcessName, String ServiceName, String OprerationName) throws Exception
	{
		
		//get process instance
		File ProcessLocation = getProcessLocation(ProcessName);
		Process CompleteProcessInstance = getXmlBindingProcess(ProcessLocation);
		ServiceType xmlBindingService = null;
		ServiceInstance returnService = null;
		
		List<ServiceType> ServiceList = CompleteProcessInstance.getServices().getService();
		
		Iterator<ServiceType> Sitr =   ServiceList.iterator();
		
		while(Sitr.hasNext())
		{
			ServiceType InspectedService = Sitr.next();
			String InspectedServiceName = InspectedService.getName();
			String InspectedServiceOperation = InspectedService.getOperation();
			
			if(InspectedServiceName.equalsIgnoreCase(ServiceName) && InspectedServiceOperation.equalsIgnoreCase(OprerationName) )
			{
				Integer removeIndex = CompleteProcessInstance.getServices().getService().indexOf(InspectedService);
				
				xmlBindingService = InspectedService;
				
				CompleteProcessInstance.getServices().getService().remove(removeIndex);
				
				
				break;
				
			}
			
		
		}
		
		returnService = new ServiceInstance(xmlBindingService);
		
		//create new  instance here and populate with its data		
		return returnService;
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

