package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;

import com.jfoenix.controls.JFXButton;

import MailFunctions.ArchiveEmails;
import MailFunctions.Credentials;
import MailFunctions.Mail_saver_xml;
import javafx.scene.control.Label;

import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import models.EmailModel;
import models.UserModel;

public class ShowMailController {
	@FXML
	private JFXButton Send;
	@FXML
	private Label Subject;
	@FXML
	private JFXButton Send1;
	@FXML
	private Label From;
	@FXML
    private Label To;

	@FXML
	private Label Let;
	@FXML
	private Label time;
	@FXML
	private TextArea body;  
	@FXML
    private ImageView dn;
	  @FXML
	   private ImageView ar;
	private String folder;
	private EmailModel m;
	private UserModel u;
	private Dashboard_hamimController parent;
	
	   private String firstTwo(String str) {
	        return str.length() < 2 ? str : str.substring(0, 2);
	    }
	
	public void setdata(EmailModel em, UserModel us, String folder) {
		u=us;
		m=em;
		this.folder= folder;
		Subject.setText(em.getSubject());
		From.setText(em.getFrom());
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(em.getDate());
    	time.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
		To.setText("A : "+em.getTo());
		Let.setText(firstTwo(em.getFrom()));
		body.setText(em.getBody());
		body.setEditable(false);
		if(folder.equals("[Gmail]/Archive")) {
			Send.setDisable(true);
			Send1.setDisable(true);
			dn.setVisible(false);
			ar.setVisible(false);
		}
		
	}

    @FXML
    void down(ActionEvent event) throws IOException {
    	String filename ="C:\\Users\\hajar\\eclipse-workspace\\Testfx\\src\\files\\file"+m.getId()+".txt";
    	    BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
    	   
    	    writer.append("Message \n From: "+m.getFrom()+"\n To :"+m.getTo()+"\n");
    	    writer.append("Message Content***********\n");
    	    writer.append(m.getBody());
    	    
    	    writer.close();
    	    System.out.println("Message copied.......");
    	}
    
	   @FXML
	    void Archive(ActionEvent event) throws MessagingException, IOException {

			Credentials cr= new Credentials(u.getEmail(),u.getPassword());
			Mail_saver_xml xml= new Mail_saver_xml(cr);
			xml.add_mail(m);		
			System.out.println(m.getFrom());
		   SearchTerm term = new SearchTerm() {
			    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				public boolean match(Message message) {
			        try {
			            if (message.getSubject().contains(Subject.getText())) {
			                return true;
			            }
			        } catch (MessagingException ex) {
			            ex.printStackTrace();
			        }
			        return false;
			    }
			};

			ArchiveEmails.archive(u.getEmail(), u.getPassword(), term, folder);
			parent.clearmess();
			parent.loadinbox();
			System.out.println("Done");
		
	    }

	public void setParent(Dashboard_hamimController parent) {
		this.parent = parent;
	}

}
