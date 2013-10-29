package serveur;

import static org.junit.Assert.*;

import java.io.IOException;

import org.jdom2.JDOMException;
import org.junit.Before;
import org.junit.Test;

public class LoginParserTests {
	
	private LoginParser loginParser;

	@Before
	public void setUp() throws JDOMException, IOException {
		loginParser = new LoginParser(Serveur.DEFAULT_LOGIN_FILE_URL);
		loginParser.parse();
	}

	@Test
	public void testHasBeenParsed() {
		assertTrue(loginParser.hasParsed());
		assertFalse(new LoginParser(Serveur.DEFAULT_LOGIN_FILE_URL).hasParsed());
	}

	@Test
	public void testValidateLogin() {
		assertTrue(loginParser.validateLogin("test01", "test"));
		assertFalse(loginParser.validateLogin("NOTtest01", "test"));
		assertFalse(loginParser.validateLogin("test01", "NOTtest"));
		assertFalse(loginParser.validateLogin("NOTtest01", "NOTtest"));
	}

}
