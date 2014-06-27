package com.acp.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.Part;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.acp.instance.ArtifactInstance;
import com.acp.instance.ProcessInstance;
import com.acp.instance.RuleInstance;
import com.acp.instance.ServiceInstance;
import com.acp.mapper.Mapper;

public class ServiceController {
	
	private Mapper mapper ;
	private ArrayList<ServiceClient> ListOfServiceClient = new ArrayList<ServiceClient>();
	

	public ServiceController(Mapper DataMapper)
	{	
		
		mapper = DataMapper;
		
	}
	/*
	 * 
	 * Method to invoke a web service to process Artifact data
	 * (we added userMessage as input as well)
	 */
	
	
	public void invokeService(ArrayList<ArtifactInstance> Artifacts,OMElement userMessage,String ServiceName,String OperationName, RuleInstance Rule, ProcessInstance Process, ServiceInstance Service) throws AxisFault, MalformedURLException, XMLStreamException, FactoryConfigurationError
	{
		//now i have to fixed it to map data from both message from user and artifact data
		ServiceClient client = null;
		ServiceInstance InvokedService;
		OMElement SOAPinputMessage = null;
		OMElement SOAPoutputMessage = null;
		ArrayList<ArtifactInstance> ListofArtifactInProcessInstance = Process.getArtifactInstanceList();
		ListIterator<ArtifactInstance> itr = Artifacts.listIterator();
		
		InvokedService= Service;
	//	InvokedService = Process.getServiceInstance(ServiceName,OperationName);
		
		System.out.println(userMessage);
		
		System.out.println(InvokedService.getServiceName()+":"+ InvokedService.getOperation());
		
		if(this.checkServiceClient(ServiceName) == null)
		{
		
		client = new ServiceClient(null, new URL(InvokedService.getWsdlLocation()),new QName(InvokedService.getNamespace(),InvokedService.getServiceName()),InvokedService.getPort());
		this.ListOfServiceClient.add(client);
		System.out.println("Success createServiceClient");
		
		}
		else
		{
			
			
			client = this.checkServiceClient(ServiceName);
			
			
		}
		
		try {
			
			SOAPinputMessage = this.CreatePayload(InvokedService);
			
			//System.out.println(SOAPinputMessage);
			//should have while loop here
			// should be something here
			while(itr.hasNext())
			{
				ArtifactInstance CurrentArtifact = itr.next();
				
				//why i have to do this?? should check id??
				if(ListofArtifactInProcessInstance.contains(CurrentArtifact))
				{
						//this is way i have to fix it
					try {
						this.mapper.mapppingArtifactToMessage(Rule, CurrentArtifact, SOAPinputMessage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}
					//need some condition
					
				
				}		
				
			}
			//System.out.println(SOAPinputMessage);
			
			if(userMessage != null)	
			{
				
					try {
						this.mapper.mappingMessageToMessage(Rule, userMessage, SOAPinputMessage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
			}
		
		
			
			
			InvokedService.setSoapInputMessage(SOAPinputMessage);
			
			System.out.println(SOAPinputMessage);
			System.out.println("Success createPayload");
		
			
		} catch (WSDLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
       SOAPoutputMessage = client.sendReceive(new QName(InvokedService.getNamespace(),InvokedService.getOperation()), SOAPinputMessage);
       
       System.out.println(SOAPoutputMessage);
       
       //this to prevent Axis2 timeout error
       client.cleanupTransport();
       
    //   client.cleanup();
     //add output to service instance as well 
       InvokedService.setSoapOutputMessage(SOAPoutputMessage);
       
	}
	
	private OMElement CreatePayload(ServiceInstance Service) throws  FactoryConfigurationError, WSDLException
	{
		
    	OMFactory fac = OMAbstractFactory.getOMFactory();
    	OMNamespace omNs = fac.createOMNamespace(Service.getNamespace(), "");
    	WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
    	OMElement PayLoad = null;
    	Node targetNode = null;
    
    	
    	// read wsdl
    	Definition wsdldefinition = reader.readWSDL(Service.getWsdlLocation());
    	
    	//get types element
    	Types typedefinition = wsdldefinition.getTypes();
    	
    	 QName WSDLMessageName = new QName(Service.getNamespace(),Service.getInputMessageName()); //message name  here
    	//get message element with a particular name
    	Message MessageDefinition = wsdldefinition.getMessage(WSDLMessageName);
    	
    	// get key to access each part in a wsdl message and use to loop
    	 Map<String, Part> parts =  MessageDefinition.getParts();
    	 
    	 Set<String> Keyset = parts.keySet();
    	 
    	 Iterator<String> itr = Keyset.iterator();
    	 
    	 while(itr.hasNext())
    	 {
    		 String keyname = itr.next();
    		 
    		 Part part = parts.get(keyname);
    		 
    		String PartName = part.getName(); //this will be used in the future
    		
    		QName PartType = part.getTypeName();// this will be used in the future
    		
    		//use this determine the complex type of xml schema
    		QName PartElement = part.getElementName();
    		
    		//System.out.println(PartElement);
    				
        	//use this method to access schema in wdsl4j
        	for( Object o : typedefinition.getExtensibilityElements()) {
        	    if( o instanceof javax.wsdl.extensions.schema.Schema ) 
        	    {
        	        Element elt = ((javax.wsdl.extensions.schema.Schema) o).getElement();
     
        	        	NodeList ListOfNode = elt.getChildNodes();
        	        	
        	        	for(int i = 0;i < ListOfNode.getLength(); i++ )
        	        	{
        	        		Node CurrentNode = ListOfNode.item(i);
        	        		//some hacking to overcome strange text element
        	        		if(CurrentNode.hasChildNodes())
        	        		{
        	        			if(CurrentNode.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase(PartElement.getLocalPart()))
        	        			{
        	        				
        	        				
        	        				targetNode = CurrentNode;
        	        				
        	        				
        	        			}
	        			
        	        		}
		
        	        		
        	        	}
        	       
        	        
        	    }
        	
    	
        	}
        	
    	 }
    	 
    	//the message should be generated in the manner that restricted by wsdl
        
    	OMElement Method = fac.createOMElement(targetNode.getAttributes().getNamedItem("name").getNodeValue(),omNs );
    	
    	NodeList ListOfchild= targetNode.getChildNodes();
 	 	
    	for(int i = 0; i < ListOfchild.getLength(); i++)
    	{
    		//hacking to remove #text
    		if(ListOfchild.item(i).hasChildNodes())
    		{	
    			NodeList ComplexType = ListOfchild.item(i).getChildNodes();
    							
    			for(int I = 0; I < ComplexType.getLength(); I++)
    	    	{	
    				//hacking to remove #text
    				if(ComplexType.item(I).hasChildNodes())
    				{
    					NodeList ListOfParameter =  ComplexType.item(I).getChildNodes();
    						
    					for(int l = 0; l < ListOfParameter.getLength(); l++ )
    					{
    						if(ListOfParameter.item(l).hasAttributes())
    	    				{
    							String ParameterName =  ListOfParameter.item(l).getAttributes().getNamedItem("name").getNodeValue();
    							OMElement Parameter = fac.createOMElement(ParameterName,omNs );   
    							Method.addChild(Parameter);
    	    				}
    					}
    					
  	
    				}
    	    	}	
    		}
    		
    		
    	}
    	 
    	PayLoad = Method;
    	
    
    	 
    	return PayLoad;
	        
	       
		
	}
	
	private ServiceClient checkServiceClient(String ServiceName)
	{
		ServiceClient ResultServiceClient = null;
		
		ListIterator<ServiceClient> ServiceClientListIterator = this.ListOfServiceClient.listIterator();
		
		while(ServiceClientListIterator.hasNext())
		{
			ServiceClient CurrentServiceClient = ServiceClientListIterator.next();
			
			
			
			String CurrentServiceName = CurrentServiceClient.getAxisService().getName();
			
		
			
			if(CurrentServiceName.equalsIgnoreCase(ServiceName))
			{
				ResultServiceClient =  CurrentServiceClient; 
				
			}
			
			
			
		}
		
		
		return ResultServiceClient;
		
	}
	

}
