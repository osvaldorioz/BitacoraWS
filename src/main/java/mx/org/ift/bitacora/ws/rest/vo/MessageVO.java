package mx.org.ift.bitacora.ws.rest.vo;

import java.io.Serializable;

public class MessageVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 245306752368968285L;
	private String uuid;
	private String content;
	private String time;
	
	public MessageVO(String uuid, String content, String time){
		this.uuid = uuid;
		this.content = content;
		this.time = time;
	}
	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
	
	
}
