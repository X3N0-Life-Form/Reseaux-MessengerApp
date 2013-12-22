package common;

/**
 * Message type enumeration.
 * @author etudiant
 * @see Message
 */
public enum MessageType {
	CONNECT,
	REQUEST_LIST,
	REQUEST_IP,
	DISCONNECT,
	DISCONNECT_CLIENT,
	OK,
	ERROR,
	CLIENT_LIST,
	CLIENT_IP,
	MSG_DISCUSS_CLIENT,
	MSG_DISCUSS_CLIENT_SEVERAL,
	MISSING_MSG
}
