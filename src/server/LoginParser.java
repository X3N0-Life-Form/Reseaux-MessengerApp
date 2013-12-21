package server;

import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * This class parses and stores the login file in memory. It can then be used to authenticate a client.
 * @author etudiant
 * @see Server
 */
public class LoginParser {
	
	private File file;
	private Document doc = null;;
	
	/**
	 * Constructs a LoginParser for the specified file.
	 * <br />Note that the file still needs to be parsed.
	 * @param fileURL
	 */
	public LoginParser(String fileURL) {
		file = new File(fileURL);
	}
	
	/**
	 * Parses the file, allowing the class to authenticate (or not) a client.
	 * @throws JDOMException
	 * @throws IOException
	 */
	public void parse() throws JDOMException, IOException {
		SAXBuilder sax = new SAXBuilder();
		doc = sax.build(file);
		//Note: we assume that the document is valid
	}
	
	public boolean hasParsed() {
		return doc != null;
	}
	
	public File getFile() {
		return file;
	}
	
	/**
	 * Are the specified login and password valid?
	 * @param login - Client login.
	 * @param pass - Client password.
	 * @return True if they are.
	 */
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
