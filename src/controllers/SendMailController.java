package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import MailFunctions.SendMail;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import com.jfoenix.controls.JFXButton;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import models.ListItemModel;
import models.ListModel;
import models.UserModel;

public class SendMailController {
	@FXML
	private Pane option;
	@FXML
	private TextField Sendto;
	@FXML
	private TextField Subject;
	@FXML
	private TextField Message;
	@FXML
	private JFXButton Send;
	 @FXML
	  private Button browse;
	    @FXML
	    private MenuButton mail;
	    private ListModel md;
	    private String ls;
	    public void setmod(ListModel m) {
	    	md = m;
	    }

	    
	    private ListModel getlist(String name) throws SQLException {
			Connection conn= DBConnect.getConnection(); 
			ResultSet re = conn.createStatement().executeQuery("select * from lists where name='"+name+"';");
			if(re.next()) return (new ListModel(""+re.getInt("id"), re.getString("name"), re.getInt("mem_num"), re.getString("img"),re.getString("owner")));
			return null;
		}
	    private String getString(ListModel name) throws SQLException {
List<ListItemModel>lists = new ArrayList<>();
			String list = "";
			Connection conn= DBConnect.getConnection(); 
			
			
			ResultSet re = conn.createStatement().executeQuery("select * from List"+name.getId()+" ;");
			re.next();
			list+=re.getString("Email");
			while(re.next()) {
					System.out.println(list);
					list+=" , "+re.getString("Email")+"";
					System.out.println(list);
					//lists.add(new ListItemModel(""+re.getInt("id"), re.getString("Uname"),re.getString("Email")));
			}
		return list;
	    }
	    private List<ListModel> getmailinglists() throws SQLException {
	    	List<ListModel>lists = new ArrayList<>();
			
			Connection conn= DBConnect.getConnection(); 
			
			
			ResultSet re = conn.createStatement().executeQuery("select * from lists");
			
			while(re.next()) {
				
				
					lists.add(new ListModel(""+re.getInt("id"), re.getString("name"),re.getInt("mem_num"), re.getString("img"),re.getString("owner")));
			}
			
			
			
			
			return lists; 
	    }
	    	
	    	
	    
	 private List<File> f = null;

	    @FXML
	    void browseev(ActionEvent event) {
	    		FileChooser fc = new FileChooser();
	    		fc.getExtensionFilters().add(new ExtensionFilter("PDF Files","*.pdf"));
	    		 f = fc.showOpenMultipleDialog(null);
	    		
	    }
	
	private UserModel u;
	private Dashboard_hamimController parent;
	
	public void setdat(UserModel us, Dashboard_hamimController pr ) throws SQLException {
		mail.getItems().clear();
		MenuItem m ;
		for (int i = 0; i <getmailinglists().size();i++) {
			final Integer innerMi = new Integer(i);
						m=new MenuItem(getmailinglists().get(i).getName());
					
						
						m.setOnAction(event -> {
							try {
								ls=this.getString(getmailinglists().get(innerMi));
								Sendto.setText(ls);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
						} );
						mail.getItems().add(m);
						}
		u=us;
		parent=pr;
	}
	// Event Listener on JFXButton[#Send].onAction
	@FXML
	public void Sendmessage(ActionEvent event) throws MessagingException {
		System.out.println(ls);
		if(!ls.equals("")) {
		if(f ==null)SendMail.sendMail(u.getEmail(), u.getPassword(), ls, Subject.getText(), Message.getText());
		else SendMail.sendMail(u.getEmail(), u.getPassword(), ls, Subject.getText(), Message.getText(),f);
		}
		else {
			if(f == null)SendMail.sendMail(u.getEmail(), u.getPassword(), Sendto.getText(), Subject.getText(), Message.getText());
			else SendMail.sendMail(u.getEmail(), u.getPassword(), Sendto.getText(), Subject.getText(), Message.getText(),f);
		}
		Sendto.setText("");
		Subject.setText("");
		Message.setText("");
	}
}
