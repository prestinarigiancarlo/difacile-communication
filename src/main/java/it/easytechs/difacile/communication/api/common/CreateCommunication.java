package it.easytechs.difacile.communication.api.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateCommunication {

	private String date;
	private Long fromUserId;
	private Long toUserId;
	private String toUserEmail;
	private String shortText;
	private String fullText;
	
	
	public CreateCommunication(){
		
	}
	
	@JsonProperty
	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}

	@JsonProperty
	public Long getFromUserId() {
		return fromUserId;
	}


	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}

	@JsonProperty
	public Long getToUserId() {
		return toUserId;
	}

	@JsonProperty
	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	@JsonProperty
	public String getToUserEmail() {
		return toUserEmail;
	}


	public void setToUserEmail(String toUserEmail) {
		this.toUserEmail = toUserEmail;
	}

	@JsonProperty
	public String getShortText() {
		return shortText;
	}


	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	@JsonProperty
	public String getFullText() {
		return fullText;
	}


	public void setFullText(String fullText) {
		this.fullText = fullText;
	}
	
}
