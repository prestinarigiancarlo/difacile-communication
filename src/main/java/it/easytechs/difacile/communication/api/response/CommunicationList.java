package it.easytechs.difacile.communication.api.response;

import java.util.ArrayList;
import java.util.List;

import it.easytechs.difacile.communication.db.entities.Communication;

public class CommunicationList {
	
	private List<Communication> list = new ArrayList<Communication>();
	
	public CommunicationList() {
		
	}

	public List<Communication> getList() {
		return list;
	}

	public void setList(List<Communication> list) {
		this.list = list;
	}
	
	
	
}
