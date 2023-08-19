package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Locale;
import java.util.Scanner;

import MailFunctions.Consts;

public class EmailModel {
	private int count;
	private int id;
	private Date date;
	private String from;
	private String to;
	private String subject;
	private String body; //could be changed to html element later
	private boolean is_attached;
	private boolean is_archived;
//	private ArrayList<Attachement> path;
	
	public EmailModel() {
		this.load_count();
		//this.date = new Date();
		this.count++;
		this.id = this.count;
		//this.path = new ArrayList<Attachement>();
		this.save_count();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public EmailModel(int id,String from, String to, Date date, String subject, String body) {
		this.id = id;
		//this.path = new ArrayList<Attachement>();
		//this.date = new Date();
		//SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
		this.date=date;
			//this.date = formatter.parse(date);
		
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.body = body;
		this.is_attached = false;
		this.is_archived = false;
	}

	public boolean isIs_attached() {
		return is_attached;
	}

	public void setIs_attached(boolean is_attached) {
		this.is_attached = is_attached;
	}

	public boolean isIs_archived() {
		return is_archived;
	}

	public void setIs_archived(boolean is_archived) {
		this.is_archived = is_archived;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public boolean get_archived() {
		return is_archived;
	}

/*	public ArrayList<Attachement> getPath() {
		return path;
	}

	public void setPath(ArrayList<Attachement> path) {
		this.path = path;
	}
*/
	public void set_archived(boolean is_archived) {
		this.is_archived = is_archived;
	}

	public boolean get_attached_status() {
		return is_attached;
	}

	public void set_attached(boolean is_attached) {
		this.is_attached = is_attached;
	}

	public int getId() {
		return id;
	}
	
	private void save_count() {
		 try {
		      FileWriter myWriter = new FileWriter(Consts.COUNT_PATH);
		      myWriter.write(""+this.count);
		      myWriter.close();
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
	private void load_count() {
		 try {
		      File myObj = new File(Consts.COUNT_PATH);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        this.count =Integer.parseInt(myReader.nextLine());
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
	public static void reset_count() {
		try {
		      FileWriter myWriter = new FileWriter(Consts.COUNT_PATH);
		      myWriter.write(""+0);
		      myWriter.close();
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}

	public String toString() {
		return "Email [id=" + id + ", date=" + date + ", from=" + from + ", to=" + to + ", subject=" + subject
				+ ", body=" + body + ", is_attached=" + is_attached + ", is_archived=" + is_archived + ", path=" 
				+ "]";
	}
	
}
