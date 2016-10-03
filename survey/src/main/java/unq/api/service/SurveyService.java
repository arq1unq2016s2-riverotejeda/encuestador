package unq.api.service;


import unq.api.model.Student;
import unq.api.model.Survey;
import unq.api.model.catalogs.Subject;

import java.util.List;

/**
 * Created by mrivero on 21/9/16.
 */
public interface SurveyService {

    String saveStudent(Student student);
    Student getStudentByID(String name);
    String  saveSurvey(Survey survey);
    Survey  getSurveyByStudent(String studentName);
    String saveSubject(Subject subject);
    List<Subject> getAllSubjects();


}
