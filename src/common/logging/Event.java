package common.logging;

import java.util.Date;

/**
 * Represents an event. Has a type, date and comment.
 * @author etudiant
 * @see Log
 * @see EventType
 */
public class Event {
	
	private EventType type;
	private Date time;
	private String comment;
	
	public Event(EventType type, String comment) {
		super();
		this.type = type;
		this.comment = comment;
		time = new Date();
	}

	@Override
	public String toString() {
		return "Event [time=" + time + "type=" + type + ", comment=" + comment + "]";
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
