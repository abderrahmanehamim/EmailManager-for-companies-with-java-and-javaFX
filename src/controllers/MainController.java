package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;



public class MainController implements Initializable{

	@FXML
    private JFXButton btnSignIn;

    @FXML
    private VBox VBox;
    private Parent fxml ;

    @FXML
    private ImageView image;

	private MouseEvent event;

    @FXML
    void openSignIn() {
    	TranslateTransition t = new TranslateTransition(Duration.seconds(0.5),VBox);
        t.setToX(325);
        t.play();
        t.setOnFinished(e ->{
        	try{
            	fxml= FXMLLoader.load(getClass().getResource("/interfaces/SignIn.fxml"));
            	VBox.getChildren().removeAll();
            	VBox.getChildren().setAll(fxml);
            }catch(Exception e1 ){
            		e1.printStackTrace();
            	}
        });
    }

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
            TranslateTransition t = new TranslateTransition(Duration.seconds(0.5),VBox);
	        t.setToX(VBox.getLayoutX() * 5.5);
	        t.play();
	        
		
	}

}


