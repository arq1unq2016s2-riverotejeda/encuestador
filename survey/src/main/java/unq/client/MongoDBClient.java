package unq.client;

import com.despegar.integration.mongo.connector.MongoCollectionFactory;
import com.despegar.integration.mongo.connector.MongoDBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * Created by mrivero on 21/9/16.
 */
public class MongoDBClient {

    public static Logger LOGGER = LoggerFactory.getLogger(MongoDBClient.class);


    public static MongoCollectionFactory init(){
        MongoDBConnection connection;
        try {
            connection = new MongoDBConnection("unq", "localhost:27017");
            LOGGER.info("Successfully connected to the database");
        } catch (UnknownHostException e) {
            LOGGER.error("Error trying to connect to MongoDB");
            throw new RuntimeException("Error trying to connect to Mongo", e);
        }

        return new MongoCollectionFactory(connection);
    }

}
