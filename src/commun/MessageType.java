package commun;

/**
 * Message type enumeration.
 * @author etudiant
 * @see Message
 */
public enum MessageType {
	CONNECT,
	REQUEST_LIST,
	DISCONNECT,
	UNKNOWN,
	OK,
	ERROR,
	CLIENT_LIST,
	CLIENT_PORT_LIST,
	LIVE,
	MSG_DISCUSS_CLIENT,
	REQUEST_CLIENT_IP
}
