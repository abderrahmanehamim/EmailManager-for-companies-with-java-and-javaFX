package application;
	
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import controllers.DBConnect;
import controllers.Dashboard_hamimController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import models.UserModel;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws SQLException {
		
		int count=0;
		
	Connection conn= DBConnect.getConnection(); 
			
			
			ResultSet re = conn.createStatement().executeQuery("select *,count(*) from userinf where connected='true';");
			re.next();
			count=re.getInt("count(*)");
			
			if(count!=0) {
				try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard_hamim.fxml"));
				Parent root = loader.load();
				
				Dashboard_hamimController ctl = loader.<Dashboard_hamimController>getController();
				UserModel us = new UserModel(re.getString("Name"),re.getString("Email"),re.getString("Password"));
				ctl.setcurrent(us);
				Scene scene = new Scene(root,1170,600);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.setResizable(false);	
				primaryStage.show();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			try{Parent root = FXMLLoader.load(getClass().getResource("/fxml/Auth.fxml"));
			Scene scene = new Scene(root,849,577);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		}
			
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
