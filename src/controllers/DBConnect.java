package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnect {
	

	
	
	
	public static Connection getConnection() throws SQLException {
		
	
	
		  
		  
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mailmanager?useUnicode=true&characterEncoding=UTF-8","root","MrW1LS0N*");
			
		
		
		return(connection);
	}
}
	
	
	
