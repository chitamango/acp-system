package com.acp.listeners;

import java.util.EventListener;

import com.acp.events.ArtifactChangeEvent;

public interface ArtifactChangeEventListener extends EventListener {

	void invokeRuleEngine(ArtifactChangeEvent evt);
	

}
