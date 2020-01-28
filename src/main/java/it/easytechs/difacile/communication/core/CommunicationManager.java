package it.easytechs.difacile.communication.core;

import java.util.List;

import it.easytechs.difacile.common.auth.model.User;
import it.easytechs.difacile.communication.db.entities.Communication;

public interface CommunicationManager {

	//Communication createCommunication(Communication communication);
	
	//void sendEmail(Communication communication);

	List<Communication> getCommunications(User user);
	
	
}
