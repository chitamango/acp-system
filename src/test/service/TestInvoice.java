package test.service;
/*
 *  @version 1.1
 */

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
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMDocument;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class TestInvoice {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ServiceClient client = null;
		
		OMElement SOAPinputMessage = null;
		OMElement SOAPoutputMessage = null;
		
		
	
		
		
		try {
			client = new ServiceClient(null, new URL("http://localhost:8080/PaymentService/services/PaymentService?wsdl"),new QName("http://payment.test","PaymentService"),"PaymentServiceHttpSoap11Endpoint");
		} catch (AxisFault e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		try {
			
			SOAPinputMessage = CreatePayload();
			
		
			//should have while loop here
			
		
			
			
		} catch (WSDLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
       try {
    	   
		SOAPoutputMessage = client.sendReceive(new QName("http://payment.test","checkBills"), SOAPinputMessage);
		
	/*	XMLStreamReader Reader = SOAPoutputMessage.getXMLStreamReaderWithoutCaching();
		
		StAXOMBuilder builder = new StAXOMBuilder( OMAbstractFactory.getOMFactory(), Reader); 
		
		OMElement documentElement = builder.getDocumentElement();
		
		*/
		
		// XMLStreamWriter writer =XMLOutputFactory.newInstance().createXMLStreamWriter(System.out);
		 
	//	 documentElement.serialize(writer);
		
		SOAPoutputMessage.build();
		
		SOAPoutputMessage.detach();
		
		System.out.println(SOAPoutputMessage);
		
		 
		 
		 
		 
		
		
		
		
       } catch (AxisFault e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	
       //add output to service instance as well 
	} catch (FactoryConfigurationError e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       
     
		
		
		
		
		
		
	}
	
	private static OMElement CreatePayload() throws  FactoryConfigurationError, WSDLException
	{
		
    	OMFactory fac = OMAbstractFactory.getOMFactory();
    	OMNamespace omNs = fac.createOMNamespace("http://payment.test", "ns");
    	WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
    	OMElement PayLoad = null;
    	Node targetNode = null;
    
    	
    	// read wsdl
    	Definition wsdldefinition = reader.readWSDL("http://localhost:8080/PaymentService/services/PaymentService?wsdl");
    	
    	//get types element
    	Types typedefinition = wsdldefinition.getTypes();
    	
    	 QName WSDLMessageName = new QName("http://payment.test","checkBillsRequest"); //message name  here
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
    	 
    	QName PartQName = new QName("http://payment.test","GrandTotal");
		
		OMElement orderid = Method.getFirstChildWithName(PartQName);
    		
		orderid.setText( "1000");
		
	
		
		PayLoad = Method;
    	
    	
    	 
    	return PayLoad;
	        
	       
		
	}

}
