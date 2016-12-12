package unq.repository;

import com.despegar.integration.mongo.connector.MongoCollection;
import com.despegar.integration.mongo.connector.MongoCollectionFactory;
import com.despegar.integration.mongo.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unq.api.model.Student;
import unq.api.model.Survey;
import unq.api.model.Subject;
import unq.client.MongoDBClient;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by mrivero on 22/9/16.
 */
public class MongoDBDAO {

    private static Logger LOGGER = LoggerFactory.getLogger(MongoDBDAO.class);
    private static MongoCollectionFactory mongoCollectionFactory = MongoDBClient.init();


    public Student getStudent(String id){
        try {
            MongoCollection<Student> students = mongoCollectionFactory.buildMongoCollection("student", Student.class);

            Query query = new Query();
            query.equals("legajo", id);
            return students.findOne(query);

        } catch (UnknownHostException e) {
            throw new RuntimeException("Error executing Mongo query", e);
        }
    }

    //TODO: Check if this works and does not duplicate rows
    public String saveStudent(Student student){
        try {
            MongoCollection<Student> students = mongoCollectionFactory.buildMongoCollection("student", Student.class);
            return students.save(student);

        } catch (UnknownHostException e) {
            throw new RuntimeException("Error executing Mongo query", e);
        }
    }

    public String saveSurvey(Survey survey){
        try {
            LOGGER.info("Starting saving survey object");
            MongoCollection<Survey> surveys = mongoCollectionFactory.buildMongoCollection("survey", Survey.class);

            //Delete existing survey
            LOGGER.info("Deleting existing survey");
            Query query = new Query();
            query.equals("legajo", survey.getLegajo());

            boolean remove = surveys.remove(query);
            LOGGER.info(String.format("Deleting survey %s", remove));

            String saved = surveys.save(survey);
            LOGGER.info("Finish saving survey object");

            return saved;

        } catch (UnknownHostException e) {
            throw new RuntimeException("Error executing Mongo query", e);
        }

    }

    public Survey getSurveyByStudent(String id) {
        try {
            MongoCollection<Survey> surveys = mongoCollectionFactory.buildMongoCollection("survey", Survey.class);

            Query query = new Query();
            query.equals("legajo", id);

            return surveys.findOne(query);

        } catch (UnknownHostException e) {
            throw new RuntimeException("Error executing Mongo query", e);
        }
    }

    public String saveSubject(Subject subject){
        try {
            LOGGER.info("Starting saving subject object");
            MongoCollection<Subject> subjects = mongoCollectionFactory.buildMongoCollection("subject", Subject.class);

            String saved = subjects.save(subject);
            LOGGER.info("Finish saving subject object");

            return saved;

        } catch (UnknownHostException e) {
            throw new RuntimeException("Error executing Mongo query", e);
        }

    }

    public List<Subject> getSubjects(){
        try {
            LOGGER.info("Getting subjects from database");
            MongoCollection<Subject> subjects = mongoCollectionFactory.buildMongoCollection("subject", Subject.class);

            List<Subject> savedSubjects = subjects.find();

            return savedSubjects;

        } catch (UnknownHostException e) {
            throw new RuntimeException("Error executing Mongo query", e);
        }
    }

    public List<Student> getStudents(){
        try {
            LOGGER.info("Getting students from database");
            MongoCollection<Student> students = mongoCollectionFactory.buildMongoCollection("student", Student.class);

            List<Student> savedStudents = students.find();

            return savedStudents;

        } catch (UnknownHostException e) {
            throw new RuntimeException("Error executing Mongo query", e);
        }
    }

    public List<Survey> getSurveys(){
        try {
            MongoCollection<Survey> surveys = mongoCollectionFactory.buildMongoCollection("survey", Survey.class);
            return surveys.find();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Error executing Mongo query", e);
        }
    }

    public Student getStudentByToken(String token) {
        try {
            MongoCollection<Student> students = mongoCollectionFactory.buildMongoCollection("student", Student.class);

            Query query = new Query();
            query.equals("authToken", token);
            return students.findOne(query);

        } catch (UnknownHostException e) {
            throw new RuntimeException("Error executing Mongo query", e);
        }
    }
}
