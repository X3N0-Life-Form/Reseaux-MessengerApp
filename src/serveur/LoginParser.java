package serveur;

import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class LoginParser {
	
	private File file;
	private Document doc = null;;
	
	public LoginParser(String fileURL) {
		file = new File(fileURL);
	}
	
	public void parse() throws JDOMException, IOException {
		SAXBuilder sax = new SAXBuilder();
		doc = sax.build(file);
		//TODO: verify that the document is valid
	}
	
	public boolean hasParsed() {
		return doc != null;
	}
	
	public File getFile() {
		return file;
	}
	
	public boolean validateLogin(String login, String pass) {
		if (doc == null) { // doc hasn't been parsed
			return false;
		}
		Element root = doc.getRootElement();
		for (Element current : root.getChildren()) {
			if (current.getAttributeValue("login").equals(login)
					&& current.getAttributeValue("pass").equals(pass)) {
				return true;
			}
		}
		return false;
	}
}
