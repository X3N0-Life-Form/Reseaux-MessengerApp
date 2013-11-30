package common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This object is used to exchange information between Client and Server
 * or Client and Client, as well as carrying simple String messages for
 * Client to Client discussions.
 * @author etudiant
 * @see MessageType
 */
public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6074821227848743167L;
	private Map<String, String> info;
	private Map<String, Object> objects;
	private String message;
	private MessageType type;
	
	/**
	 * Constructs a Message of the specified type with an empty message String.
	 * @param type - Determines how the Message will be handled by the recipient.
	 */
	public Message(MessageType type) {
		this(type, "");
	}
	
	/**
	 * Constructs a Message of the specified type and message string.
	 * <br />Note that any additional information or objects must be added later, depending on the MessageType.
	 * @param type - Determines how the Message will be handled by the recipient.
	 * @param message
	 */
	public Message(MessageType type, String message) {
		this.message = message;
		this.type = type;
		info = new HashMap<String, String>();
		objects = new HashMap<String, Object>();
	}
	
	public MessageType getType() {
		return type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Map<String, String> getInfo() {
		return info;
	}
	
	public Map<String, Object> getObjects() {
		return objects;
	}
	
	public void addInfo(String key, String info) {
		this.info.put(key, info);
	}
	
	public void setObjects(Map<String, Object> objects)
	{
		this.objects=objects;
	}

	public String getInfo(String key) {
		return info.get(key);
	}

	public void addObject(String key, Object object) {
		objects.put(key, object);
	}
	
	public Object getObject(String key) {
		return objects.get(key);
	}

	@Override
	public String toString() {
		return "Message [type=" + type + ", message="
				+ message + "]";
	}

}
