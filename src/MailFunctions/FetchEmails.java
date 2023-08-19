package MailFunctions;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.search.SearchTerm;
import javax.mail.internet.MimeMultipart;

import controllers.DBConnect;
import models.EmailModel;



public class FetchEmails {
	
	public FetchEmails() {
		super();
		// TODO Auto-generated constructor stub
	}

	private PreparedStatement pst;
	private EmailModel em;
   public static List<EmailModel> fetch(String user,
      String password, String folder) {
	   
	   
	   List<EmailModel> emails = new ArrayList<EmailModel>();
      try {
         // create properties field
         Properties properties = new Properties();
         properties.put("mail.store.protocol", "imap");
         properties.put("mail.imap.host", Consts.IMAP_HOST);
         properties.put("mail.imap.port", Consts.IMAP_PORT);
         properties.put("mail.imap.starttls.enable", "true");
         Session emailSession = Session.getDefaultInstance(properties);
         // emailSession.setDebug(true);

         // create the POP3 store object and connect with the pop server
         Store store = emailSession.getStore("imaps");

         store.connect(Consts.IMAP_HOST, user, password);
         javax.mail.Folder[] folders = store.getDefaultFolder().list("*");
         for (javax.mail.Folder fold : folders) {
             if ((fold.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
                 System.out.println(fold.getFullName() + ": " + fold.getMessageCount());
             }
         }
         // create the folder object and open it
         Folder emailFolder = store.getFolder(folder);

         if (!emailFolder.exists()&&folder.equals("[Gmail]/Archive"))
             if (emailFolder.create(Folder.HOLDS_MESSAGES))
                 System.out.println("Folder was created successfully");
            emailFolder.open(Folder.READ_ONLY);

         BufferedReader reader = new BufferedReader(new InputStreamReader(
	      System.in));

         // retrieve the messages from the folder in an array and print it
         Message[] messages = emailFolder.getMessages();
         
    	   System.out.println(messages.length);
         for (int i = 0; i < messages.length; i++) {

             Message msg = messages[i];
             Address[] fromAddress = msg.getFrom();
           
             String from = fromAddress[0].toString();
             String subject = msg.getSubject();
             String toList = parseAddresses(msg
				         .getRecipients(RecipientType.TO));
            
             String ccList = parseAddresses(msg
                     .getRecipients(RecipientType.CC));
             Date sentDate = msg.getSentDate();

             String contentType = msg.getContentType();
             String messageContent = "";
             messageContent = getTextFromMessage(msg) ;
           //  System.out.println("message" +messageContent);
            /* if (contentType.contains("text/plain")
                     || contentType.contains("text/html")) {
                 try {
                     Object content = msg.getContent();
                     if (content != null) {
                         messageContent = content.toString();
                     }
                 } catch (Exception ex) {
                     messageContent = "[Error downloading content]";
                     ex.printStackTrace();
                 }
             }*/
             
             

             // print out details of each message
             
             EmailModel em = new EmailModel(i+1,from,toList,sentDate,subject,messageContent);
            emails.add(em);
         }
         // close the store and folder objects
         emailFolder.close(false);
         store.close();

      } catch (NoSuchProviderException e) {
         e.printStackTrace();
      } catch (MessagingException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
	return emails;
   }
   private static String getTextFromMessage(Message message) throws MessagingException, IOException {
	    String result = "";
	    if (message.isMimeType("text/plain")) {
	        result = message.getContent().toString();
	    } else if (message.isMimeType("multipart/*")) {
	        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
	        result = getTextFromMimeMultipart(mimeMultipart);
	    }
	    return result;
	}

	private static String getTextFromMimeMultipart(
	        MimeMultipart mimeMultipart)  throws MessagingException, IOException{
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            result = result + "\n" + bodyPart.getContent();
	            break; // without break same text appears twice in my tests
	        } else if (bodyPart.isMimeType("text/html")) {
	            String html = (String) bodyPart.getContent();
	            result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
	        } else if (bodyPart.getContent() instanceof MimeMultipart){
	            result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
	        }
	    }
	    return result;
	}
   
