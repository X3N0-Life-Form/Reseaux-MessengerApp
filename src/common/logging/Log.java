package common.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to log events according to type, date and error message (see {@link Event}).
 * It can be used by either a Client or a Server, which then passes the Log object to its Handlers,
 * MessageManagers, etc.
 * @author etudiant
 * @see Event
 */
public class Log {
	
	private List<Event> events;
	
	/**
	 * Constructs an empty Log.
	 */
	public Log() {
		events = new LinkedList<Event>();
	}
	
	/**
	 * Records an event in the log.
	 * @param type - EventType.
	 * @param comment - Short description of the event.
	 */
	public synchronized void log(EventType type, String comment) {
		Event e = new Event(type, comment);
		events.add(e);
		System.out.println(" (" + type + ")\t" + e.getTime() + "  " + comment);
	}
	
	public List<Event> getEvents() {
		return events;
	}
	
	/**
	 * Returns recorded events by type.
	 * @param type - EventType to look for.
	 * @return List containing only the events of the specified type.
	 */
	public List<Event> getEventsByType(EventType type) {
		LinkedList<Event> res = new LinkedList<Event>();
		for (Event event : events) {
			if (event.getType() == type) {
				res.add(event);
			}
		}
		return res;
	}
	
	/**
	 * Prints every event in the specified stream.
	 * @param out - OutputStream printing the events.
	 * @throws IOException
	 */
	public void print(OutputStream out) throws IOException {
		for (Event e : events) {
			OutputStreamWriter osw = new OutputStreamWriter(out);
			osw.write("\n" + e);
		}
	}
}
