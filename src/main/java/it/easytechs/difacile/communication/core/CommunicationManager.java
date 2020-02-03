package it.easytechs.difacile.communication.core;

import java.util.List;

import it.easytechs.difacile.common.auth.model.User;
import it.easytechs.difacile.communication.api.common.CreateCommunication;
import it.easytechs.difacile.communication.core.exception.CommunicationException;
import it.easytechs.difacile.communication.db.entities.Communication;

public interface CommunicationManager {

	void createCommunication(CreateCommunication communication) throws CommunicationException;
	
	//void sendEmail(Communication communication);

	List<Communication> getCommunications(Long userId);
	
	
}