   public static String parseAddresses(Address[] address) {
       String listAddress = "";

       if (address != null) {
           for (int i = 0; i < address.length; i++) {
               listAddress += address[i].toString() + ", ";
           }
       }
       if (listAddress.length() > 1) {
           listAddress = listAddress.substring(0, listAddress.length() - 2);
       }

       return listAddress;
   }
 public static List<EmailModel> find(String text,String user,
	      String password, String folderpath) throws MessagingException, IOException {
	 List<EmailModel> emails = new ArrayList<EmailModel>();
	
	   SearchTerm term = new SearchTerm() {
		    
             
			public boolean match(Message message) {
				 String messageContent = "";
				 try {
					 
					messageContent = getTextFromMessage(message) ;
					if(messageContent.contains(text)) {
						return true;
					}
				} catch (MessagingException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        try {
		            if (message.getSubject().contains(text)) {
		                return true;
		            }
		            if (message.getFrom()[0].toString().contains(text)) {
		                return true;
		            }
		            if (FetchEmails.parseAddresses(message.getRecipients(RecipientType.TO)).contains(text)) {
		                return true;
		            }
		        } catch (MessagingException ex) {
		            ex.printStackTrace();
		        }
		        return false;
		    }
		};
		
	         // create properties field
	         Properties properties = new Properties();
	         properties.put("mail.store.protocol", "imap");
	         properties.put("mail.imap.host", Consts.IMAP_HOST);
	         properties.put("mail.imap.port", Consts.IMAP_PORT);
	         properties.put("mail.imap.starttls.enable", "true");
	         Session emailSession = Session.getDefaultInstance(properties);
	         // emailSession.setDebug(true);

	         // create the POP3 store object and connect with the pop server
	         Store store = emailSession.getStore("imaps");

	         store.connect(Consts.IMAP_HOST, user, password);
	         javax.mail.Folder[] folders = store.getDefaultFolder().list("*");
	         for (javax.mail.Folder fold : folders) {
	             if ((fold.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
	                 System.out.println(fold.getFullName() + ": " + fold.getMessageCount());
	             }
	         }
	         // create the folder object and open it
	         Folder folder = store.getFolder(folderpath);

	        
	            folder.open(Folder.READ_ONLY);

		Message[] foundMessages = folder.search(term);
		
		  for (int i = 0; i < foundMessages.length; i++) {

	             Message msg = foundMessages[i];
	             Address[] fromAddress = msg.getFrom();
	           
	             String from = fromAddress[0].toString();
	             String subject = msg.getSubject();
	             String toList = parseAddresses(msg
					         .getRecipients(RecipientType.TO));
	            
	             Date sentDate = msg.getSentDate();

	            
	             String messageContent = "";
	             messageContent = getTextFromMessage(msg) ;
	             
	             EmailModel em = new EmailModel(i+1,from,toList,sentDate,subject,messageContent);
	            emails.add(em);
	         }
	         // close the store and folder objects
	         folder.close(false);
	         store.close();
	return emails;
 }
 }
  
   /* This method checks for content-type 
   * based on which, it processes and
   * fetches the content of the message

   public static void writePart(Part p) throws Exception {
      if (p instanceof Message)
         //Call methos writeEnvelope
         writeEnvelope((Message) p);

      System.out.println("----------------------------");
      System.out.println("CONTENT-TYPE: " + p.getContentType());

      //check if the content is plain text
      if (p.isMimeType("text/plain")) {
         System.out.println("This is plain text");
         System.out.println("---------------------------");
         System.out.println((String) p.getContent());
      } 
      //check if the content has attachment
      else if (p.isMimeType("multipart/*")) {
         System.out.println("This is a Multipart");
         System.out.println("---------------------------");
         Multipart mp = (Multipart) p.getContent();
         int count = mp.getCount();
         for (int i = 0; i < count; i++)
            writePart(mp.getBodyPart(i));
      } 
      //check if the content is a nested message
      else if (p.isMimeType("message/rfc822")) {
         System.out.println("This is a Nested Message");
         System.out.println("---------------------------");
         writePart((Part) p.getContent());
      } 
      //check if the content is an inline image
      else if (p.isMimeType("image/jpeg")) {
         System.out.println("--------> image/jpeg");
         Object o = p.getContent();

         InputStream x = (InputStream) o;
         // Construct the required byte array
         System.out.println("x.length = " + x.available());
         while ((i = (int) ((InputStream) x).available()) > 0) {
            int result = (int) (((InputStream) x).read(bArray));
            if (result == -1)
         int i = 0;
         byte[] bArray = new byte[x.available()];

            break;
         }
         FileOutputStream f2 = new FileOutputStream("/tmp/image.jpg");
         f2.write(bArray);
      } 
      else if (p.getContentType().contains("image/")) {
         System.out.println("content type" + p.getContentType());
         File f = new File("image" + new Date().getTime() + ".jpg");
         DataOutputStream output = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream(f)));
            com.sun.mail.util.BASE64DecoderStream test = 
                 (com.sun.mail.util.BASE64DecoderStream) p
                  .getContent();
         byte[] buffer = new byte[1024];
         int bytesRead;
         while ((bytesRead = test.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
         }
      } 
      else {
         Object o = p.getContent();
         if (o instanceof String) {
            System.out.println("This is a string");
            System.out.println("---------------------------");
            System.out.println((String) o);
         } 
         else if (o instanceof InputStream) {
            System.out.println("This is just an input stream");
            System.out.println("---------------------------");
            InputStream is = (InputStream) o;
            is = (InputStream) o;
            int c;
            while ((c = is.read()) != -1)
               System.out.write(c);
         } 
         else {
            System.out.println("This is an unknown type");
            System.out.println("---------------------------");
            System.out.println(o.toString());
         }
      }

   }
   /*
   * This method would print FROM,TO and SUBJECT of the message
   *
   public static void writeEnvelope(Message m) throws Exception {
      System.out.println("This is the message envelope");
      System.out.println("---------------------------");
      Address[] a;

      // FROM
      if ((a = m.getFrom()) != null) {
         for (int j = 0; j < a.length; j++)
         System.out.println("FROM: " + a[j].toString());
      }

      // TO
      if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
         for (int j = 0; j < a.length; j++)
         System.out.println("TO: " + a[j].toString());
      }

      // SUBJECT
      if (m.getSubject() != null)
         System.out.println("SUBJECT: " + m.getSubject());

   }
*/
