package com.acp.mapper;
/*
 *  @version 1.1
 */


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

import com.acp.instance.ArtifactInstance;
import com.acp.instance.AttributeInstance;
import com.acp.instance.MapRule.copy;
import com.acp.instance.RuleInstance;
import com.acp.instance.MapRule;
import com.acp.sharingrule.ArtifactType;
import com.acp.sharingrule.AttributeType;
import com.acp.sharingrule.RuleType;



public class Mapper {
	
	private final  String Pattern1 = "RequestToArtifact";
	private final  String Pattern2 = "MessageToArtifact";
	private final  String Pattern3 = "ArtifactToMessage";
	private final String Pattern4 = "MessageToMessage";	
	
	
	
	public void mapppingRequestToArtifact(RuleInstance rule, ArtifactInstance artifact, HttpServletRequest request)throws Exception 
	{	
		
		//1. search for mapType
		ArrayList<MapRule> MapRuleList = rule.getMapRules();
		
		MapRule CurrentMapRule = extractRequestToArtifactMapType(MapRuleList);
		
		//if there is no match for map rule then do nothing
		if(CurrentMapRule != null)
		{
		
		ArrayList<copy> MappingList = CurrentMapRule.getMappingList(); 
		
		ListIterator<copy> itr = MappingList.listIterator();
		
			while(itr.hasNext())
			{
				copy currentCopy = itr.next();
				//this should check whether requirements for mapping are collect or not
				if(currentCopy.getToArtifact().equalsIgnoreCase(artifact.getArtifactName()))
				{
					//map artifact here
					//frompart
					String RequestParameter = request.getParameter(currentCopy.getFromPart());
					//topart
					String TargetAttribute = currentCopy.getToAttribute();
					
					AttributeInstance MappingAtt = artifact.getAttributeInstance(TargetAttribute);
					
					String AttributeType = MappingAtt.getAttributeType();
					String AttributeStructure = MappingAtt.getAttributeStructure();
					
					if(AttributeStructure.equalsIgnoreCase("pair"))
					{
						
						try {
							//MappingAtt.add(RequestParameter);
							
							if(MappingAtt.Size()== 0)
							{
								if(AttributeType.equalsIgnoreCase("string"))
								{	
									MappingAtt.add((String)RequestParameter);
								}
								else if (AttributeType.equalsIgnoreCase("integer"))
								{
									Integer ConvertedRequestParameter = Integer.parseInt(RequestParameter);
									MappingAtt.add(ConvertedRequestParameter);
									
								}
								else if (AttributeType.equalsIgnoreCase("short"))
								{
									Short ConvertedRequestParameter = new Short(RequestParameter);
									MappingAtt.add(ConvertedRequestParameter);
									
								}
								else if (AttributeType.equalsIgnoreCase("double"))
								{
									Double ConvertedRequestParameter = Double.parseDouble(RequestParameter);
									MappingAtt.add(ConvertedRequestParameter);
									
								}
								else if (AttributeType.equalsIgnoreCase("boolean"))
								{
									
									Boolean ConvertedRequestParameter = Boolean.parseBoolean(RequestParameter);
									MappingAtt.add(ConvertedRequestParameter);
									
									
								}
								// the last one should be artifact
								
								
							}
							else
							{
								
								if(AttributeType.equalsIgnoreCase("string"))
								{	
									MappingAtt.set(0, (String)RequestParameter);
								}
								else if (AttributeType.equalsIgnoreCase("integer"))
								{
									Integer ConvertedRequestParameter = Integer.parseInt(RequestParameter);
									MappingAtt.set(0, ConvertedRequestParameter);
									
									
								}
								else if (AttributeType.equalsIgnoreCase("short"))
								{
									Short ConvertedRequestParameter = new Short(RequestParameter);
									MappingAtt.set(0, ConvertedRequestParameter);
									
								}
								else if (AttributeType.equalsIgnoreCase("double"))
								{
									Double ConvertedRequestParameter = Double.parseDouble(RequestParameter);
									MappingAtt.set(0, ConvertedRequestParameter);
									
								}
								else if (AttributeType.equalsIgnoreCase("boolean"))
								{
									
									Boolean ConvertedRequestParameter = Boolean.parseBoolean(RequestParameter);
									MappingAtt.set(0, ConvertedRequestParameter);
									
									
								}
								// the last one should be artifact
							}
						}	
						catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
					else if (AttributeStructure.equalsIgnoreCase("list"))
					{
						
						try {
							
							
							//MappingAtt.add(RequestParameter);
							
							if(AttributeType.equalsIgnoreCase("string"))
							{	
								MappingAtt.add((String)RequestParameter);
							}
							else if (AttributeType.equalsIgnoreCase("integer"))
							{
								Integer ConvertedRequestParameter = Integer.parseInt(RequestParameter);
								MappingAtt.add(ConvertedRequestParameter);
								
							}
							else if (AttributeType.equalsIgnoreCase("short"))
							{
								Short ConvertedRequestParameter = new Short(RequestParameter);
								MappingAtt.add(ConvertedRequestParameter);
								
							}
							else if (AttributeType.equalsIgnoreCase("double"))
							{
								Double ConvertedRequestParameter = Double.parseDouble(RequestParameter);
								MappingAtt.add(ConvertedRequestParameter);
								
							}
							else if (AttributeType.equalsIgnoreCase("boolean"))
							{
								
								Boolean ConvertedRequestParameter = Boolean.parseBoolean(RequestParameter);
								MappingAtt.add(ConvertedRequestParameter);
								
								
							}
							// the last one should be artifact
							
							
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						
					}
					
					
					
					
				}
						
				
			}
		
		
		}
		
		
		
		
		
		
	}
	

	
	//artifact ID?
	public void mapppingMessageToArtifact(RuleInstance rule, ArtifactInstance artifact, OMElement ReponseMessage)throws Exception 
	{
		ArrayList<MapRule> MapRuleList = rule.getMapRules();
		
		MapRule CurrentMapRule = extractMessageToArtifactMapType(MapRuleList);
		//if there is no match for map rule then do nothing
		if(CurrentMapRule != null)
		{
			ArrayList<copy> MappingList = CurrentMapRule.getMappingList(); 
			
			ListIterator<copy> itr = MappingList.listIterator();
			
			// System.out.println("Message in Mapper: "+ReponseMessage);
		
			String NamespaceURI = ReponseMessage.getNamespace().getNamespaceURI();
			
			
			
			while(itr.hasNext())
			{
				copy currentCopy = itr.next();
				
				//check requirement message and artifact 
			//	if(currentCopy.getToArtifact().equalsIgnoreCase(artifact.getArtifactName()) && ReponseMessage.getLocalName().equalsIgnoreCase(currentCopy.getFromMessage())	)
			//	{
					//from part
					QName OutputMessageQName = new QName(NamespaceURI,currentCopy.getFromMessage());
					QName PartQName = new QName(NamespaceURI,currentCopy.getFromPart());
					
					
					//what with return element !! some hacking here
			
					
					OMElement FromPart = ReponseMessage.getFirstElement().getFirstElement().getFirstChildWithName(PartQName);
					
					//OMElement FromPart = ReponseMessage.getFirstChildWithName(PartQName);
					
				
					
					String MessageValue = FromPart.getText();
					
					
					//to part
					String TargetArtifact = currentCopy.getToArtifact();
					String TargetAttribute = currentCopy.getToAttribute();
					
					// need to check artifact i think????????
					if(artifact.getArtifactName().equalsIgnoreCase(TargetArtifact))
					{
					
					AttributeInstance MappingAtt = artifact.getAttributeInstance(TargetAttribute);
					//need to do the casting
					String AttributeType = MappingAtt.getAttributeType();
					
					
					String AttributeStructure = MappingAtt.getAttributeStructure();
			
					if(AttributeStructure.equalsIgnoreCase("pair"))
					{
						
						try {
							
							if(MappingAtt.Size()== 0)
							{
								if(AttributeType.equalsIgnoreCase("string"))
								{	
									MappingAtt.add((String)MessageValue);
								}
								else if (AttributeType.equalsIgnoreCase("integer"))
								{
									Integer ConvertedMessageValue = Integer.parseInt(MessageValue);
									MappingAtt.add(ConvertedMessageValue);
									
								}
								else if (AttributeType.equalsIgnoreCase("short"))
								{
									Short ConvertedMessageValue = new Short(MessageValue);
									MappingAtt.add(ConvertedMessageValue);
									
								}
								else if (AttributeType.equalsIgnoreCase("double"))
								{
									Double ConvertedMessageValue = Double.parseDouble(MessageValue);
									MappingAtt.add(ConvertedMessageValue);
									
								}
								else if (AttributeType.equalsIgnoreCase("boolean"))
								{
									
									Boolean ConvertedMessageValue = Boolean.parseBoolean(MessageValue);
									MappingAtt.add(ConvertedMessageValue);
									
									
								}
								// the last one should be artifact
								
								
							}
							else
							{
								
								if(AttributeType.equalsIgnoreCase("string"))
								{	
									MappingAtt.set(0, (String)MessageValue);
								}
								else if (AttributeType.equalsIgnoreCase("integer"))
								{
									Integer ConvertedMessageValue = Integer.parseInt(MessageValue);
									MappingAtt.set(0, ConvertedMessageValue);
									
									
								}
								else if (AttributeType.equalsIgnoreCase("short"))
								{
									Short ConvertedMessageValue = new Short(MessageValue);
									MappingAtt.set(0, ConvertedMessageValue);
									
								}
								else if (AttributeType.equalsIgnoreCase("double"))
								{
									Double ConvertedMessageValue = Double.parseDouble(MessageValue);
									MappingAtt.set(0, ConvertedMessageValue);
									
								}
								else if (AttributeType.equalsIgnoreCase("boolean"))
								{
									
									Boolean ConvertedMessageValue = Boolean.parseBoolean(MessageValue);
									MappingAtt.set(0, ConvertedMessageValue);
									
									
								}
								// the last one should be artifact
								
							}
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
					else if (AttributeStructure.equalsIgnoreCase("list"))
					{
						
						try {
							
							if(AttributeType.equalsIgnoreCase("string"))
							{	
								MappingAtt.add((String)MessageValue);
							}
							else if (AttributeType.equalsIgnoreCase("integer"))
							{
								Integer ConvertedMessageValue = Integer.parseInt(MessageValue);
								MappingAtt.add(ConvertedMessageValue);
								
							}
							else if (AttributeType.equalsIgnoreCase("short"))
							{
								Short ConvertedMessageValue = new Short(MessageValue);
								MappingAtt.add(ConvertedMessageValue);
								
							}
							else if (AttributeType.equalsIgnoreCase("double"))
							{
								Double ConvertedMessageValue = Double.parseDouble(MessageValue);
								MappingAtt.add(ConvertedMessageValue);
								
							}
							else if (AttributeType.equalsIgnoreCase("boolean"))
							{
								
								Boolean ConvertedMessageValue = Boolean.parseBoolean(MessageValue);
								MappingAtt.add(ConvertedMessageValue);
								
								
							}
							// the last one should be artifact
										
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						
					}
					
					
				}
					
				
			}
		
		}
		
		
		
		
	}
	
	
	
	//have to fix this
	public void mappingMessageToMessage(RuleInstance rule, OMElement userMessage, OMElement InputMessage)throws Exception 
	{
		
		ArrayList<MapRule> MapRuleList = rule.getMapRules();
		//need to fix this
		MapRule CurrentMapRule = extractMessageToMessageMapType(MapRuleList);
		
		//System.out.println(CurrentMapRule);
	
		
		//if there is no match for map rule then do nothing
		if(CurrentMapRule != null)
		{
		
			//System.out.println("test");
			ArrayList<copy> MappingList = CurrentMapRule.getMappingList(); 
			
			//System.out.println(MappingList.size());
			
			ListIterator<copy> itr = MappingList.listIterator();
			
			
			String NamespaceURIofUserMessage = userMessage.getNamespace().getNamespaceURI();
			
			String NamespaceURIofInputMessage = InputMessage.getNamespace().getNamespaceURI();
			
			while(itr.hasNext())
			{
				copy currentCopy = itr.next();
				
				
					//from part
					//QName UserMessageQName = new QName(NamespaceURIofUserMessage,currentCopy.getFromMessage());
					QName UserPartQName = new QName(NamespaceURIofUserMessage,currentCopy.getFromPart());
					
			
					OMElement fromPart = userMessage.getFirstChildWithName(UserPartQName);
					
					String userParameter = fromPart.getText();
					
					
					
					
					
					//to part
					//QName inputMessageQName = new QName(NamespaceURIofInputMessage,currentCopy.getToMessage());
					QName inputPartQName = new QName(NamespaceURIofInputMessage,currentCopy.getToPart());
				
					OMElement ToPart = InputMessage.getFirstChildWithName(inputPartQName);
					
					ToPart.setText(userParameter);
			/*	
				//from part
				QName UserMessageQName = new QName(NamespaceURIofUserMessage,currentCopy.getFromMessage());
				QName UserPartQName = new QName(NamespaceURI,currentCopy.getFromPart());
					
					
				//to part
				QName PartQName = new QName(NamespaceURIofInputMessage,currentCopy.getToPart());
				
				OMElement ToPart = InputMessage.getFirstChildWithName(PartQName);
					
		
				if(AttributeStructure.equalsIgnoreCase("pair"))
				{
					try {
						
						
						ToPart.setText( MappingAtt.get(0).toString());
				
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
				}
				else if (AttributeStructure.equalsIgnoreCase("list"))
				{
					
					
					
					
				}
					
				
			*/
			}
		}
	}
	
	
	
	public void mapppingArtifactToMessage(RuleInstance rule, ArtifactInstance artifact, OMElement InputMessage)throws Exception 
	{
		
		ArrayList<MapRule> MapRuleList = rule.getMapRules();
		
		MapRule CurrentMapRule = extractArtifactToMessageMapType(MapRuleList);
		//if there is no match for map rule then do nothing
		if(CurrentMapRule != null)
		{	
			
			ArrayList<copy> MappingList = CurrentMapRule.getMappingList(); 
			
			ListIterator<copy> itr = MappingList.listIterator();
			
		
			String NamespaceURI = InputMessage.getNamespace().getNamespaceURI();
			
			while(itr.hasNext())
			{
				copy currentCopy = itr.next();
				
				//check requirement message and artifact 
			
				if(currentCopy.getFromArtifact().equalsIgnoreCase(artifact.getArtifactName()) )
				{
					
					
					//from part
					String SourceAttribute = currentCopy.getFromAttribute();
					
					AttributeInstance MappingAtt = artifact.getAttributeInstance(SourceAttribute);
					//need to do the casting
					String AttributeType = MappingAtt.getAttributeType();
					
					String AttributeStructure = MappingAtt.getAttributeStructure();
					
					
					
					
					//to part
					QName PartQName = new QName(NamespaceURI,currentCopy.getToPart());
					
					OMElement ToPart = InputMessage.getFirstChildWithName(PartQName);
						
			
					if(AttributeStructure.equalsIgnoreCase("pair"))
					{
						try {
							
							
							ToPart.setText( MappingAtt.get(0).toString());
					
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						
					}
					else if (AttributeStructure.equalsIgnoreCase("list"))
					{
						
						
						
						
					}
					
					
				}
			
			}
		
		
		}
	
		
		
	
		
	}
	
	
	
	///map local artifact to payload of a notification message
	public void mapppingArtifactToPayload(RuleType SharingRule, ArtifactInstance Artifact, OMElement Payload) throws Exception 
	{
		//list artifact in sharing rule
		List<ArtifactType> SharingArtifactList = SharingRule.getShare().getArtifact();
		ListIterator<ArtifactType> SharingArtifactListIterator = SharingArtifactList.listIterator();
		//namespace of payload 
		OMNamespace omNs = Payload.getNamespace();
		
		
		
		
		//loop through all artifact in mapping rule
		while(SharingArtifactListIterator.hasNext())
		{
			//current artifact in sharing rule
			ArtifactType SharingArtifact = SharingArtifactListIterator.next();
			//name of artifact need to 
			String FromArtifact = SharingArtifact.getName();
			
			//check whether artifact instance match to current artifact in sharting rule
			if(Artifact.getArtifactName().equalsIgnoreCase(FromArtifact))
			{
				//list of attribute need to be shared
				List<String> FromAttributesList = SharingArtifact.getAttributes().getAttribute();
				ListIterator<String> FromAttributesListIterator = FromAttributesList.listIterator();
				
				//loop all attribute of artifact in sharing rule
				while(FromAttributesListIterator.hasNext())
				{
					String CurrentAttributeName = FromAttributesListIterator.next();
					
					AttributeInstance SourceAttribute = Artifact.getAttributeInstance(CurrentAttributeName);
					//value to be map
					String value = SourceAttribute.get(0).toString();
					
					//list of artifact element
					Iterator<OMElement> ListOfArtifactElement = Payload.getChildElements();
					
					//try to loop to find target artifact 
					while(ListOfArtifactElement.hasNext())
					{
						OMElement CurrentArtifactElement = ListOfArtifactElement.next();
						
						String CurrentArtifactElementName = CurrentArtifactElement.getAttributeValue(new QName(omNs.getNamespaceURI(),"name"));
						
						if(CurrentArtifactElementName.equalsIgnoreCase(Artifact.getArtifactName()))
						{
							Iterator<OMElement> ArtifactAttributeElementIterator =  CurrentArtifactElement.getChildElements();
							//loop to find targer attribute
							while(ArtifactAttributeElementIterator.hasNext())
							{
								OMElement CurrentArtifactAtrributeElement = ArtifactAttributeElementIterator.next();
								
								String CurrentArtifactAttributeName = CurrentArtifactAtrributeElement.getAttributeValue(new QName(omNs.getNamespaceURI(),"name")); 
								
								if(CurrentArtifactAttributeName.equalsIgnoreCase(CurrentAttributeName))
								{
									
									OMFactory fac = OMAbstractFactory.getOMFactory();
									
									OMElement ValueElement = fac.createOMElement("value", omNs);
									
									ValueElement.setText(value);
									
									CurrentArtifactAtrributeElement.addChild(ValueElement);
									
								}
								
		
								
							}
							
							
							
						}
						
						
						
					}
					
			
					
				}
			}
	
		}
	
	}
	
	private  MapRule extractRequestToArtifactMapType(ArrayList<MapRule> ListOfMapRule)
	{
		ListIterator<MapRule> itr = ListOfMapRule.listIterator();
		
		while(itr.hasNext())
		{
			MapRule CurrentMapRule = itr.next();
			if(CurrentMapRule.getType().equalsIgnoreCase(Pattern1))
			{
				
				
				return CurrentMapRule;
				
				
				
			
			}
		}
		
		return null;		// should not reach this line
		
	}
	
	private  MapRule extractMessageToArtifactMapType(ArrayList<MapRule> ListOfMapRule)
	{
		ListIterator<MapRule> itr = ListOfMapRule.listIterator();
		
		while(itr.hasNext())
		{
			MapRule CurrentMapRule = itr.next();
			if(CurrentMapRule.getType().equalsIgnoreCase(Pattern2))
			{
				
				
				return CurrentMapRule;
				
				
				
			
			}
		}
		
		return null;		// should not reach this line
		
	}
	
	private  MapRule extractArtifactToMessageMapType(ArrayList<MapRule> ListOfMapRule)
	{
		ListIterator<MapRule> itr = ListOfMapRule.listIterator();
		
		while(itr.hasNext())
		{
			MapRule CurrentMapRule = itr.next();
			if(CurrentMapRule.getType().equalsIgnoreCase(Pattern3))
			{
				
				
				return CurrentMapRule;
				
				
				
			
			}
		}
		
		return null;		// should not reach this line
		
	}
	
	private MapRule extractMessageToMessageMapType(ArrayList<MapRule> ListOfMapRule)
	{
		ListIterator<MapRule> itr = ListOfMapRule.listIterator();
		
		while(itr.hasNext())
		{
			MapRule CurrentMapRule = itr.next();
			if(CurrentMapRule.getType().equalsIgnoreCase(Pattern4))
			{
				
				
				return CurrentMapRule;
				
				
				
			
			}
		}
		
		
		
		
		return null;
		
		
		
	}
	
	
	
	
	
	
	
	
	

}
