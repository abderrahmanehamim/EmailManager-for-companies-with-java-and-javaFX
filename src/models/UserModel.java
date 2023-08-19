package models;

public class UserModel {
	private static int count;
	private String Name;
	private String Email;
	private String Password;
	private int id;
	
	public UserModel() {
		count++;
		this.id = count;
	}

	public UserModel(String name, String email, String pwd) {
		this();
		Name = name;
		Email = email;
		Password = pwd;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}
	
}
