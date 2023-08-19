package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;

import controllers.DBConnect;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.ListItemModel;
import models.ListModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

public class ListContentController implements Initializable{
	@FXML
	private TableView<ListItemModel> table;
	@FXML
	private TableColumn<ListItemModel, String> id;
	@FXML
	private TableColumn<ListItemModel, String> Uname;
	@FXML
	private TableColumn<ListItemModel, String> Email;
	@FXML
	private TextField ins_email;
	@FXML
	private TextField ins_uname;
	@FXML
	private Button Ajouter;
	
	@FXML
	private Button Update;
	@FXML
	private Button Supprimer;
	@FXML
	private TextField code_client;
	private ObservableList<ListItemModel> data = FXCollections.observableArrayList();
	private ListModel lt;
	private String query;
	private String query2;
	private PreparedStatement pst;
	
	private ListModel getlist() throws SQLException {
		return lt;
	}
	public void setlistmodel(ListModel mdel) throws SQLException {
		lt=mdel;
		System.out.println("inside controller"+lt.getId());
	}
	public void loaddata() {
		System.out.println("inside controller"+lt.getId());
		try {
		Connection conn= DBConnect.getConnection();
		data.clear();
		
		
		
		ResultSet re = conn.createStatement().executeQuery("select * from List"+lt.getId()+" ;");
		
		while(re.next()) {
			
				data.add(new ListItemModel(re.getString("id"),re.getString("Uname"),re.getString("Email")));
		}}
		catch (SQLException e) {
			Logger.getLogger(ListContentController.class.getName()).log(Level.SEVERE, null, e);
		}
		
		table.setItems(data);
		
	}
	@FXML
	public void add(ActionEvent event) throws SQLException {
		Connection conn= DBConnect.getConnection();
    	
    	query = "INSERT INTO List"+lt.getId()+"(Uname,Email)  VALUES('"+ins_uname.getText()+"', '"+ins_email.getText()+"');";
    	query2= "Update lists set mem_num=(select count(*) from list"+lt.getId()+") where id="+lt.getId()+";";
    	pst= conn.prepareStatement(query);
    	pst.executeUpdate();
    	  pst =conn.prepareStatement(query2);
    	  pst.executeUpdate();
    	this.loaddata();
		
		ins_uname.setText("");
		ins_email.setText("");
	}

	// Event Listener on JFXButton[#Update].onAction
	@FXML
	public void update(ActionEvent event) throws SQLException {

		Connection conn= DBConnect.getConnection();
    	
    	query = "UPDATE List"+lt.getId()+" set Uname='"+ins_uname.getText()+"', Email='"+ins_email.getText()+"';";
    	pst= conn.prepareStatement(query);
    	pst.executeUpdate();
    	this.loaddata();
		
		ins_uname.setText("");
		ins_email.setText("");
	}

	// Event Listener on JFXButton[#Supprimer].onAction
	@FXML
	public void delete(ActionEvent event) throws SQLException {
		int num;
    	ListItemModel item = table.getSelectionModel().getSelectedItem();
      
      num= Integer.parseInt(item.getId());
 	  Connection conn= DBConnect.getConnection();
 	  System.out.println(num);
		
		String sql ="delete from List"+lt.getId()+" where id="+num+";";
		query2= "Update lists set mem_num=(select count(*) from list"+lt.getId()+") where id="+lt.getId()+";";
		pst= conn.prepareStatement(sql);
		pst.executeUpdate();
	  pst =conn.prepareStatement(query2);
	  pst.executeUpdate();
		this.loaddata();
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}
	
	public void initerrthin() {
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		Uname.setCellValueFactory(new PropertyValueFactory<>("Uname"));
		Email.setCellValueFactory(new PropertyValueFactory<>("Email"));
		
		System.out.println(lt);
		
		System.out.println("inside controller"+lt.getId());
		try {
		Connection conn= DBConnect.getConnection();
		data.clear();
		
		ListModel lm = this.getlist();
		
		ResultSet re = conn.createStatement().executeQuery("select * from List"+lm.getId()+" ;");
		
		while(re.next()) {
			
				data.add(new ListItemModel(re.getString("id"),re.getString("Uname"),re.getString("Email")));
		}}
		catch (SQLException e) {
			Logger.getLogger(ListContentController.class.getName()).log(Level.SEVERE, null, e);
		}
		
		table.setItems(data);
		 
		FilteredList<ListItemModel> filteredData = new FilteredList<>(data, b -> true);
		
		code_client.textProperty().addListener((observable , oldValue, newValue) ->{
			
			filteredData.setPredicate(item -> {
				
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (item.getEmail().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				else if (item.getId().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
					return true;
				}else if (item.getUname().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
					return true;
				}
				return false;
			});
			
			SortedList<ListItemModel> sortedData = new SortedList<>(filteredData);
			
			sortedData.comparatorProperty().bind(table.comparatorProperty());
			
			table.setItems(sortedData);
		});
		
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	ListItemModel item = table.getSelectionModel().getSelectedItem();
				
				ins_uname.setText(item.getUname());
				ins_email.setText(item.getEmail());
		    }
		});
		
	}
}
