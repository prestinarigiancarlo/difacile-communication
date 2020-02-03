package it.easytechs.difacile.communication.db.repository.mongodb;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import it.easytechs.difacile.common.db.repository.mongodb.MongoDbConnection;
import it.easytechs.difacile.common.db.repository.mongodb.MongoDbRepository;
import it.easytechs.difacile.communication.db.entities.Communication;
import it.easytechs.difacile.communication.db.repository.mongodb.mapper.CommunicationMapper;
import it.easytechs.difacile.user.db.entities.DiFacileUser;
import it.easytechs.difacile.user.db.repository.mongodb.mapper.UserMapper;

import java.util.List;

public class CommunicationRepository extends MongoDbRepository<Communication> {

    private static final CommunicationMapper mapper = new CommunicationMapper();

    public CommunicationRepository(MongoDbConnection mongoDbConnection, String collection) {
        super(mongoDbConnection, collection);
    }

    @Override
    protected DBObject toDBObject(Communication entity) {
        return mapper.toDBObject(entity);
    }

    @Override
    protected Communication toEntity(DBObject dbObject) {
        return mapper.toEntityCommunication(dbObject);
    }

    @Override
    public Communication get(String id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        List<Communication> list = this.query(query);
        if(list!=null && list.size()>0)
            return list.get(0);
        return null;
    }

    @Override
    public void update(Communication entity) {
        DBCollection collection = super.getCollection();

        collection.update(new BasicDBObject("_id",entity.getId()),this.toDBObject(entity));
    }





}