package unq.api.service;

import java.util.List;

import unq.api.model.ClassOccupation;
import unq.api.model.Student;
import unq.api.model.Subject;
import unq.api.model.Survey;
import unq.api.model.SurveyModel;
import unq.api.model.SurveyStudentData;
import unq.api.model.catalogs.SubjectOptions;

/**
 * Created by mrivero on 21/9/16.
 */
public interface SurveyService {

	String saveStudent(Student student);

	Student getStudentByID(String name);

	String saveSurvey(Survey survey);

	Survey getSurveyByStudent(String studentName);

	String saveSubject(Subject subject);

	List<SubjectOptions> getAllSubjects();

	List<ClassOccupation> getClassOccupation();

	double getPercentageCompletedSurveys();

	SurveyStudentData getSurveyStudentData();

	SurveyModel getSurveyModel(String token);
}
