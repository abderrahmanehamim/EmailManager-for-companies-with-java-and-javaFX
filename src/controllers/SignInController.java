package controllers;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;

import MailFunctions.Authentication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignInController implements Initializable{
	
	@FXML
    private VBox VBox;
	private Parent fxml ;

    @FXML
    private JFXButton btn_login;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
      public void openHome() {
    	if(Authentication.connect(txtEmail.getText(), txtPassword.getText())== true) {
    		System.out.println("Connexion reussie");
        	VBox.getScene().getWindow().hide();
        	Stage home = new Stage() ;
         	try {
 				fxml = FXMLLoader.load(getClass().getClassLoader().getResource("interfaces/Dashboard.fxml"));
 				Scene scene = new Scene(fxml);
				home.setScene(scene);
				home.show();
 			} catch (IOException e) {
 				
 				e.printStackTrace();
 			}
			
    	}else {
    		Alert alert = new Alert(AlertType.ERROR, "Your email or your password is incorrect",javafx.scene.control.ButtonType.OK);
         	alert.showAndWait();
    	}
    }   

    
	@Override
	public void initialize(URL location, ResourceBundle resources) {		    	 
		
		
		
	}

}
