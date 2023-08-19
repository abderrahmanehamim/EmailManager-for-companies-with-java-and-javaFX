package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.search.SearchTerm;

import com.jfoenix.controls.JFXButton;

import MailFunctions.FetchEmails;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.EmailModel;
import models.ListModel;
import models.UserModel;

public class Dashboard_hamimController implements Initializable {
	@FXML
	private JFXButton NewMail;
    @FXML
    private TextField search;
	@FXML
	private JFXButton Inbox;
	@FXML
	private JFXButton Sent;
	@FXML
	private JFXButton Trash;
	@FXML
	private JFXButton MailingList;
	@FXML
	private JFXButton Archive;
	@FXML
	private JFXButton Logout;

    @FXML
    private JFXButton clear;

    @FXML
    private Pane option;

    @FXML
    private VBox load;
    @FXML
    private JFXButton Archivexml;

    @FXML
    private MenuButton vb;


    @FXML
    private JFXButton serchbtn;





	private int current =0;
	private UserModel us = null;
	private List<UserModel> lists;
	private List<EmailModel> emails;
	private PreparedStatement pst;
	private String selected_f;
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		try {
			lists = this.getdata();
		} catch (NumberFormatException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		vb.getItems().clear();
		MenuItem m ;
		for (int i = 0; i <lists.size()+1;i++) {
					if(i==0) {
						m=new MenuItem("Ajouter un nouveau compte");
						m.setOnAction(event -> {
							vb.getScene().getWindow().hide(); 
							try {
							Stage Welcome = new Stage();
							Parent root = FXMLLoader.load(getClass().getResource("/fxml/Auth.fxml"));
							Scene scene = new Scene(root);
							Welcome.setScene(scene);
							Welcome.show();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							});
					}else {
						m = new MenuItem(lists.get(i-1).getEmail());
						String text = m.getText();
						m.setOnAction(event -> {
							   vb.setText(text);
							   this.setcurrent(this.getcurrent(text));
							   System.out.println(us.getName());
							});
					}
					
					vb.getItems().add(m);
					vb.setText(m.getText());
					
					System.out.println("done wut");
					
					
					
		}
		
		
		
	}
	public void setcurrent(UserModel user) {
		us=user;
	}
	
	private UserModel getcurrent(String add) {
		for(UserModel o : lists) {
			if(o.getEmail().equals(add))return o;
		}
		return null;
	}
		
		private List<UserModel> getdata() throws NumberFormatException, SQLException{
			
			lists = new ArrayList<>();
			
			Connection conn= DBConnect.getConnection(); 
			
			
			ResultSet re = conn.createStatement().executeQuery("select * from userinf where connected='true'");
			
			while(re.next()) {
				
					lists.add(new UserModel(re.getString("Name"), re.getString("Email"),re.getString("password")));
			}
		
			return lists; 
		}
		
		private List<EmailModel> getemails() throws NumberFormatException, SQLException{
			
			emails = new ArrayList<>();
			
			Connection conn= DBConnect.getConnection(); 
			
			
			ResultSet re = conn.createStatement().executeQuery("select * from email where tos='"+us.getEmail()+"';");
			
			while(re.next()) {
				
					emails.add(new EmailModel(re.getInt("id"), re.getString("froms"), re.getString("tos"), re.getDate("dates"),  re.getString("subject"), re.getString("body")));
			}
		
			return emails; 
		}
	
	

	
	@FXML
    void newmess(ActionEvent event) throws IOException, SQLException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SendMail.fxml"));
		Pane newpane = fxmlLoader.load();
		SendMailController sctl = fxmlLoader.<SendMailController>getController();
          		//option.setStyle("-fx-background-color: darkred" );
		
		sctl.setdat(us, this);
				 this.loadmess(newpane);
			
    }
	
	public void loadmess(Node nd) {
		this.clearmess();
		option.getChildren().add(nd);
	}
	public void setcol() {
		NewMail.setStyle("-fx-background-color : darkred;");
	}
	public void clearmess() {
		option.getChildren().clear();
		
	}
	private void loadsearchlist(String text,String em, String pwd, String folder) throws MessagingException, IOException {
		emails = FetchEmails.find(text,em, pwd,folder);
		
		if(emails.size()==0) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Empty Inbox");
			System.out.println("welp");
			alert.show();
		}else {
		
		
		for(int i=0; i<emails.size();i++) {
			
		//	System.out.println(emails.get(i).getBody());
			try {
			FXMLLoader fxmlLoader = new FXMLLoader();
	          
			
            fxmlLoader.setLocation(getClass().getResource("/fxml/User_choose.fxml"));
			Pane p = fxmlLoader.load();
			 
			User_chooseController txtctl = fxmlLoader.<User_chooseController>getController();
			txtctl.initdata(emails.get(i),us,folder);
			EmailModel eml = emails.get(i);
			
			Dashboard_hamimController ctl = this;
			p.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
					try { FXMLLoader fxmlLoad = new FXMLLoader();
			          
						
			            fxmlLoad.setLocation(getClass().getResource("/fxml/ShowMail.fxml"));
			  
						
							Pane m = fxmlLoad.load();
							ShowMailController sw = fxmlLoad.<ShowMailController>getController();
							sw.setdata(eml,us,folder);
							sw.setParent(ctl);
							ctl.loadmess(m);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
				}
					
			});
				 load.getChildren().add(p); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
	         
		}

