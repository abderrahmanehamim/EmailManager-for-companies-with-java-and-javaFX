package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import models.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;

public class NewListController {
	@FXML
	private TextField name;
	@FXML
	private Button create;
	private PreparedStatement pst;
	private UserModel user;
	// Event Listener on Button[#create].onAction
	
	public void setdata(UserModel user) {
		this.user=user;
	}
	@FXML
	public void newlist(ActionEvent event) throws SQLException {
		
		Connection conn= DBConnect.getConnection(); 
		ResultSet re = conn.createStatement().executeQuery("select id,name,count(name) from lists where name='"+name.getText()+"';");
		re.next();
		int count=re.getInt("count(name)");
		if(count!=0) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Changez le nom");
			alert.show();
		}else {
			String sql = "insert into lists(name, mem_num, img,owner) values ('"+name.getText()+"',0,'C:/Users/hajar/eclipse-workspace/Testfx/src/img/group.png','"+user.getEmail()+"');";
			System.out.println(sql);
			pst= conn.prepareStatement(sql);
			pst.executeUpdate();
			re = conn.createStatement().executeQuery("select id from lists where name='"+name.getText()+"';");
			re.next();
			String sql2= "create table List"+re.getInt("id")+" (id int not null auto_increment primary key, Uname varchar(50), Email varchar(60) );";
			System.out.println(sql2);
			pst= conn.prepareStatement(sql2);
			pst.executeUpdate();
		}
		
	}
}
