package com.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.drools.compiler.DrlParser;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.xml.XmlDumper;
import org.drools.lang.descr.PackageDescr;

public class DroolsRuleDumper {

   
   public String getXMLrule() throws FileNotFoundException, DroolsParserException
   {
		XmlDumper dumper = new XmlDumper();
		
		
		String fis = "C:/deployed/rule/order.drl";
		
	    final DrlParser parser = new DrlParser();
	   final PackageDescr pkg = parser.parse(fis);
		
	    String xml = dumper.dump(pkg);
	   
	   return xml;
	   
	   
	   
   }
	
	

}
