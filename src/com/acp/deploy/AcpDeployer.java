package com.acp.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;



import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;



public class AcpDeployer {
	
	
	DocumentBuilderFactory dbfac; 
    DocumentBuilder docBuilder; 
    Document doc; 
	
	
	public void uploadAcpDefinition(File acpInputFile)
	{
		
		System.out.println("uploading... "+ acpInputFile.getAbsolutePath() );
		if(checkFileExtension(acpInputFile) == true && checkACPSchema(acpInputFile)== true )
		{
			
			if(registerProcess(acpInputFile)== true)
			{
				System.out.println("Successfully uploading: " + acpInputFile.getName() );
			}
			else
			{
				System.out.println("failed uploading: " + acpInputFile.getName() );
				
			}
	
		}
		else
		{
			
			System.out.println("failed uploading: " + acpInputFile.getName() );
			
			
		}
		
	 

		
		
		
	}
	

	
	private Boolean checkFileExtension(File InputFile)
	{
		Boolean checkResult = true;
		Integer dotCount = InputFile.getName().split(".").length - 1;
		Integer dotLocation = InputFile.getName().indexOf(".");
		String fileExtension = InputFile.getName().substring(dotLocation+1);
		
		//check whether file contain more than one dot
		if(dotCount > 1)
		{
			
			System.out.println("File checking:  not a valid file format");
			checkResult = false;
			
		}
		//check file extension
		if(fileExtension.toLowerCase().equals("xml")== false)
		{
			
			System.out.println("File checking:   not a valid file extension");
			checkResult = false;		
			
		}
		
		
		if(checkResult == true)
		{
			
			System.out.println("File checking:  Passed");
			
		}
				
		
		return checkResult;
	}
	
/////////////
	
private boolean checkACPSchema(File InputFile) 
{
	

	Boolean checkResult = true;
	//File schFile = new File("C:/AcpSchema.xsd"); // have to change this in the future
	File schFile = new File("src/AcpSchema.xsd");
	
	
	try {
	//set schema factory	
	SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
	
	//compile schema
    Schema schema = factory.newSchema(schFile);
	//get  a validator from a schema
    Validator validator = schema.newValidator();
    //parse xml file to create dom
    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    domFactory.setNamespaceAware(true); // never forget this
    DocumentBuilder Dbuilder = domFactory.newDocumentBuilder();
    Document doc = Dbuilder.parse(InputFile);
    DOMSource source = new DOMSource(doc);
    // validate the DOM tree
      validator.validate(source);
        
        
    }
    catch (SAXException ex) {
    	checkResult = false;
        System.out.println("Acp Schema checking: failed ");
        System.out.println(ex.getMessage());
    } catch (IOException e) {
		// TODO Auto-generated catch block
    	checkResult = false;
		e.printStackTrace();
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		checkResult = false;
		e.printStackTrace();
	}  
    
   
	if(checkResult == true)
	{
		
		System.out.println("Acp Schema checking:  Passed");
		
	}

	
	
	
	return checkResult;
	
	
}
/**
 * 
 * 
 * 
 * 
 * 
 * @param InputFile
 * @return
 */
//input file is location that deployer save acp xml file
private Boolean registerProcess(File InputFile)
{
	//why relative path doesn't work
	File registerProcessFile = new File("definition/deployedprocess.xml"); // have to change this - this is register file
	//File registerProcessFile = new File("deployed/deployedprocess.xml");
	Boolean registerResult = true;
	
	//this part is location of deployed xml file
	File processfolder= new File("definition/");//have to change this as well
//	File deployedfolder = new File("C:/deployed/");//have to change this as well
	File processFile = new File("definition/"+InputFile.getName());//have to change this as well
	    
//	File processfolder= new File("deployed/process/");//have to change this as well
//	File deployedfolder = new File("deployed/");//have to change this as well
//	File processFile = new File("deployed/process/"+InputFile.getName());//have to change this as well
	
	
	//    if(deployedfolder.exists()== false)
	//    {
	//    	deployedfolder.mkdir();
	//    }
	    
	    if(processfolder.exists()== false)
	    {
	
	    	processfolder.mkdir();
	    		
	    }
	    
	
	System.out.println("Acp process registration: registering a new process");
	if(registerProcessFile.exists())
	{
		
		try {
		//load existing registered process file	 to domcument
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	    domFactory.setNamespaceAware(true); // never forget this
	    DocumentBuilder Dbuilder = domFactory.newDocumentBuilder();
	    Document doc = Dbuilder.parse(registerProcessFile);
	    
	    //Load Input acp process file to dom document
	    DocumentBuilderFactory acpdomFactory = DocumentBuilderFactory.newInstance();
	    acpdomFactory.setNamespaceAware(true); // never forget this
	    DocumentBuilder acpDbuilder = domFactory.newDocumentBuilder();
	    Document acpdoc = acpDbuilder.parse(InputFile);
	    
	    //get process name and file name from acp process definition
	    String processName = acpdoc.getFirstChild().getAttributes().getNamedItem("name").getNodeValue();
	    String processFilename = InputFile.getName();
	    
	    //searching inside registered process file 
	    NodeList  processnameList = doc.getElementsByTagName("name"); //get a list of name elements
	    NodeList   locationList = doc.getElementsByTagName("location");
	    //check whether process does exist	
		    for(int i = 0; i < processnameList.getLength() ;i++ )
		    {
		    	if(processName.equalsIgnoreCase(processnameList.item(i).getTextContent()) )
		    	{
		    		System.out.println("Acp process registration: failed(process already registered)");
		    		registerResult = false;
		    		
		    	}
		    	
		    	
		    		    		
		    }
		    
		    for(int i = 0; i < locationList.getLength() ;i++ )
		    {
		    	
		    	
		    	String locationString = locationList.item(i).getTextContent().substring(20);
		    	

		    	
		    	
		    	if(processFilename.equalsIgnoreCase(locationString) )
		    	{
		    		System.out.println("Acp process registration: failed(process defintion file already exist)");
		    		registerResult = false;
		    		
		    	}
		    	
		    	
		    		    		
		    }
		    
		    
		    
		  //if new process,add it 
		    
		    if(registerResult == true)
		    {	
			    System.out.println("Acp process registering: updating a registration file"); 
			    Element process = doc.createElement("process");
			    Element name = doc.createElement("name");
			    Element location = doc.createElement("location");
			    
			    //set value to name and location
			    name.setTextContent(processName);
			    location.setTextContent(processFile.getAbsolutePath()); // have to change this
			    
			   //append node 
			    process.appendChild(name);
			    process.appendChild(location);
			    doc.getFirstChild().appendChild(process);
		
			    
			    //save xml file
			    TransformerFactory factory = TransformerFactory.newInstance();
			    
			    Transformer transformer = factory.newTransformer();
			    
			    Result result = new StreamResult(registerProcessFile);
			    
			    Source source = new DOMSource(doc);
			    
			    transformer.transform(source, result);
			    
			  
			    
			  //save a process file to process folder
			    System.out.println("Acp process registering: updating a process folder"); 
			    factory = TransformerFactory.newInstance();
			    
			    transformer = factory.newTransformer();
			    
			    result = new StreamResult(processFile);
			    
			    source = new DOMSource(acpdoc);
			    
			    transformer.transform(source, result);
			    
		    }
	        
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
			
	}
	else
	{
		
		
		try {
			//load existing registered process file	 to domcument
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		    domFactory.setNamespaceAware(true); // never forget this
		    DocumentBuilder Dbuilder = domFactory.newDocumentBuilder();
		    Document doc = Dbuilder.newDocument();
		    
		    //Load Input acp process file to dom document
		    DocumentBuilderFactory acpdomFactory = DocumentBuilderFactory.newInstance();
		    acpdomFactory.setNamespaceAware(true); // never forget this
		    DocumentBuilder acpDbuilder = acpdomFactory.newDocumentBuilder();
		    Document acpdoc = acpDbuilder.parse(InputFile);
		    
		    	    
		    //get process name and location from acp process definition
		    String processName = acpdoc.getFirstChild().getAttributes().getNamedItem("name").getNodeValue();
		    
			  //if new process,add it 
		    	System.out.println("Acp process registering: updating a registration file"); 
		    	Element root = doc.createElement("registeredProcess");
			    Element process = doc.createElement("process");
			    Element name = doc.createElement("name");
			    Element location = doc.createElement("location");
			    
			    //set value to name and location
			    name.setTextContent(processName);
			    location.setTextContent(processFile.getAbsolutePath()); 
			    
			   //append node 
			    process.appendChild(name);
			    process.appendChild(location);
			    root.appendChild(process);
			    doc.appendChild(root);
			  
			    //save xml file 
			    //create a transformer factory
			    TransformerFactory factory = TransformerFactory.newInstance();
			    //create a new transformer
			    Transformer transformer = factory.newTransformer();
			    //create a stream result
			    Result result = new StreamResult(registerProcessFile);
			    //create new dom source
			    Source source = new DOMSource(doc);
			    //transform and save to xml file
			    transformer.transform(source, result);
			    
			    //save a process file to process folder
			    System.out.println("Acp process registering: updating a process folder");
			    
			    factory = TransformerFactory.newInstance();
			    
			    transformer = factory.newTransformer();
			    
			    result = new StreamResult(processFile);
			    
			    source = new DOMSource(acpdoc);
			    
			    transformer.transform(source, result);
			    
			    
		        
			
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		
		
		
	}
    
	if(registerResult == true)
	{
		
		System.out.println("Acp process registration: registering a new process completed ");
		
	}
	else
	{
		
		System.out.println("Acp process registration: registering a new process failed ");
		
	}
	
	return registerResult;

		
	
}




	// have to detect file name
	
	
	
	
	
	

}
