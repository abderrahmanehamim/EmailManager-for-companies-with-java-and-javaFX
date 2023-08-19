package controllers;

import models.EmailModel;
import models.UserModel;

import java.io.IOException;
import java.util.Calendar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class User_chooseController {
	   @FXML
	    private Label Body;

	    @FXML
	    private Label Let;

	    @FXML
	    private Label Subject;

	    @FXML
	    private Label Username;
	    @FXML
	    private Label time;
	    
	    private EmailModel e ;
	    private UserModel u;
	    private String folder;
	    private String firstTwo(String str) {
	        return str.length() < 2 ? str : str.substring(0, 2);
	    }
	    public void initdata(EmailModel em, UserModel us, String folder) {
	    	u = us;
	    	this.folder=folder;
	    	e=em;
	    	Let.setText(firstTwo(em.getFrom()));
	    	Username.setText(em.getFrom());
	    	Subject.setText(em.getSubject());
	    	Body.setText(em.getBody());
	    	Calendar calendar = Calendar.getInstance();
	        calendar.setTime(em.getDate());
	    	time.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
	    }

	    @FXML
	    void ShowMessage(MouseEvent event) throws IOException {
	    		 FXMLLoader fxmlLoader = new FXMLLoader();
		          
				
	            fxmlLoader.setLocation(getClass().getResource("/fxml/ShowMail.fxml"));
	  
				Pane p = fxmlLoader.load();
				ShowMailController sw = fxmlLoader.<ShowMailController>getController();
				sw.setdata(e,u,folder);
				FXMLLoader fxmlLoa= new FXMLLoader();
						fxmlLoa.setLocation(getClass().getResource("/fxml/Dashboard_hamim.fxml"));
						fxmlLoa.load();
				Dashboard_hamimController ctl = fxmlLoa.<Dashboard_hamimController>getController();
				
				ctl.loadmess(p);
				
	    }
	
	
	

}
