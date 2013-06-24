package com.test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.event.rule.DebugAgendaEventListener;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.acp.factory.AcpFactory;
import com.acp.instance.ArtifactInstance;
import com.acp.instance.AttributeInstance;
import com.acp.instance.ProcessInstance;

public class testDrools {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		
		AcpFactory fac = new AcpFactory();
		ProcessInstance p1 = fac.getProcessInstance("ordering3");
		ArtifactInstance a1 = fac.getArtifactInstance("ordering3", "order");
		ArtifactInstance a2 = fac.getArtifactInstance("ordering3", "shipping");
		
		AttributeInstance att1 = a1.getAttributeInstance("orderID");
		AttributeInstance att2 = a2.getAttributeInstance("shippingID");
		System.out.println(att1);
		System.out.println(att2);
		att1.add("ID123");
		att2.add("SH123");
		a1.setCurrentState("start");
		a2.setCurrentState("start");
		
		p1.addArtifactInstance(a1);
		p1.addArtifactInstance(a2);
		
		
		ProcessInstance p2 = fac.getProcessInstance("ordering3");
		ArtifactInstance b1 = fac.getArtifactInstance("ordering3", "order");
		ArtifactInstance b2 = fac.getArtifactInstance("ordering3", "shipping");
		
		AttributeInstance att11 = b1.getAttributeInstance("orderID");
		AttributeInstance att22 = b2.getAttributeInstance("shippingID");
		System.out.println(att11);
		System.out.println(att22);
		att11.add("ID123");
		att22.add("SH123");
		b1.setCurrentState("start");
		b2.setCurrentState("start");
		
		p2.addArtifactInstance(b1);
		p2.addArtifactInstance(b2);
		
		
		
		
		/*
		 *  used to turn a DRL source file into Package objects
		 */
		
		final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

		FileInputStream fis1 = new FileInputStream("C:/order.drl");
	//	FileInputStream fis2 = new FileInputStream("C:/HelloWorld.drl");
		
		/*
		*2.0  this will parse and compile in one step
		*/
	
	//	kbuilder.add(ResourceFactory.newClassPathResource("C:/order.drl",testDrools.class), ResourceType.DRL);
		kbuilder.add(ResourceFactory.newInputStreamResource(fis1), ResourceType.DRL);
//		kbuilder.add(ResourceFactory.newInputStreamResource(fis2), ResourceType.DRL);
		/*
		*3.0 Check the builder for errors
		*/
		if (kbuilder.hasErrors()) {
			System.out.println(kbuilder.getErrors().toString());
			throw new RuntimeException("Unable to compile \"order.drl\".");
		}
		/*
		*4.0 get the compiled packages (which are serializable)
		* 
		*/
		final Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
		/*
		 *5.0 add the packages to a knowledgebase (deploy the knowledge packages).
		 *
		 */
		final KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(pkgs);
		 StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		
		
		//what is set Global, this part add listener
	//	ksession.setGlobal("list", new ArrayList<Object>());
	//	ksession.addEventListener(new DebugAgendaEventListener());
		ksession.addEventListener(new DebugWorkingMemoryEventListener());
		// setup the audit logging
		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
				.newFileLogger(ksession, "C:/log/helloworld");
		
		/*
		* 6.0 insert fact and fire rule
		*/
	
		
	//test one artifact	
		
	/*
		ksession.insert(a1);
	

		ksession.fireAllRules();
		
		

		logger.close();

		ksession.dispose();
	*/	
		
	//test multiple artifact
		
		
		ksession.insert(p1);

	
		ksession.fireUntilHalt();
		
		
		ksession.insert(p2);
		
	//	ksession.halt();
		
//	logger.close();

//	ksession.dispose();
		
	
	
		
		
		
		
		
		
		
		
		
		
	}

}
