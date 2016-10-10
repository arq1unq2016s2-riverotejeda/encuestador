package unq.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unq.api.model.*;
import unq.api.model.catalogs.SubjectOptions;
import unq.api.service.SurveyService;
import unq.repository.MongoDBDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mrivero on 28/9/16.
 */
public class SurveyServiceImpl implements SurveyService {

    private static MongoDBDAO mongoDAO = new MongoDBDAO();
    private static Logger LOGGER = LoggerFactory.getLogger(SurveyServiceImpl.class);


    @Override
    public String saveStudent(Student student) {
        LOGGER.info("Saving student "+student.getLegajo());
        return mongoDAO.saveStudent(student);
    }

    @Override
    public Student getStudentByID(String id) {
        LOGGER.info("Getting student "+id);
        return mongoDAO.getStudent(id);
    }

    @Override
    public String saveSurvey(Survey survey) {
        LOGGER.info("Saving survey for student "+survey.getLegajo());
        return mongoDAO.saveSurvey(survey);
    }

    @Override
    public Survey getSurveyByStudent(String studentId) {
        LOGGER.info("Getting survey by student "+studentId);
        return this.mongoDAO.getSurveyByStudent(studentId);
    }

    @Override
    public String saveSubject(Subject subject) {
        LOGGER.info("Starting saving subject "+subject.getName());
        return this.mongoDAO.saveSubject(subject);
    }

    @Override
    public List<SubjectOptions> getAllSubjects() {
        LOGGER.info("Start building subjects");
        List<SubjectOptions> options = new ArrayList<>();
        List<Subject> subjects = this.mongoDAO.getSubjects();
        options.addAll(subjects.stream().map(subject ->
                new SubjectOptions(subject.getName(), buildSelectionDates(subject.getDates()))).collect(Collectors.toList()));
        LOGGER.info("All subject finished successfully");
        return options;
    }

    private List<String> buildSelectionDates(Map<Comision, List<String>> dates) {
        return dates.entrySet().stream().map(subjectElection ->
                subjectElection.getKey()+": " + String.join(", ", subjectElection.getValue())).collect(Collectors.toList());
    }
}