	private void loadlist(String em, String pwd, String folder) {
		emails = FetchEmails.fetch(em, pwd,folder);
		//emails = this.getemails();
		//Show In_box**********************
		if(emails.size()==0) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Empty Inbox");
			System.out.println("welp");
			alert.show();
		}else {
		
		
		for(int i=0; i<emails.size();i++) {
			
		//	System.out.println(emails.get(i).getBody());
			try {
			FXMLLoader fxmlLoader = new FXMLLoader();
	          
			
            fxmlLoader.setLocation(getClass().getResource("/fxml/User_choose.fxml"));
			Pane p = fxmlLoader.load();
			 
			User_chooseController txtctl = fxmlLoader.<User_chooseController>getController();
			txtctl.initdata(emails.get(i),us,folder);
			EmailModel eml = emails.get(i);
			
			Dashboard_hamimController ctl = this;
			p.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
					try { FXMLLoader fxmlLoad = new FXMLLoader();
			          
						
			            fxmlLoad.setLocation(getClass().getResource("/fxml/ShowMail.fxml"));
			  
						
							Pane m = fxmlLoad.load();
							ShowMailController sw = fxmlLoad.<ShowMailController>getController();
							sw.setdata(eml,us,folder);
							sw.setParent(ctl);
							ctl.loadmess(m);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
				}
					
			});
				 load.getChildren().add(p); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
	         
		}
	}
	public void loadinbox() {
		this.setSelected_f("INBOX");
		  this.clearmess();
		  load.getChildren().clear();
		  this.loadlist(us.getEmail(), us.getPassword(), "INBOX");
	    }
	  @FXML
	    void loadinbox(ActionEvent event) {
		  this.loadinbox();
	    }
	  
	  @FXML
	    void loadsent(ActionEvent event) {
		  System.out.println("done");
		  this.setSelected_f("[Gmail]/Sent Mail");
		  this.clearmess();
		  load.getChildren().clear();
		  this.loadlist(us.getEmail(), us.getPassword(), "[Gmail]/Sent Mail");
		
		  
	    }
	  
	  @FXML
	    void loadArchive(ActionEvent event) {

		  this.setSelected_f("[Gmail]/Archive");
		  this.clearmess();
		  load.getChildren().clear();
		  this.loadlist(us.getEmail(), us.getPassword(), "[Gmail]/Archive");
	    }
	  
	  @FXML
	    void logout(ActionEvent event) throws SQLException, IOException {
			
			Connection conn= DBConnect.getConnection(); 
			String sql = "update userinf set connected='false' where email='"+us.getEmail()+"';";
			pst= conn.prepareStatement(sql);
			pst.executeUpdate();
			Logout.getScene().getWindow().hide(); 
			Stage Welcome = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/Auth.fxml"));
			Scene scene = new Scene(root);
			Welcome.setScene(scene);
			Welcome.show();
			
	    }
	  @FXML
	    void loadlists(ActionEvent event) throws IOException {
			this.loadlists();
		
	    }
	  public void loadlists() throws IOException {
		  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
          
	         
			Pane newpane = fxmlLoader.load();
			homeController sw = fxmlLoader.<homeController>getController();
			sw.setData(this,us);
			sw.initdata();
			 this.loadmess(newpane);
	  }

	    @FXML
	    void loadtrash(ActionEvent event) {
	    	this.setSelected_f("[Gmail]/Trash");
	    	this.clearmess();
	    	load.getChildren().clear();
			  this.loadlist(us.getEmail(), us.getPassword(), "[Gmail]/Trash");
	    }
	    
	    @FXML
	    void searchsm(ActionEvent event) throws MessagingException, IOException {
	    this.clearmess();
	   load.getChildren().clear();
	    	this.loadsearchlist(search.getText(), us.getEmail(), us.getPassword(), selected_f);
	    	System.out.println("hello");
	    }
	    @FXML
	    void clear(ActionEvent event) {
	    	search.setText("");
	    	this.clearmess();
	    	load.getChildren().clear();
	    	 this.loadlist(us.getEmail(), us.getPassword(), selected_f);
	    }

		public void setSelected_f(String selected_f) {
			this.selected_f = selected_f;
		}

}
