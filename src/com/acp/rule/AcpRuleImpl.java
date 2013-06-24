package com.acp.rule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.apache.axiom.om.OMElement;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.definition.KnowledgePackage;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.drools.runtime.rule.FactHandle;

import com.acp.instance.ProcessInstance;
import com.acp.controllers.AcpProcessController;
import com.acp.events.ArtifactChangeEvent;
import com.acp.events.CombineEvent;
import com.acp.events.MessageEvent;




public class AcpRuleImpl {
/*	
	protected KnowledgeBuilder kbuilder; 
	protected Collection<KnowledgePackage> pkgs;
	protected KnowledgeBase kbase;
	protected StatefulKnowledgeSession ksession;
	protected FileInputStream RuleFis;
	protected String ruleFile;
	
*/	
	protected KnowledgeBase kbase;
	protected DebugWorkingMemoryEventListener debugListener;
	protected ArrayList<String> RuleFiles;
	

	
	/**
	 * 
	 * Constructor method to initialise rule engine //need to fix to be able to load more rule files
	 * 
	 * @param RuleFile
	 */
	public AcpRuleImpl()
	{
		
		/*
		 *  to turn a DRL source file into Package objects
		 */
		RuleFiles = new ArrayList<String>();
		//get a list of Rule 
		this.readRuleFiles();
		
	try {
		
		
		
		debugListener = new DebugWorkingMemoryEventListener();
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		//kbuilder.add(ResourceFactory.newInputStreamResource(RuleFis), ResourceType.DRL);
		
		//add each rule file into knowleadge builder
		ListIterator<String> itr = RuleFiles.listIterator();
		
		while(itr.hasNext())
		{
			String RuleFile = itr.next();
		
			FileInputStream RuleFis = new FileInputStream(RuleFile);
		
		
			kbuilder.add(ResourceFactory.newInputStreamResource(RuleFis), ResourceType.DRL);
			
		
		
		if (kbuilder.hasErrors()) {
			System.out.println(kbuilder.getErrors().toString());
			throw new RuntimeException("Unable to compile: "+ RuleFile + ".");
		}
		
		}
		
		 //get the compiled packages (which are serializable)
		
		Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
		
		
		 //add the packages to a knowledgebase (deploy the knowledge packages).
		
		kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(pkgs);
		//ksession = kbase.newStatefulKnowledgeSession();
			} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	
/*	
	public void executeRules(ProcessInstance process)
	{
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		ksession.addEventListener(debugListener);
		
		ksession.insert(process);
		
		
		ksession.fireAllRules();
		
			
	}
*/	
	public void executeRules(ArtifactChangeEvent artifactChangeEvt, AcpProcessController Pcontroller)
	{
		StatelessKnowledgeSession ksession;
		//StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		//ksession = kbase.newStatefulKnowledgeSession();
		ksession = kbase.newStatelessKnowledgeSession();
		ksession.addEventListener(debugListener);
		ksession.setGlobal("pc", Pcontroller );
		//ksession.insert(Process);
	
			ksession.execute(artifactChangeEvt);
		
		//ksession.fireAllRules();
		
		//ksession.dispose();
		
			
	}
	
	public void executeRules(CombineEvent combineEvent, AcpProcessController Pcontroller)
	{
		StatelessKnowledgeSession ksession;
		//StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		//ksession = kbase.newStatefulKnowledgeSession();
		System.out.println(combineEvent.getValueFromPart("item"));
		
		ksession = kbase.newStatelessKnowledgeSession();
		
		ksession.addEventListener(debugListener);
		ksession.setGlobal("pc", Pcontroller );
		
		//FactHandle MessageHandle = ksession.insert(Message);
		//FactHandle ProcessHandle = ksession.insert(Process);
	
		// Set up a list of commands

	
		
		ksession.execute(combineEvent);
		

		//ksession.fireAllRules();
		
		//ksession.dispose();
			
	}
	
	
	public void executeRules(MessageEvent Message, AcpProcessController Pcontroller)
	{
		StatelessKnowledgeSession ksession;
		//StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		//ksession = kbase.newStatefulKnowledgeSession();
		ksession = kbase.newStatelessKnowledgeSession();
		ksession.addEventListener(debugListener);
		ksession.setGlobal("pc", Pcontroller );
		//FactHandle MessageHandle = ksession.insert(Message);
		
		ksession.execute(Message);
		//ksession.fireAllRules();
		
		//ksession.dispose();
	
		
		
	}
	
	
/*	
	public void executeRules(Object arg0, AcpProcessController Pcontroller)
	{
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		ksession.addEventListener(debugListener);
		ksession.setGlobal("pc", Pcontroller );
		ksession.insert(arg0);
		
		
		ksession.fireAllRules();
		ksession.dispose();
		
	}
	
/*	
	public void excuteRule(Object arg0)
	{
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		ksession.addEventListener(debugListener);
		ksession.insert(arg0);
		
		
		ksession.fireAllRules();
	
		
	}
	
	*/
	//need to add more method in the future !!
	
	
	private void readRuleFiles()
	{
		//have to change in the future
		File RuleFolder = new File("rules/") ;
		File[] ListOfFiles = RuleFolder.listFiles();
		
		for (int i = 0;i<ListOfFiles.length; i++ )
		{
			if(ListOfFiles[i].isFile())
			{
				this.RuleFiles.add(ListOfFiles[i].getPath());	
			
				
			}
						
		}
		
				
	}
	
	
	
	
	
	
	
	
	

}
