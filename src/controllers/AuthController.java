package controllers;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import com.jfoenix.controls.JFXButton;

import MailFunctions.*;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.UserModel;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.PasswordField;

public class AuthController {
	@FXML
	private TextField un;
	@FXML
	private PasswordField pwd;
	@FXML
	private Label letat;
	@FXML
	private Button login;

	private PreparedStatement pst ;

	public String getUsername(String add) {
		String username =add;
		    username.replaceAll("((@.*)|[^a-zA-Z])+", " ").trim();
		return username.replaceAll("((@.*)|[^a-zA-Z])+", " ").trim();
	} 
    @FXML
    void check(ActionEvent event) throws MessagingException, SQLException {
    	Credentials cr = new Credentials(un.getText(),pwd.getText());
    	  //Authentication.connect(un.getText(), pwd.getText())
    			if(cr.auth()) {
    				
    				String name = this.getUsername( un.getText());
    			
    				Connection conn= DBConnect.getConnection(); 
    				String sql = "insert into userinf(Name, Email, connected, password) values ('"+name+"','"+un.getText()+"', 'true','"+pwd.getText()+"');";
    				System.out.println(sql);
    				pst= conn.prepareStatement(sql);
    				pst.executeUpdate();
    				
    				letat.setTextFill(Color.LAWNGREEN);
    	    		letat.setText("Connexion reussie");
    	    		PauseTransition delay = new PauseTransition();
    	    		delay.setDuration(Duration.seconds(3));
    	    		delay.setOnFinished( e ->{
    	    			login.getScene().getWindow();
    	    			login.getScene().getWindow().hide(); 
    	                Stage Manipulation = new Stage();
    	         	
    	    		
    	    				try {
    	    					FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard_hamim.fxml"));
    	    					Parent root = loader.load();
    	    					
    	    					Dashboard_hamimController ctl = loader.<Dashboard_hamimController>getController();
    	    					UserModel us = new UserModel(name,un.getText(),pwd.getText());
    	    					ctl.setcurrent(us);
    	    					Scene scene = new Scene(root); 
    	                 		Manipulation.setScene(scene);
    	                 		Manipulation.show();
    	    				} catch (IOException e1) {
    	    					// TODO Auto-generated catch block
    	    					e1.printStackTrace();
    	    				}
    	    				
    	    			
    	    			
    	                });
    	    		delay.play();
    	    		
    	     
    	    		
    	    	}else {
    	    		letat.setTextFill(Color.RED);
    	    		letat.setText("Mot de pass ou username incorrect!!");
    	    		
    	    	}
    			}
    }
	

