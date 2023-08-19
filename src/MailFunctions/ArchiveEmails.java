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
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;

import controllers.DBConnect;
import models.EmailModel;



public class ArchiveEmails {
	
	public ArchiveEmails() {
		super();
		// TODO Auto-generated constructor stub
	}

   public static void archive(String user,
      String password, SearchTerm term, String folder) throws MessagingException {
	   
   
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
      
         // create the folder object and open it
         Folder emailFolder = store.getFolder(folder);
         emailFolder.open(Folder.READ_WRITE);
         Folder eolder = store.getFolder("[Gmail]/Archive");
  
         if (!eolder.exists())
             if (eolder.create(Folder.HOLDS_MESSAGES))
                 System.out.println("Folder was created successfully");
         Message[] messages = emailFolder.search(term);
         emailFolder.copyMessages(messages, store.getFolder("[Gmail]/Archive"));
  	 	 emailFolder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
    	   System.out.println(messages.length); 
       
      
      }
      
   }