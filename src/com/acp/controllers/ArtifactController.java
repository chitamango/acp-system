package com.acp.controllers;
/*
 *  @version 1.1
 */

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;

import javax.rules.admin.Rule;
import javax.servlet.http.HttpServletRequest;

import org.apache.axiom.om.OMElement;

import com.acp.instance.ArtifactInstance;
import com.acp.instance.ProcessInstance;
import com.acp.instance.RuleInstance;
import com.acp.instance.StateInstance;
import com.acp.instance.TransitionRule;
import com.acp.mapper.Mapper;

public class ArtifactController {
	
	
	private Mapper mapper ;
	
	
	
	public ArtifactController(Mapper DataMapper)
	{
		
		mapper = DataMapper;
		
	}
	
	
	public void updateArtifact(ArrayList<ArtifactInstance> Artifacts,HttpServletRequest request ,ProcessInstance Process ,RuleInstance Rule )
	{
		ArrayList<ArtifactInstance> ListofArtifactInProcessInstance = Process.getArtifactInstanceList();
		
		ListIterator<ArtifactInstance> itr = Artifacts.listIterator();
		
		while(itr.hasNext())
		{
			ArtifactInstance CurrentArtifact = itr.next();
			
			
			if(ListofArtifactInProcessInstance.contains(CurrentArtifact))
			{
			
			//this call mapping method, pass artifact and request, message
			
				try {
					mapper.mapppingRequestToArtifact(Rule, CurrentArtifact, request);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			
			
			
		}
		
		
		
		//next is to do a state transition
		this.updateArtifactState(Rule.getTransitionRules(), Artifacts);
		
	
		
		
	}
	
	public void updateArtifact(ArrayList<ArtifactInstance> Artifacts,OMElement SOAPOutputMessage ,ProcessInstance Process ,RuleInstance Rule )
	{
		ArrayList<ArtifactInstance> ListofArtifactInProcessInstance = Process.getArtifactInstanceList();
		
		ListIterator<ArtifactInstance> itr = Artifacts.listIterator();
		
		while(itr.hasNext())
		{
			ArtifactInstance CurrentArtifact = itr.next();
			
			
			if(ListofArtifactInProcessInstance.contains(CurrentArtifact))
			{
			
			//this call mapping method, pass artifact and request, message
			
				try {
					this.mapper.mapppingMessageToArtifact(Rule, CurrentArtifact, SOAPOutputMessage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			
			
			
			try {
				
				
			
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		
		//next is to do a state transition
		this.updateArtifactState(Rule.getTransitionRules(), Artifacts);
		
	
		
		
	}

	// to end the process change artifact state only no service invoked
	public void updateArtifact(ArrayList<ArtifactInstance> Artifacts,ProcessInstance Process ,RuleInstance Rule )
	{
		
		ArrayList<ArtifactInstance> ListofArtifactInProcessInstance = Process.getArtifactInstanceList();
		
		if(ListofArtifactInProcessInstance.containsAll(Artifacts))
		{
			
			this.updateArtifactState(Rule.getTransitionRules(), Artifacts);
			
		}
		
		
		
		
		
	}
	
	public void AddArtifactInstance(ArtifactInstance Artifact, ProcessInstance Process)
	{
		//the way to give artifact id should be revised
		//Artifact.setArtifactId(Process.getProcessId()+ Artifact.getArtifactName()+DateFormat.getDateTimeInstance().format(new Date()));
		
		Process.addArtifactInstance(Artifact);
			
		
		
	}
	//i think some thing wrong here have to fix !!!
	//need to make use of artifact id for changing the state
	private void updateArtifactState(ArrayList<TransitionRule> transitionRules , ArrayList<ArtifactInstance> Artifacts)
	{
		
		
		ListIterator<TransitionRule>  TrasitionIterator = transitionRules.listIterator();
		
		//System.out.println("size of transition Rule:" + transitionRules.size());
		
		while(TrasitionIterator.hasNext())
		{
			TransitionRule currentTransitionRule = TrasitionIterator.next();
			String targetArtifact = currentTransitionRule.getArtifact();
			String fromState = currentTransitionRule.getFromState();
			String toState = currentTransitionRule.getToState();
			
			//System.out.println(targetArtifact);
			//System.out.println(fromState);
			//System.out.println(toState);
			
			ListIterator<ArtifactInstance> ArtifactIterator = Artifacts.listIterator();
			
			while(ArtifactIterator.hasNext())
			{
				
				ArtifactInstance CurrentArtifact = ArtifactIterator.next();
				String ArtifactName = CurrentArtifact.getArtifactName();
			//	System.out.println(ArtifactName);
				String CurrentArtifactState = CurrentArtifact.getCurrentState();
				ArrayList<StateInstance> StateList = CurrentArtifact.getStateList();
				
			 // to be continue state transiton
				if(ArtifactName.equalsIgnoreCase(targetArtifact) && CurrentArtifactState.equalsIgnoreCase(fromState) ) //&& this.checkState(StateList, fromState, toState)
				{
					
					CurrentArtifact.setCurrentState(toState);
					//System.out.println(CurrentArtifact.getArtifactName()+":"+CurrentArtifact.getCurrentState());
					
				}
				
				
				
			}
			
			
		}
		
		
		
		
		
		
	}
	
	private Boolean checkState(ArrayList<StateInstance> StateList,String FromState,String ToState )
	{
		ListIterator<StateInstance> itr = StateList.listIterator();
		Boolean FromStateValid = false;
		Boolean ToStateValid = false;
		Boolean valid = false;
		
		while(itr.hasNext())
		{
			StateInstance Currentstate = itr.next();
			String StateName = Currentstate.getStateName();
			
			if(StateName.equalsIgnoreCase(FromState))
			{
				
				FromStateValid = true;
				
			}
			
			if(StateName.equalsIgnoreCase(ToState))
			{
				
				ToStateValid = true;
				
			}
			
			
			
		}
		
		if(FromStateValid && ToStateValid )
		{
			
			valid = true;
			
			
		}
		
		
		
		
		return valid;
		
	}
	
	
	
	

}
