package common;

/**
 * Contains the various types of errors that can be passed into a {@link Message} object.
 * @author etudiant
 * @see Message
 */
public class ErrorTypes {

	public static final String CLIENT_UNKNOWN = "client unknown";
	public static final String ALREADY_CONNECTED = "client already connected";
	
	//do not instanciate
	private ErrorTypes() {}
}
