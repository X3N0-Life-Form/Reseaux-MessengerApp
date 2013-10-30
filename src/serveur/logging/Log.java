package serveur.logging;

import java.util.LinkedList;
import java.util.List;

public class Log {
	List<Event> events;
	
	public Log() {
		events = new LinkedList<Event>();
	}
	
	public void log(EventType type, String comment) {
		Event e = new Event(type, comment);
		events.add(e);
		System.out.println(e.getTime() + "  " + comment);
	}
}
