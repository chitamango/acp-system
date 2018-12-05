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

import com.acp.controllers.SharedArtifactClientManager;



public class ClientManagerMsgReceiver extends RPCMessageReceiver {

	private SharedArtifactClientManager ClientManager;
	
	
	
	//our target is to return at least processId

	public void invokeBusinessLogic(MessageContext inMessageContext,
            MessageContext outMessageContext) throws AxisFault
	{
		
		OMElement OutputElement = null;
	
		System.out.println("message in");
		//access global axis configuration
		AxisConfiguration myCustomAxisConfiguration =  inMessageContext.getConfigurationContext().getAxisConfiguration();
		//receive ClientManagerMessageReceiver
		ClientManagerMsgReceiver ClientManagerReceiver = (ClientManagerMsgReceiver) myCustomAxisConfiguration.getMessageReceiver("http://www.w3.org/2004/10/wsdl/in-out");
	       
      	//setSharedArtifactClientManager
		this.setSharedArtifactClientManager(ClientManagerReceiver.getSharedArtifactClientManager());
		
		//get Soap Envelop	
     	SOAPEnvelope InMessageEnvelope = inMessageContext.getEnvelope();
     		
		
     	//get Body
     	SOAPBody InMessageBody = InMessageEnvelope.getBody();
     	
     	//getCoordination Message
     	OMElement SoapAction = InMessageBody.getFirstElement();
     	
     	try {
     		
     		OutputElement = ClientManager.recieverReplyCoodinationMessage(SoapAction);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("message out");
		
		//this part to create envelop and return Notifacation Message
		SOAPFactory fac = OMAbstractFactory.getSOAP11Factory();
		
		SOAPEnvelope envelope = fac.getDefaultEnvelope();
		
		//envelope.getBody().addChild(SoapAction );
		
		envelope.getBody().addChild(OutputElement );
		
		outMessageContext.setEnvelope(envelope);
		
		//outMessageContext.setEnvelope(envelope);
		
		
		
	}
	
	public void setSharedArtifactClientManager(SharedArtifactClientManager ClientManager)
	{
		
		this.ClientManager = ClientManager;
		
		
	}
	
	public SharedArtifactClientManager getSharedArtifactClientManager()
	{
		return this.ClientManager;
		
		
	}
	
	
	
	
}
