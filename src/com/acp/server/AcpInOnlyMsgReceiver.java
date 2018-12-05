package com.acp.server;
/*
 *  @version 1.1
 */

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.rpc.receivers.RPCInOnlyMessageReceiver;

import com.test.eventlistener.EventDispatcher;

public class AcpInOnlyMsgReceiver extends RPCInOnlyMessageReceiver {
	
	private EventDispatcher EDispatcher;
	
	public void invokeBusinessLogic(MessageContext inMessageContext) throws AxisFault
	{
		//access global axis configuration
		AxisConfiguration myCustomAxisConfiguration =  inMessageContext.getConfigurationContext().getAxisConfiguration();
		//receive AcpMessage Receiver
		AcpInOnlyMsgReceiver AcpInOnlyReceiver = (AcpInOnlyMsgReceiver) myCustomAxisConfiguration.getMessageReceiver("http://www.w3.org/2004/09/wsdl/in-only");
	       
      	//add event dispatcher from global config to AcpReceiver 
      	 this.setEventDispatcher(AcpInOnlyReceiver.getEventDispathcer());
      	 
     	
		
     	SOAPEnvelope InMessageEnvelope = inMessageContext.getEnvelope();
     		
		
     	
     	SOAPBody InMessageBody = InMessageEnvelope.getBody();
     	
	
     	OMElement SoapAction = InMessageBody.getFirstElement();
     		
		EDispatcher.eventReceived(inMessageContext, SoapAction); 
	
	
	
	}
	
	public void setEventDispatcher(EventDispatcher eventDispatcher)
	{
		
		EDispatcher = eventDispatcher;
		
		
	}
	
	public EventDispatcher getEventDispathcer()
	{
		return EDispatcher;
		
		
	}

}
