package it.easytechs.difacile.communication.db.entities;

import java.util.Date;
import it.easytechs.difacile.common.db.entities.MyObject;


public class Communication extends MyObject {
		
	private String date;
	private Long fromUserId;
	private Long toUserId;
	private String toUserEmail;
	private String shortText;
	private String fullText;
	
	
	public Communication(){
		
	}

	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public Long getFromUserId() {
		return fromUserId;
	}


	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}


	public Long getToUserId() {
		return toUserId;
	}


	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}


	public String getToUserEmail() {
		return toUserEmail;
	}


	public void setToUserEmail(String toUserEmail) {
		this.toUserEmail = toUserEmail;
	}


	public String getShortText() {
		return shortText;
	}


	public void setShortText(String shortText) {
		this.shortText = shortText;
	}


	public String getFullText() {
		return fullText;
	}


	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

}
