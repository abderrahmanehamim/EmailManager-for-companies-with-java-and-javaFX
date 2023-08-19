package MailFunctions;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import javafx.scene.control.Alert;

public class SendMail {
	

	public static void sendMail(String acc, String pwd,String recipient,String sub,String bod,List<File> files) throws MessagingException {
		
		System.out.println("Preparing...");
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", Consts.SMTP_HOST);
		prop.put("mail.smtp.port", Consts.SMTP_TLS_PORT);
		
	
		Session session = Session.getInstance(prop, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(acc, pwd);
			}
			
		});
		Message message ;
		 message = prepareMessage(session, acc, recipient, sub, bod,files);
		Transport.send(message);
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText("Mess sent..");
		alert.show();
	}
public static void sendMail(String acc, String pwd,String recipient,String sub,String bod) throws MessagingException {
		
		System.out.println("Preparing...");
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", Consts.SMTP_HOST);
		prop.put("mail.smtp.port", Consts.SMTP_TLS_PORT);
		
	
		Session session = Session.getInstance(prop, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(acc, pwd);
			}
			
		});
		 Message message = new MimeMessage(session);  
         message.setFrom(new InternetAddress(acc));  
         message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(recipient));
         message.setSubject(sub);  
         message.setText(bod);  
  
         // Send message  
         Transport.send(message);  
         System.out.println("message sent successfully....");  
  
		Transport.send(message);
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText("Mess sent..");
		alert.show();
	}
	
	private static Message prepareMessage(Session sec, String acc, String rec, String subj,String body,List<File> files) throws MessagingException {
	
		// Create a default MimeMessage object.
        Message message = new MimeMessage(sec);

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(acc));

        // Set To: header field of the header.
        message.setRecipients(Message.RecipientType.TO,
           InternetAddress.parse(rec));

        // Set Subject: header field
        message.setSubject(subj);

        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();

        // Now set the actual message
        messageBodyPart.setText(body);

        // Create a multipar message
        Multipart multipart = new MimeMultipart();

        // Set text message part
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        String path;
       for(File file : files) {
    	   path=file.getAbsolutePath();

           DataSource source = new FileDataSource(path);
           messageBodyPart.setDataHandler(new DataHandler(source));
           messageBodyPart.setFileName(path);
           multipart.addBodyPart(messageBodyPart);

       }
        // Send the complete message parts
        message.setContent(multipart);


		return message;
	}
	

}
