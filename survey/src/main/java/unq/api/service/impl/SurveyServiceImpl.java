package unq.api.service.impl;

import unq.api.model.Student;
import unq.api.model.Survey;
import unq.api.model.catalogs.Subject;
import unq.api.service.SurveyService;
import unq.repository.MongoDBDAO;

import java.util.List;

/**
 * Created by mrivero on 28/9/16.
 */
public class SurveyServiceImpl implements SurveyService {

    private static MongoDBDAO mongoDAO = new MongoDBDAO();


    @Override
    public String saveStudent(Student student) {
        return mongoDAO.saveStudent(student);
    }

    @Override
    public Student getStudentByID(String id) {
        return mongoDAO.getStudent(id);
    }

    @Override
    public String saveSurvey(Survey survey) {
        return mongoDAO.saveSurvey(survey);
    }

    @Override
    public Survey getSurveyByStudent(String studentId) {
        return this.mongoDAO.getSurveyByStudent(studentId);
    }

    @Override
    public String saveSubject(Subject subject) {
        return this.mongoDAO.saveSubject(subject);
    }

    @Override
    public List<Subject> getAllSubjects() {
        return this.mongoDAO.getSubjects();
    }
}
