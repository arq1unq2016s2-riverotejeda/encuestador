package unq.client;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;

/**
 * Created by mrivero on 21/9/16.
 */
public class MongoDBClient {

    MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

    MongoDatabase database = mongoClient.getDatabase("unq");

    public void insertData(Document dataToInsert){
        MongoCollection<Document> surveyCollection = database.getCollection("suervey");
        surveyCollection.insertOne(dataToInsert);
    }

    public void updateData(Document dataToUpdate){
        //TODO
    }

    public void deleteData(Document dataToDelete){
        //TODO
    }

    public void getData(Document filter){
        MongoCollection<Document> surveyCollection = database.getCollection("suervey");
        surveyCollection.find(eq("field", filter.getInteger("value")));
    }

}
