package it.easytechs.difacile.communication.db.repository.mongodb.mapper;


import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import it.easytechs.difacile.common.core.logging.DiFacileLogger;
import it.easytechs.difacile.common.core.logging.DiFacileLoggerFactory;
import it.easytechs.difacile.communication.db.entities.Communication;
import it.easytechs.difacile.user.db.entities.DiFacileUser;
import org.bson.types.ObjectId;

public class CommunicationMapper {

	private static final DiFacileLogger logger = DiFacileLoggerFactory.getLogger(CommunicationMapper.class);

    public Communication toEntityCommunication(DBObject dbObject) {
    	
        BasicDBObject basicDBObject = (BasicDBObject)dbObject;
        Communication communication = new Communication();

        communication.setId(basicDBObject.get("_id").toString());
        communication.setFromUserId(basicDBObject.getLong("fromUserId"));
        communication.setToUserId(basicDBObject.getLong("toUserId"));
        communication.setToUserEmail(basicDBObject.getString("toUserEmail"));
        communication.setShortText(basicDBObject.getString("shortText"));
        communication.setFullText(basicDBObject.getString("fullText"));
		
        return communication;
        
    }


    public DBObject toDBObject(Communication entity) {
				DBObject dbObject = new BasicDBObject();

				dbObject = new BasicDBObject();

        ((BasicDBObject) dbObject).append("fromUserId",entity.getFromUserId());
        ((BasicDBObject) dbObject).append("toUserId",entity.getToUserId());
        ((BasicDBObject) dbObject).append("toUserEmail",entity.getToUserEmail());
        ((BasicDBObject) dbObject).append("shortText",entity.getShortText());
        ((BasicDBObject) dbObject).append("fullText",entity.getFullText());
        
		//((BasicDBObject) dbObject).append("_id",new ObjectId(entity.getId()));

        return dbObject;
    }


}