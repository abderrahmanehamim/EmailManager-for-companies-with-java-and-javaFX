package MailFunctions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import models.EmailModel;


public class Mail_saver_xml {
	
	private String filepath;
	private Document doc;
	private Element root;
	private Credentials c;
	
	public Mail_saver_xml(Credentials c) {
		this.c = c;
		this.filepath = Consts.XML_PATH.concat(c.getUsername()).concat(".xml");
		if(!new File(this.filepath).exists()) {
			System.out.println("Creating new xml file");
			File file = new File(this.filepath);
			try {
				file.createNewFile();
				FileWriter writer = new FileWriter(this.filepath);
				writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
						+ "<emails>\r\n"
						+ "</emails>");
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		load();
	}

	private void save() {
		try {
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			out.output(doc, new FileOutputStream(this.filepath));
		} catch (Exception e) {
			System.out.println("Erreur : " + e.getMessage());
		}
	}

	private void load() {
		try {
			SAXBuilder sxb = new SAXBuilder();
			doc = sxb.build(new File(this.filepath));
			this.root = doc.getRootElement();
		} catch (Exception e) {
			System.out.println("Erreur : " + e.getMessage());
		}
		
	}

	public int add_mail(EmailModel mail) {
		if(mail != null) {
			Element e = new Element("email");
			Attribute a1 = new Attribute("id", ""+mail.getId());
			Element a2 = new Element("from");
			Element a3 = new Element("to");
			Element a4 = new Element("subject");
			Element a5 = new Element("body");
			Element a6 = new Element("date");
			e.setAttribute(a1);
			a2.addContent(mail.getFrom());
			a3.addContent(mail.getTo());
			a4.addContent(mail.getSubject());
			a5.addContent(mail.getBody());
			a6.addContent(mail.getDate().toString());
			
			e.addContent(a2);
			e.addContent(a3);
			e.addContent(a4);
			e.addContent(a5);
			e.addContent(a6);
			root.addContent(e);
			save();
			return 0;
		}else return 1;
	}

	
	public int delete_mail(EmailModel m) {
		List<Element> l = this.root.getChildren();
		if(l != null) {
			for(Element e : l) {
				if(e.getAttributeValue("id").equals(""+m.getId())) {
					root.removeContent(e);
					save();
					return 0;
				}
			}
			return 1;
		}
		return 1;
	}
	
	public ArrayList<EmailModel> get_Mails() throws ParseException{
		List<EmailModel> list = new ArrayList<EmailModel>();
		List<Element> l = root.getChildren();
		if(l != null) {
			LocalDate date ;
			for (Element e : l) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

				Date dat = formatter.parse(e.getChildText("date"));
				EmailModel em = new EmailModel(Integer.parseInt(e.getAttributeValue("id")), e.getChildText("from"), e.getChildText("to"),dat,e.getChildText("subject"),e.getChildText("body"));
				if(Boolean.parseBoolean(e.getChildText("is_archived")))em.set_archived(true);
				if(Boolean.parseBoolean(e.getChildText("is_attached"))) {
					em.set_archived(true);
				}
				list.add(em);
			}
		}
		return (ArrayList<EmailModel>) list;
	}
}
