package unq.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unq.api.model.Student;
import unq.client.MongoDBClient;
import static com.mongodb.client.model.Filters.*;

/**
 * Created by mrivero on 16/10/16.
 */
public class StudentDAO {

    /*
    private static Logger LOGGER = LoggerFactory.getLogger(StudentDAO.class);
    private static MongoDatabase mongoCollectionFactory = MongoDBClient.init();

    public Student getStudent(String id){
        LOGGER.info("Getting student with legajo "+id);
        MongoCollection<Document> students = mongoCollectionFactory.getCollection("student");

        Document student = students.find(eq("legajo", id)).first();
        return new Student(student.getString("name"), student.getString("legajo"));
    }

    public Student saveStudent(Student student){

    }*/
}
