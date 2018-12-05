package com.acp.listeners;
/*
 *  @version 1.1
 */

import java.util.EventListener;

import com.acp.events.ArtifactChangeEvent;

public interface ArtifactChangeEventListener extends EventListener {

	void invokeRuleEngine(ArtifactChangeEvent evt);
	

}
