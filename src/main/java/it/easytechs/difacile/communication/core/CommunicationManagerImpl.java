package it.easytechs.difacile.communication.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;

import it.easytechs.difacile.common.auth.model.User;
import it.easytechs.difacile.common.core.CacheManager;
import it.easytechs.difacile.common.core.logging.DiFacileLogger;
import it.easytechs.difacile.common.core.logging.DiFacileLoggerFactory;
import it.easytechs.difacile.common.db.repository.Repository;
import it.easytechs.difacile.common.utils.DateUtils;
import it.easytechs.difacile.communication.api.common.CreateCommunication;
import it.easytechs.difacile.communication.core.exception.CommunicationException;
import it.easytechs.difacile.communication.db.entities.Communication;
import it.easytechs.difacile.practice.api.common.ProcedureSearchResult;
import it.easytechs.difacile.practice.core.procedure.model.ProcedureStatus;
import it.easytechs.difacile.practice.db.entities.Procedure;
import it.easytechs.difacile.user.api.registration.CompanyDocument;
import it.easytechs.difacile.user.core.registration.RegistrationManagerImpl;
import it.easytechs.difacile.user.db.entities.RegisteredUser;

public class CommunicationManagerImpl implements CommunicationManager {

	private static final DiFacileLogger logger = DiFacileLoggerFactory.getLogger(CommunicationManagerImpl.class);
	
	private Repository<Communication, String> repository;
	private CacheManager cacheManager;

	public CommunicationManagerImpl(CacheManager cacheManager, Repository<Communication, String> repository) {
		this.repository = repository;
		this.cacheManager = cacheManager;
	}

	/*private <T> T readFromCache(String registrationId, String objectId) {
		return this.cacheManager.get("RegistrationManagerImpl", String.format("%s_%s", registrationId, objectId));
	}

	private void saveToCache(String registrationId, String objectId, Object object) {
		cacheManager.put("RegistrationManagerImpl", String.format("%s_%s", registrationId, objectId), object);
	}*/
	
	@Override
	public List<Communication> getCommunications(Long userId) {
		
		logger.info("getCommunications start");

		BasicDBObject orQuery = new BasicDBObject();
		List<BasicDBObject> objOr = new ArrayList<>();		
		objOr.add(new BasicDBObject("toUserId", userId));
		objOr.add(new BasicDBObject("fromUserId", userId));
		orQuery.put("$or", objOr);
		
		
        logger.info("query " + orQuery);
		List<Communication> result = repository.query(orQuery);
		
		return result;
	}
	
	public void createCommunication(CreateCommunication communication) throws CommunicationException {
		
		if (communication != null) {
			
			Communication entity = new Communication();
			entity.setDate(communication.getDate());
			entity.setFromUserId(communication.getFromUserId());
			entity.setToUserId(communication.getToUserId());
			entity.setToUserEmail(communication.getToUserEmail());
			entity.setShortText(communication.getShortText());
			entity.setFullText(communication.getFullText());			
		
			repository.add(entity);			
			
		} else {
			throw new CommunicationException("Communication empty");
		}
		
	}
	
	
}
