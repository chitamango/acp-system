package com.test;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.context.MessageContext;

public class Mycallback implements AxisCallback {
	
	
	private OMElement response;
	
	

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		
		System.out.println("asynchronous invoking completed");
		
	}

	@Override
	public void onError(Exception e) {
		// TODO Auto-generated method stub
		
		e.printStackTrace();
		
	}

	@Override
	public void onFault(MessageContext MContext) {
		// TODO Auto-generated method stub
		
		System.out.println(MContext);
		
	}

	@Override
	public void onMessage(MessageContext MContext) {
		// TODO Auto-generated method stub
		
	//	System.out.println(MContext.getEnvelope().getBody().getFirstElement());
		
		String namespace = MContext.getEnvelope().getBody().getFirstElement().getNamespace().getNamespaceURI();
		
		//QName returnQname = new QName(namespace, "return");
		
		//System.out.println(MContext.getEnvelope().getBody().getFirstElement());
		
		this.response = MContext.getEnvelope().getBody().getFirstElement();
		
		
		
	}

	public OMElement getResponseMessage()
	{
		
		return this.response;
		
	}
	
}
