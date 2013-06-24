package com.test;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import com.acp.controllers.AcpProcessController;
import com.acp.controllers.MonitorObject;
import com.acp.controllers.SharedArtifactClientManager;
import com.acp.events.MessageEvent;
import com.acp.instance.ProcessInstance;
import com.acp.listeners.MessageEventListener;
import com.acp.mapper.Mapper;
import com.acp.rule.AcpRuleImpl;
import com.test.eventlistener.EventDispatcher;
import com.test.eventlistener.EventListenerImpl;

public class testClientManager {

	/**
	 * @param args
	 */
	
	public static MessageEventListener listener;
	  public static EventDispatcher eDispatcher;
	  public static SharedArtifactClientManager ClientManager;
	  public static AcpProcessController ProcessController;
	  public static MonitorObject Monitor;
	  public static AcpRuleImpl RuleEngine;
	  
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

	/*	listener = new EventListenerImpl();
        
    	eDispatcher = new EventDispatcher();
    	
    	RuleEngine = new AcpRuleImpl();
    	
    	ClientManager = new SharedArtifactClientManager();
    	
    	ProcessController = new AcpProcessController();
    	
    	Monitor = new MonitorObject();
    	
    	
    	
    	listener.setProcessController(ProcessController);   	
		listener.setRuleEngine(RuleEngine);
		
		//initial client manager
		ClientManager.setMonitor(Monitor);
		ClientManager.setProcessController(ProcessController);
		ClientManager.setRuleEngine(RuleEngine);
		ClientManager.setMapper(ProcessController.getMapper());
		
		//initial process controller
		ProcessController.setClientManager(ClientManager);
		ProcessController.setMonitor(Monitor);
		
		//initial EventDispatcher
    	eDispatcher.addEventListener(listener);
    	eDispatcher.setProcessController(ProcessController);
    	
    	*/
    /*	OMFactory fac = OMAbstractFactory.getOMFactory();
    	
    	
    	//payload
    	
    	OMNamespace omNs = fac.createOMNamespace("http://payment.test", "");
		OMElement method = fac.createOMElement("makePayment", omNs);
		OMElement customername = fac.createOMElement("customer_name", omNs);
		OMElement customeraddress = fac.createOMElement("grand_total", omNs);
		customername.setText("john smith");
		customeraddress.setText("1000");
		method.addChild(customername);
		method.addChild(customeraddress);
    	
    	
    	
    	//notification message
		//create the elements
		
		OMElement coordinationMessageElement = fac.createOMElement("coordinationMessage", omNs);
		OMElement coordinationIdElement = fac.createOMElement("coordinateId", omNs);
		OMElement RoleElement = fac.createOMElement("role", omNs);
		OMElement RuleIdElement = fac.createOMElement("ruleId", omNs);   //should have rolename as well
		OMElement PayloadElement = fac.createOMElement("messagePayload", omNs);
		
		//assign value
		coordinationIdElement.setText("c1");
		RoleElement.setText("banker");
		RuleIdElement.setText("rule1");
		PayloadElement.addChild(method);
		
		//build the complete coordinationmessage
		coordinationMessageElement.addChild(coordinationIdElement);
		coordinationMessageElement.addChild(RoleElement);
		coordinationMessageElement.addChild(RuleIdElement);
		coordinationMessageElement.addChild(PayloadElement);
		
		
		
		//instantiate our component
        listener = new EventListenerImpl();
    	eDispatcher = new EventDispatcher();
    	RuleEngine = new AcpRuleImpl();
    	ClientManager = new SharedArtifactClientManager();
    	ProcessController = new AcpProcessController();
    	Monitor = new MonitorObject();
    	
    	
    	//initial EventListener Implement class
    	listener.setProcessController(ProcessController);   	
		listener.setRuleEngine(RuleEngine);
		
		//initial client manager
		ClientManager.setMonitor(Monitor);
		ClientManager.setProcessController(ProcessController);
		ClientManager.setRuleEngine(RuleEngine);
		ClientManager.setMapper(ProcessController.getMapper());
		
		//initial process controller
		ProcessController.setClientManager(ClientManager);
		ProcessController.setMonitor(Monitor);
		
		//initial EventDispatcher
    	eDispatcher.addEventListener(listener);
    	eDispatcher.setProcessController(ProcessController);
		
		
    	System.out.println(ClientManager.recieverReplyCoodinationMessage(coordinationMessageElement));
    	

	/*	URL url = new URL("http://localhost:8080/axis2/services/SharedArtifactClientManagerService?wsdl");	
			
		QName serviceName = new QName("http://webservice.acp.com", "SharedArtifactClientManagerService");
		
		QName operationName = new QName("http://webservice.acp.com", "invokePartnerRole");
		
		ServiceClient client = new ServiceClient(null, url, serviceName, "SharedArtifactClientManagerServiceHttpSoap11Endpoint");
		
		OMElement response;
		
		Mycallback callback = new Mycallback();
		
		response = client.sendReceive(operationName, coordinationMessageElement);
			
		   
		 //Wait till the callback receives the response.
		   while (callback.getResponseMessage() == null) {
		       
				Thread.sleep(1000);
			
		   }
				
			//response = client.sendReceive(operationName, method);
				
				//client.sendRobust(method);
				
		   //     System.out.println("test");
		        
		        
		  //  response = callback.getResponseMessage();
		    
		   
		        
			System.out.println(response);
			
			client.cleanupTransport();
		       
		       //client.cleanup();
		         
		        
		        */
		
/*		
	
		 try{
			
			OMElement NotifacationMessage =  ClientManager.recieverReplyCoodinationMessage(coordinationMessageElement);
			
			System.out.println(NotifacationMessage);
			
			
		
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
		
    //	System.out.println("done");
    	

    	
//payload2
			
			Thread.sleep(5000);
			
			
    	
    	OMFactory fac2 = OMAbstractFactory.getOMFactory();
    	
    	OMNamespace omNs1 = fac2.createOMNamespace("http://online.test", "");
		OMElement method1 = fac2.createOMElement("addItem", omNs1);
		OMElement Item = fac2.createOMElement("item", omNs1);
		OMElement Quantity = fac2.createOMElement("quantity", omNs1);
		Item.setText("iphone_5_32G");
		Quantity.setText("1");
		method1.addChild(Item);
		method1.addChild(Quantity);
    	
    	
    	
    	//notification message
		//create the elements
		
		
		OMElement coordinationMessageElement2 = fac2.createOMElement("coordinationMessage", omNs);
		OMElement coordinationIdElement2 = fac2.createOMElement("coordinateId", omNs);
		OMElement RoleElement2 = fac2.createOMElement("role", omNs);
		OMElement RuleIdElement2 = fac2.createOMElement("ruleId", omNs);   //should have rolename as well
		OMElement PayloadElement2 = fac2.createOMElement("messagePayload", omNs);
		
		//assign value
		coordinationIdElement2.setText("c1");
		RoleElement2.setText("seller");
		RuleIdElement2.setText("rule2");
		PayloadElement2.addChild(method1);
		
		//build the complete coordinationmessage
		coordinationMessageElement2.addChild(coordinationIdElement2);
		coordinationMessageElement2.addChild(RoleElement2);
		coordinationMessageElement2.addChild(RuleIdElement2);
		coordinationMessageElement2.addChild(PayloadElement2);
    	
		
		URL url2 = new URL("http://localhost:8080/axis2/services/SharedArtifactClientManagerService?wsdl");	
		
		QName serviceName2 = new QName("http://webservice.acp.com", "SharedArtifactClientManagerService");
		
		QName operationName2 = new QName("http://webservice.acp.com", "invokePartnerRole");
		
		ServiceClient client2 = new ServiceClient(null, url2, serviceName2, "SharedArtifactClientManagerServiceHttpSoap11Endpoint");
		
		
		Mycallback callback2 = new Mycallback();
		
		
		client2.sendReceiveNonBlocking(operationName2, coordinationMessageElement2, callback2);
		
		   
		 //Wait till the callback receives the response.
		   while (callback2.getResponseMessage() == null) {
		       
				Thread.sleep(1000);
			
		   }
				
			//response = client.sendReceive(operationName, method);
				
				//client.sendRobust(method);
				
		   //     System.out.println("test");
		        
		        
		    response = callback2.getResponseMessage();
		    
		   
		        
			System.out.println(response);
		
	//	System.out.println(coordinationMessageElement2);
    	
	
/*		
		try {
			
			OMElement NotifacationMessage2 =  ClientManager.recieverReplyCoodinationMessage(coordinationMessageElement2);
			
			System.out.println(NotifacationMessage2);
			
			
			
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    //payload3
		OMFactory fac3 = OMAbstractFactory.getOMFactory();
    	
    	OMNamespace omNs2 = fac2.createOMNamespace("http://online.test", "");
		OMElement method2 = fac2.createOMElement("addItem", omNs2);
		OMElement Item2 = fac2.createOMElement("item", omNs2);
		OMElement Quantity2 = fac2.createOMElement("quantity", omNs2);
		OMElement total = fac2.createOMElement("total", omNs2);
		Item2.setText("iphone_5_16G");
		Quantity2.setText("2");
		total.setText("899");
		method2.addChild(Item2);
		method2.addChild(Quantity2);
		//method2.addChild(total);
    	
    	
    	//notification message
		//create the elements
		
		
		OMElement coordinationMessageElement3 = fac3.createOMElement("coordinationMessage", omNs2);
		OMElement coordinationIdElement3 = fac3.createOMElement("coordinateId", omNs2);
		OMElement RoleElement3 = fac2.createOMElement("role", omNs2);
		OMElement RuleIdElement3 = fac2.createOMElement("ruleId", omNs2);   //should have rolename as well
		OMElement PayloadElement3 = fac2.createOMElement("messagePayload", omNs2);
		
		//assign value
		coordinationIdElement3.setText("c1");
		RoleElement3.setText("seller");
		RuleIdElement3.setText("rule3");
		PayloadElement3.addChild(method2);
		
		//build the complete coordinationmessage
		coordinationMessageElement3.addChild(coordinationIdElement3);
		coordinationMessageElement3.addChild(RoleElement3);
		coordinationMessageElement3.addChild(RuleIdElement3);
		coordinationMessageElement3.addChild(PayloadElement3);
    	
	//	System.out.println(coordinationMessageElement3);
		
		try {
			
			OMElement NotifacationMessage3 =  ClientManager.recieverReplyCoodinationMessage(coordinationMessageElement3);
			
			System.out.println(NotifacationMessage3);
			
			
			
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    */	
		
		OMFactory fac = OMAbstractFactory.getOMFactory();
    	
    	
    	//payload
    	
    	OMNamespace omNs = fac.createOMNamespace("http://online.test", "");
    	OMElement method = fac.createOMElement("addItem", omNs);
		OMElement customername = fac.createOMElement("item", omNs);
		OMElement customeraddress = fac.createOMElement("quantity", omNs);
		OMElement processId = fac.createOMElement("processid", omNs);
		customername.setText("Iphone");
		customeraddress.setText("1");
		processId.setText("order_process-P1");
		method.addChild(customername);
		method.addChild(customeraddress);
		method.addChild(processId);
		
		URL url = new URL("http://localhost:8080/axis2/services/OnlineOrderService?wsdl");	
		
		QName serviceName = new QName("http://online.test", "OnlineOrderService");
		
		QName operationName = new QName("http://online.test", "addItem");
		
		ServiceClient client = new ServiceClient(null, url, serviceName,"OnlineOrderServiceHttpSoap11Endpoint");
		
		System.out.println(client.sendReceive(operationName, method));
		
		
	}

}
