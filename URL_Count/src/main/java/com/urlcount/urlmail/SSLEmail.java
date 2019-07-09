
package com.urlcount.urlmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SSLEmail {
 
	private static final Logger logger = LogManager.getLogger(SSLEmail.class);
	public static void SendMail(File file) throws Exception
	{
	Properties prop = new Properties();
	
        InputStream in;
        try (InputStream input = SSLEmail.class.getClassLoader().getResourceAsStream("config.properties")) {

            

            if (input == null) {
            	logger.info("Sorry, unable to find config.properties");
                return;
            }
            
            //load a properties file from class path, inside static method
            prop.load(input);
            final String fromEmail =prop.getProperty("from");
        	final String toEmail = prop.getProperty("to");
        	//final String fromEmail = "kgaurav@hcl.com"; //requires valid gmail id
        	//final String password = "KG2019av";
        	final String password =  prop.getProperty("password");
        	//final String toEmail = "<kgaurav@pbgholdings.com >"; // can be any email id 
        	//final String toEmail = "kgaurav@pbgholdings.com";
        	String attachmentName = file.getAbsolutePath();
            
            System.out.println("SSLEmail Start");
    		Properties props = new Properties();
    		props.put("mail.smtp.auth", true);
    	    props.put("mail.smtp.starttls.enable", true);
    	    props.put("mail.smtp.host", "smtp.nitds.com");
    	    props.put("mail.smtp.port", "587");
    	    prop.put("mail.smtp.ssl.trust", "smtp.nitds.com");
    	    
    	    //SSL Factory Class
    		//props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
    	//	props.put("mail.smtp.port", "25"); //SMTP Port
    		logger.info( props.get("mail.smtp.port").toString()+"::"+props.get("mail.smtp.host").toString()+"::");
    		Authenticator auth = new Authenticator() {
    			//override the getPasswordAuthentication method
    			protected PasswordAuthentication getPasswordAuthentication() {
    				return new PasswordAuthentication(fromEmail, password);
    			}
    		};
    		Session session = Session.getDefaultInstance(props, auth);
    		logger.info("Session created");
    	   //      EmailUtil.sendEmail(session, fromEmail,password, toEmail,"SSLEmail Testing Subject", "SSLEmail Testing Body");
    				StringBuilder body=new StringBuilder();
    				body.append("Dear Sir/Madam\n");
    				body.append("\n\nPlease Find URL Link report attachment");
    				
    				
    				
    	  EmailUtil.sendAttachmentEmail(session, fromEmail,password, toEmail,"URL Link Report ", body.toString(),file);

         
        } catch (Exception ex) {
            throw ex;
        }

		
		
		
	
	
		
		
		

	 //       EmailUtil.sendImageEmail(session, toEmail,"SSLEmail Testing Subject with Image", "SSLEmail Testing Body with Image");

	}
	
	public static void main(String[] args) {
		
	
	}
} 
 
