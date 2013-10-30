package serveur.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
	
	public void print(OutputStream out) throws IOException {
		for (Event e : events) {
			OutputStreamWriter osw = new OutputStreamWriter(out);
			osw.write("\n" + e);
		}
	}
}
