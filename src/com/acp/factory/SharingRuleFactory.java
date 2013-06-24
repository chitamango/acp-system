package com.acp.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.acp.process.Process;
import com.acp.sharingrule.ClientSharingRule;

public class SharingRuleFactory {
	
	private JAXBContext jaxbContext;
	private  Unmarshaller unmarshaller;
	public ArrayList<String> SharedRuleFiles;
	
	
	public SharingRuleFactory()
	{
		
		SharedRuleFiles = new ArrayList<String>();
		
		this.readRuleFiles();
		
		
		try {
			jaxbContext = JAXBContext.newInstance("com.acp.sharingrule");
			unmarshaller = jaxbContext.createUnmarshaller();
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		
		
		
	}
	
	
	public ArrayList<ClientSharingRule> getSharingRules()
	{
		ArrayList<ClientSharingRule> ListOfSharingRules = new ArrayList<ClientSharingRule>();
		
		try {
			
			ListIterator<String> RuleFilesListIterator = SharedRuleFiles.listIterator();
			
			while(RuleFilesListIterator.hasNext())
			{
			
				String RuleLocation = RuleFilesListIterator.next();
				
				File RuleFile = new File(RuleLocation);
				
				Object obj = unmarshaller.unmarshal(RuleFile) ;
		
				
				ListOfSharingRules.add((ClientSharingRule)obj);
						
									
			
			}	
					
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return ListOfSharingRules;
		

	}
	
	
	
	private void readRuleFiles()
	{
		//have to change in the future
		File RuleFolder = new File("sharingrules/") ;
		File[] ListOfFiles = RuleFolder.listFiles();
		
		for (int i = 0;i<ListOfFiles.length; i++ )
		{
			if(ListOfFiles[i].isFile())
			{
				this.SharedRuleFiles.add(ListOfFiles[i].getPath());	
			
				
			}
						
		}
		
				
	}
	
	
	

}
