package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import models.*;

public class homeController implements Initializable{
	@FXML
	private ScrollPane scroll;
	@FXML
	private GridPane grid;
	  @FXML
	    private Button add;

	    @FXML
	    private Button change;

	    @FXML
	    private Button delete;
	    
	    private ListModel lm;
	    private Parent root ;
	    private Dashboard_hamimController parent;
	    private UserModel user;
	    
		public void setData(Dashboard_hamimController parent,UserModel us) {
			this.parent = parent;
			this.user = us;
		}
	    @FXML
	    void alter(ActionEvent event) throws SQLException {

				try {
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ListContent.fxml"));
					Parent newpane = fxmlLoader.load();
					ListContentController sctl = fxmlLoader.<ListContentController>getController();
					Stage Manipulation = new Stage();
					System.out.println("Testing hadchi "+getSelected().getId());
					
					Scene scene = new Scene(newpane);
					Manipulation.setScene(scene);
					Manipulation.show();
					
					ListModel md= getSelected();
					System.out.println("Testing hadchi mera tania "+md.getId());
					//ListContentController lct = ((FXMLLoader) FXMLLoader.load(getClass().getResource("/fxml/ListContent.fxml"))).<ListContentController>getController();
					
					System.out.println("Testing hadchi "+ md.getId());
					sctl.setlistmodel(getSelected());		
					sctl.initerrthin();
					
					Manipulation.setOnCloseRequest(ev -> {
						
								try {
								parent.loadlists();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
						});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
		
	    }

	    @FXML
	    void delete(ActionEvent event) throws SQLException, IOException {
	    
	    System.out.println("size"+grid.getChildren().size());
	 	  Connection conn= DBConnect.getConnection();
	 
			
			String sql ="delete from lists where id="+getSelected().getId()+";";
			String sql2 ="drop table list"+getSelected().getId()+" ;";
			
			pst= conn.prepareStatement(sql);
			
			pst.executeUpdate();
			pst= conn.prepareStatement(sql2);

			pst.executeUpdate();
			
			 
			 
		  parent.loadlists();
	    }

	    

	

	
	private List<ListModel>lists = new ArrayList<>();
	private PreparedStatement pst;
	 private UserModel getl(String name) throws SQLException {
			Connection conn= DBConnect.getConnection(); 
			ResultSet re = conn.createStatement().executeQuery("select * from userinf where Name='"+name+"';");
			if(re.next()) return (new UserModel(re.getString("Name"), re.getString("Email"),re.getString("password")));
			return null;
			}
	
	private List<ListModel> getdata() throws NumberFormatException, SQLException{
		
		List<ListModel>lists = new ArrayList<>();
		
		Connection conn= DBConnect.getConnection(); 
		
		
		ResultSet re = conn.createStatement().executeQuery("select * from lists");
		
		while(re.next()) {
			
			
				lists.add(new ListModel(""+re.getInt("id"), re.getString("name"),re.getInt("mem_num"), re.getString("img"), re.getString("owner")));
		}
		
		
		
		
		return lists; 
	}
	
	private void setSelected(ListModel lst) {
		lm=lst;
	}
	private ListModel getSelected() {
		return lm;
	}
	
	private ListModel getlist(String name) throws SQLException {
		Connection conn= DBConnect.getConnection(); 
		ResultSet re = conn.createStatement().executeQuery("select * from lists where name='"+name+"';");
		if(re.next()) return (new ListModel(""+re.getInt("id"), re.getString("name"), re.getInt("mem_num"), re.getString("img"),re.getString("owner")));
		return null;
	}
	
	public void initdata() {
		
		scroll.setPrefSize(736, 415);
		try {
			lists.add(0, new ListModel("Special", "Créer liste" ,0, "C:/Users/hajar/eclipse-workspace/Testfx/src/img/plus.png",user.getEmail()));
			lists.addAll(this.getdata());
		} catch (NumberFormatException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int column = 0;
        int row = 1;
    	try {

    		  
					

						for (int i = 0; i <lists.size();i++) {
							FXMLLoader fxmlLoader = new FXMLLoader();
			                  fxmlLoader.setLocation(getClass().getResource("/fxml/List.fxml"));
			                  
			                 
			  			
			  					AnchorPane anchorPane = fxmlLoader.load();
			  					ListController listctrl = fxmlLoader.getController();
			  					listctrl.setData(lists.get(i));
			  					if(i==0) {
			  					anchorPane.setOnMouseClicked(
			  						new EventHandler<MouseEvent>() {

									@Override
									public void handle(MouseEvent arg0) {
										FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewList.fxml"));
										
										try {
										 root = (Parent)loader.load();
										 NewListController nw = loader.<NewListController>getController();
										 nw.setdata(user);
											Stage first = new Stage();
											
											Scene scene = new Scene(root);
											first.setScene(scene);
											first.show();
											
											first.setOnCloseRequest(ev -> {
										
											try {
												parent.loadlists();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
									
										});
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										} 
								    	
									}	});
					
					
			  					}else {
			  					anchorPane.setOnMouseClicked(
			  							
	  			  						new EventHandler<MouseEvent>() {

											@Override
											public void handle(MouseEvent arg0) {
												
												try {
												setSelected(getlist(listctrl.getLabel().getText()));
											} catch (SQLException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
			  					}
	  			  						});
			  					}
            
            if (column == 3) {
                column = 0;
                row++;
            }

            grid.add(anchorPane, column++, row); //(child,column,row)
            //set grid width
            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            grid.setMaxWidth(Region.USE_PREF_SIZE);

            //set grid height
            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            grid.setMaxHeight(Region.USE_PREF_SIZE);

            GridPane.setMargin(anchorPane, new Insets(10));
		}
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void initialize(URL arg0, ResourceBundle arg1) {
		
		    	}
	

}

