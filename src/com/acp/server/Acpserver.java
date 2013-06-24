package com.acp.server;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.transport.http.SimpleHTTPServer;
import com.acp.controllers.AcpProcessController;
import com.acp.controllers.MonitorObject;
import com.acp.controllers.SharedArtifactClientManager;
import com.acp.listeners.MessageEventListener;
import com.acp.rule.AcpRuleImpl;
import com.test.eventlistener.EventDispatcher;
import com.test.eventlistener.EventListenerImpl;

public class Acpserver extends SimpleHTTPServer {

	/**
	 * @param args
	 */
	
	public static MessageEventListener listener;
	public static EventDispatcher eDispatcher;
	public static SharedArtifactClientManager ClientManager;
	public static AcpProcessController ProcessController;
	public static MonitorObject Monitor;
	public static AcpRuleImpl RuleEngine;
	public static final String AXIS2_REPOSITORY = "repository";
	public static final String AXIS2_configuration = "conf/axis2.xml";
	
	
	public Acpserver(ConfigurationContext confContext, int i) throws AxisFault {
		// TODO Auto-generated constructor stub
		
		super(confContext,i);
		
		
	}

	public void start()
	{
		try {
			
				
			
				super.start();
			
			 	System.out.println("Server started on port 8080 ");
	            
	            System.out.println("initialize Event listener");
	            
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
	    		ProcessController.setEventDispatcher(eDispatcher);
	    		//initial EventDispatcher
	        	eDispatcher.addEventListener(listener);
	        	eDispatcher.setProcessController(ProcessController);
	        	
	        	//get AxixConfiguration
	        	AxisConfiguration myCustomAxisConfiguration = super.configurationContext.getAxisConfiguration();
				//retreive AcpReceiver
	        	 AcpMsgReceiver AcpReceiver =  (AcpMsgReceiver) myCustomAxisConfiguration.getMessageReceiver("http://www.w3.org/2004/09/wsdl/in-out");
	            //initial AcpReceiver 	
	        	 AcpReceiver.setEventDispatcher(eDispatcher);
	        	
	        	 //retreive ClientReceiver
	        	 ClientManagerMsgReceiver ClientManagerReceiver = (ClientManagerMsgReceiver) myCustomAxisConfiguration.getMessageReceiver("http://www.w3.org/2004/10/wsdl/in-out");
	        	 
	        	 //intitial ClientReceiver
	        	 ClientManagerReceiver.setSharedArtifactClientManager(ClientManager);

	        	//retreive AcpReceiver
	        	 AcpInOnlyMsgReceiver AcpInOnlyReceiver =  (AcpInOnlyMsgReceiver) myCustomAxisConfiguration.getMessageReceiver("http://www.w3.org/2004/09/wsdl/in-only");
	            //initial AcpReceiver 	
	        	 AcpInOnlyReceiver.setEventDispatcher(eDispatcher);
				
		
			
			try {
                Thread.sleep(2000000);
            } catch (InterruptedException e) {
            	
            	
            }
			
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		 try {
				ConfigurationContext confContext =
				     ConfigurationContextFactory.createConfigurationContextFromFileSystem(
				    		 AXIS2_REPOSITORY, AXIS2_configuration);
				
					
				 //change server port here
					new Acpserver(confContext, 8080).start();
					
				
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		
		
		
		

	}

}
