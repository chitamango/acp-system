package com.acp.server;
/*
 *  @version 1.1
 */

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.rpc.receivers.RPCMessageReceiver;


import com.test.eventlistener.EventDispatcher;

public class AcpMsgReceiver extends RPCMessageReceiver {

	private EventDispatcher EDispatcher;
	
	
	
	//our target is to return at least processId

	public void invokeBusinessLogic(MessageContext inMessageContext,
            MessageContext outMessageContext) throws AxisFault
	{
		
		
	
		//access global axis configuration
		AxisConfiguration myCustomAxisConfiguration =  inMessageContext.getConfigurationContext().getAxisConfiguration();
		//receive AcpMessage Receiver
		AcpMsgReceiver AcpReceiver = (AcpMsgReceiver) myCustomAxisConfiguration.getMessageReceiver("http://www.w3.org/2004/09/wsdl/in-out");
	       
      	//add event dispatcher from global config to AcpReceiver 
      	 this.setEventDispatcher(AcpReceiver.getEventDispathcer());
      	 
     	
		
     	SOAPEnvelope InMessageEnvelope = inMessageContext.getEnvelope();
     		
		
     	
     	SOAPBody InMessageBody = InMessageEnvelope.getBody();
     	
	
     	OMElement SoapAction = InMessageBody.getFirstElement();
     		
		EDispatcher.eventReceived(inMessageContext, SoapAction); // this should return something so that we can manipulate outgoing message
		
		
		while(EDispatcher.getProcessId() == null)
		{
			
			
			
		}
		
		
		
		SOAPFactory fac = OMAbstractFactory.getSOAP11Factory();
		
		SOAPEnvelope envelope = fac.getDefaultEnvelope();
		
		OMElement processid = fac.createOMElement("process_id", SoapAction.getNamespace());
		
		processid.setText(EDispatcher.getProcessId());

		envelope.getBody().addChild(processid );
		
		outMessageContext.setEnvelope(envelope);
		
		
		
		
		
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
