package com.test;
/*
 *  @version 1.1
 */

import java.io.File;

import com.acp.deploy.*;

public class ProcessDeploment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		AcpDeployer deployer = new AcpDeployer();
		
		File AcpDefinition = new File("C:/orderprocess.xml");
		
		deployer.uploadAcpDefinition(AcpDefinition);
		
		
		

	}

}
