package com.regression.ServiceRequest;


import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import com.regression.utils.Constants;



public class ARIShopRequest {

	private  SOAPConnectionFactory soapConnectionFactory;
	private  SOAPConnection soapConnection ;
	private  MessageFactory messageFactory;
	private  SOAPMessage soapMessage;
	private  SOAPPart soapPart;
	
	public String requestXmlPath=System.getProperty("user.dir")+"//RequestXML//SingleRT-SingleRP.xml";
	public String actualResponsePath=System.getProperty("user.dir")+"//ActualResponse//SingleRT-SingleRP.xml";
	public String expectedResponsePath=System.getProperty("user.dir")+"//ExpectedResponse//SingleRT-SingleRP.xml";


	public void  getAriShopResponse(){

		try {
			//creating connection object
			soapConnectionFactory = SOAPConnectionFactory.newInstance();
			soapConnection = soapConnectionFactory.createConnection();

			//creating message object
			messageFactory = MessageFactory.newInstance();
			soapMessage = messageFactory.createMessage();                
			soapPart = soapMessage.getSOAPPart();                

			// Object for message parts
			StreamSource prepMsg = new StreamSource(new FileInputStream(requestXmlPath));
			soapPart.setContent(prepMsg);

			// Save message
			soapMessage.saveChanges();

			String endPoinUrl = Constants.WSDL_ARISHOP;

			SOAPMessage messagerequest = soapConnection.call(soapMessage, endPoinUrl);

			// response data
			Source source = messagerequest.getSOAPPart().getContent();

			// Create transformer object
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();  
			transformer.setOutputProperty(OutputKeys.INDENT, "no");

			// set response parameter to string
			StringWriter writer = new StringWriter();
			StreamResult sResult = new StreamResult(writer);
			transformer.transform(source, sResult);
			String result1 = writer.toString();       
			System.out.println(result1);

			// close connection
			soapConnection.close();

		
			File file = new File(actualResponsePath);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(result1);
			bw.close();

			System.out.println("Done");
			
			

		}   catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (SOAPException e) {
			e.printStackTrace();
		}         
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
}
