package common.logging;

/**
 * Enumerates the various types of event recorded by the Log class.
 * @author etudiant
 * @see Log
 * @see Event
 */
public enum EventType {
	PARSING,
	START,
	STOP,
	INFO,
	RECEIVE_TCP,
	RECEIVE_UDP,
	SEND_TCP,
	SEND_UDP,
	TIMEOUT,
	WARNING,
	ERROR
}
