package MailFunctions;

import java.awt.print.Printable;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

public class Credentials {
	private	String username;
	private	String password;
	private Session session;
	private Properties prop = new Properties();
	public Credentials() {
		
	}
	
	public Credentials(final String username, final String password) {
		this.username = username;
		this.password = password;
		
    	prop.put("mail.smtp.host", Consts.SMTP_HOST);
        prop.put("mail.smtp.port", Consts.SMTP_TLS_PORT);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
		this.session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
	}
	
	public boolean auth() {
	
		try {
			Transport t = this.session.getTransport();
			t.connect();
			return true;
		}
		catch(MessagingException e) {
			//e.printStackTrace();
			return false;
		}
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Session getSession() {
		return session;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
