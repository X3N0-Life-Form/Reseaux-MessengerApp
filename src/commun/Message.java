package commun;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6074821227848743167L;
	private Map<String, String> info;
	private Map<String, Object> objects;
	private String message;
	private MessageType type;
	
	public Message(MessageType type) {
		this(type, "");
	}
	
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
