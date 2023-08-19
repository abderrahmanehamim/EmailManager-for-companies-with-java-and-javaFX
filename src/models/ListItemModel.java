package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ListItemModel {
	private StringProperty id;
	private StringProperty Uname;
	private StringProperty Email;
	
	public ListItemModel( String id ,String Uname, String Email) {
		this.id = new SimpleStringProperty(id);
		this.Uname = new SimpleStringProperty(Uname);
		this.Email = new SimpleStringProperty(Email);
		
		
		
	}

	public String getId() {
		return id.get();
	}
	public StringProperty IDProperty() {
		return id;
	}

	public String getUname() {
		return Uname.get();
	}
	public StringProperty RaisonProperty() {
		return Uname;
	}

	public String getEmail() {
		return Email.get();
	}
	public StringProperty EmailProperty() {
		return Email;
	}

	
	public void setId(String numClient) {
		id.set(numClient);
	}

	public void setUname(String raisonS) {
		Uname.set(raisonS);
	}

	public void setEmail(String ville) {
		Email.set(ville);
	}


}