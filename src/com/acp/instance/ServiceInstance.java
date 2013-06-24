package com.acp.instance;




import org.apache.axiom.om.OMElement;
import com.acp.process.ServiceType;

public class ServiceInstance {
	
	private String ServiceID;
	private String location;
	private String InputMessageName;
	private String OutputMessageName;
	private String ServiceName;
	private String NameSpace;
	private String Operation;
	private String Port;
	
	//should field for the axiom model
	private OMElement SOAPinputMessage;
	private OMElement SOAPoutputMessage;

	
	
	public ServiceInstance(ServiceType xmlBindingService)
	{
		
		
		this.location = xmlBindingService.getLocation();
		this.InputMessageName = xmlBindingService.getInputMessage();
		this.OutputMessageName = xmlBindingService.getOutputMessage();
		this.Operation = xmlBindingService.getOperation();
		this.Port = xmlBindingService.getPort();
		this.NameSpace = xmlBindingService.getNamespace();
		this.ServiceName = xmlBindingService.getName();
		
		
	}
	
	// have to study a bit 
	
	//method to call service as well as field to contain input message and output message
	
	public String getWsdlLocation()
	{
		
		
		return this.location;
		
	}
	
	public String getInputMessageName()
	{
		
		
		return this.InputMessageName;
		
	}
	
	public String getOutoutMessageName()
	{
		
		return this.OutputMessageName;
		
		
	}
	
	
	public String getOperation()
	{
		
		
		return this.Operation;
	}
	
	public String getNamespace()
	{
		
		return this.NameSpace;
		
	}
	
	public String getPort()
	{
		return this.Port;
		
	}
	
	public String getServiceName()
	{
		return this.ServiceName;
		
		
	}
	
	public OMElement getSoapInputMessage()
	{
		
		return this.SOAPinputMessage;
	}
	
	public void setSoapInputMessage(OMElement InputMessage )
	{
		
		this.SOAPinputMessage = InputMessage;
		
	}
	
	public OMElement getSoapOutputMessage()
	{
		
		return this.SOAPoutputMessage;
	}
	
	public void setSoapOutputMessage(OMElement OutputMessage)
	{
		
		this.SOAPoutputMessage = OutputMessage;
		
	}
	
	public String getServiceID()
	{
		return this.ServiceID;
		
	}
	
	public void setServiceID(String IdOfService)
	{
		
		this.ServiceID = IdOfService;
		
	}
	

}
