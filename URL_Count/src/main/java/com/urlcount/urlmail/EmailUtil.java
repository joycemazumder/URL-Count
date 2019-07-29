package com.urlcount.urlmail;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

 
public class EmailUtil {

	/**
	 * Utility method to send simple HTML email
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	 
	private static final Logger logger = LogManager.getLogger(EmailUtil.class);
	
	public static void sendEmail(Session session,String  fromEmail ,String pwd,String toEmail, String subject, String body){
		try
	    {
	      MimeMessage msg = new MimeMessage(session);
	      //set message headers
	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	      msg.addHeader("format", "flowed");
	      msg.addHeader("Content-Transfer-Encoding", "8bit");
	      logger.info("Email from: "+fromEmail +" to "+toEmail);
	      msg.setFrom(new InternetAddress(fromEmail, "NoReply-JD"));

	      msg.setReplyTo(InternetAddress.parse(toEmail, false));

	      msg.setSubject(subject, "UTF-8");

	      msg.setText(body, "UTF-8");

	      msg.setSentDate(new Date());

	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
	      logger.info("Message is ready");
    	  Transport.send(msg, fromEmail, pwd); 

    	  logger.info("EMail Sent Successfully!!" );
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	
	/**
	 * Utility method to send email with attachment
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 * @throws Exception 
	 */
	public static void sendAttachmentEmail(Session session,String  fromEmail ,String pwd,String toEmail, String subject, String body,File file) throws Exception{
		try{
	         MimeMessage msg = new MimeMessage(session);
	         msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		     msg.addHeader("format", "flowed");
		     msg.addHeader("Content-Transfer-Encoding", "8bit");
		      
		     msg.setFrom(new InternetAddress(fromEmail, "NoReply-JD"));
		     logger.info("Email from: "+fromEmail +" to "+toEmail);
		     msg.setReplyTo(InternetAddress.parse(toEmail, false));

		     msg.setSubject(subject, "UTF-8");

		     msg.setSentDate(new Date());

		     msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
		      
	         // Create the message body part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Fill the message
	         messageBodyPart.setText(body);
	         
	         // Create a multipart message for attachment
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Second part is attachment
	         messageBodyPart = new MimeBodyPart();
	         String filename =file.getAbsolutePath();
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(file.getName());
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         msg.setContent(multipart);

	         // Send message
	         Transport.send(msg);
	         logger.info("EMail Sent Successfully with attachment!!");
	      }catch (MessagingException e) {
	    	 throw e;
	        
	      } catch (UnsupportedEncodingException e) {
	    	  throw e;
			  
		}
		catch ( Exception e) {
			throw e;
		}
	}
	

/**
 * Utility method to send image in email body
 * @param session
 * @param toEmail
 * @param subject
 * @param body
 */
public static void sendImageEmail(Session session, String toEmail, String subject, String body){
	try{
         MimeMessage msg = new MimeMessage(session);
         msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	     msg.addHeader("format", "flowed");
	     msg.addHeader("Content-Transfer-Encoding", "8bit");
	      
	     msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));

	     msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

	     msg.setSubject(subject, "UTF-8");

	     msg.setSentDate(new Date());

	     msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
	      
         // Create the message body part
         BodyPart messageBodyPart = new MimeBodyPart();

         messageBodyPart.setText(body);
         
         // Create a multipart message for attachment
         Multipart multipart = new MimeMultipart();

         // Set text message part
         multipart.addBodyPart(messageBodyPart);

         // Second part is image attachment
         messageBodyPart = new MimeBodyPart();
         String filename = "image.png";
         DataSource source = new FileDataSource(filename);
         messageBodyPart.setDataHandler(new DataHandler(source));
         messageBodyPart.setFileName(filename);
         //Trick is to add the content-id header here
         messageBodyPart.setHeader("Content-ID", "image_id");
         multipart.addBodyPart(messageBodyPart);

         //third part for displaying image in the email body
         messageBodyPart = new MimeBodyPart();
         messageBodyPart.setContent("<h1>Attached Image</h1>" +
        		     "<img src='cid:image_id'>", "text/html");
         multipart.addBodyPart(messageBodyPart);
         
         //Set the multipart message to the email message
         msg.setContent(multipart);
         
         // Send message
         Transport.send(msg);
         System.out.println("EMail Sent Successfully with image!!");
      }catch (MessagingException e) {
         e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
		 e.printStackTrace();
	}
}

}
 