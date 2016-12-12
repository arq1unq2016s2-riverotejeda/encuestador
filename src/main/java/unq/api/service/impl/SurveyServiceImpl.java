package unq.api.service.impl;

import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.utils.Assert;
import unq.api.exceptions.InvalidTokenException;
import unq.api.model.*;
import unq.api.model.catalogs.SubjectOptions;
import unq.api.service.SurveyService;
import unq.repository.MongoDBDAO;
import unq.utils.EnvConfiguration;
import unq.utils.SecurityTokenGenerator;
import unq.utils.SendEmailTLS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mrivero on 28/9/16.
 */
public class SurveyServiceImpl implements SurveyService {

    private static MongoDBDAO mongoDAO = new MongoDBDAO();
    private static Logger LOGGER = LoggerFactory.getLogger(SurveyServiceImpl.class);


    @Override
    public String saveStudent(Student student) {
        LOGGER.info(String.format("Saving student %s", student.getLegajo()));
        String token = SecurityTokenGenerator.getToken();
        student.setAuthToken(token);
        this.validateStudent(student);
        String saved = mongoDAO.saveStudent(student);
        String url = getUrlNotification(token);
        SendEmailTLS.sendEmailSurveyNotification(student.getName(), student.getEmail(),url);
        LOGGER.info("Finish saving student and sending survey notification");
        return saved;
    }

    private void validateStudent(Student student) {
        LOGGER.info(String.format("Validating if %s already exist", student.getId()));

        Student savedStudent = mongoDAO.getStudent(student.getId());
        Assert.isTrue(savedStudent==null, String.format("Student %s already exist", student.getId()));
    }

    private String getUrlNotification(String token) {
        String domain = EnvConfiguration.configuration.getString("frontendDomain");
        return domain+token;
    }

    @Override
    public Student getStudentByID(String id) {
        LOGGER.info(String.format("Getting student %s", id));
        return mongoDAO.getStudent(id);
    }

    @Override
    public String saveSurvey(Survey survey) {
        LOGGER.info(String.format("Saving survey for student %s", survey.getLegajo()));
        this.validateSurvey(survey);
        return mongoDAO.saveSurvey(survey);
    }

    @Override
    public Survey getSurveyByStudent(String studentId) {
        LOGGER.info(String.format("Getting survey by student %s", studentId));
        return mongoDAO.getSurveyByStudent(studentId);
    }

    @Override
    public String saveSubject(Subject subject) {
        LOGGER.info(String.format("Starting saving subject %s", subject.getName()));
        return mongoDAO.saveSubject(subject);
    }

    @Override
    public List<SubjectOptions> getAllSubjects() {
        LOGGER.info("Start building subjects");
        List<SubjectOptions> options = new ArrayList<>();
        List<Subject> subjects = mongoDAO.getSubjects();
        options.addAll(subjects.stream().map(subject ->
                new SubjectOptions(subject.getName(), buildSelectionDates(subject.getDivisions()))).collect(Collectors.toList()));
        LOGGER.info("All subject finished successfully");
        return options;
    }

    @Override
    public List<ClassOccupation> getClassOccupation() {

        LOGGER.info("Getting class occupation");
        List<ClassOccupation> classOccupations = new ArrayList<>();
        List<Survey> surveys = mongoDAO.getSurveys();
        List<Subject> subjects = mongoDAO.getSubjects();
        List<SelectedSubject> totalSelectedSubjects = new ArrayList<>();

        for(Survey survey: surveys){
            // get chosen subjects
            Stream<SelectedSubject> selectedSubjects = survey.getSelectedSubjects().stream().filter(
                    this::isSelected);
            totalSelectedSubjects.addAll(selectedSubjects.collect(Collectors.toList()));
        }

        Map<String, List<SelectedSubject>> seleccionBySubject = totalSelectedSubjects.stream().collect(
                Collectors.groupingBy(SelectedSubject::getSubject));


        seleccionBySubject.forEach((subjectName, value) -> {
            Map<String, Long> countComision = value.stream().collect(
                    Collectors.groupingBy(SelectedSubject::getStatus, Collectors.counting()));

            countComision.forEach((comision, count) -> {
                Stream<Subject> subjectStream = subjects.stream().filter(subject -> subject.getName().equals(subjectName));
                Stream<Division> divisions = subjectStream.collect(Collectors.toList()).get(0).getDivisions().stream().filter(
                        division -> division.getComision().name().equals(comision.split(":")[0]));
                divisions.forEach(division -> {
                    long percentage = count * 100L / division.getQuota();

                    classOccupations.add(new ClassOccupation(subjectName, comision,count, percentage));
                });
            });

        });

        LOGGER.info("Finish calculating class occupation");
        return classOccupations;
    }

    @Override
    public double getPercentageCompletedSurveys(){
        LOGGER.info("Getting percentage completed survey");

        List <Student> students = mongoDAO.getStudents();
        List<Survey> surveys = mongoDAO.getSurveys();

        int totalStudents = students.size();
        int counter = 0;
        for(Student student: students){
            if (this.completedSurvey(student.getLegajo(), surveys)) counter++;
        }
        int percentage = (counter * 100) / totalStudents;

        LOGGER.info("Finishing percentage completed survey");

        return percentage;
    }


    @Override
    public SurveyStudentData getSurveyStudentData() {
        LOGGER.info("Getting survey data");
        List<Survey> surveys = mongoDAO.getSurveys();
        List<Student> students = mongoDAO.getStudents();
        SurveyStudentData surveyStudentData = new SurveyStudentData(students.size(), surveys.size());
        LOGGER.info("Finishing survey data");
        return surveyStudentData;
    }

    @Override
    public SurveyModel getSurveyModel(String token) {
        LOGGER.info(String.format("Getting survey model for token %s", token));
        Student studentByToken = mongoDAO.getStudentByToken(token);
        Assert.notNull(studentByToken, "Student must not be null for token");
        Survey completedSurvey = mongoDAO.getSurveyByStudent(studentByToken.getLegajo());
        return new SurveyModel(studentByToken.getName(), studentByToken.getLegajo(), this.getAllSubjects(),
                completedSurvey);
    }

    private boolean isSelected(SelectedSubject selectedSubject) {
        return !selectedSubject.getStatus().toLowerCase().equals(SubjectStatus.APPROVED.name().toLowerCase()) &&
                !selectedSubject.getStatus().toLowerCase().equals(SubjectStatus.BAD_SCHEDULE.name().toLowerCase()) &&
                !selectedSubject.getStatus().toLowerCase().equals(SubjectStatus.NOT_YET.name().toLowerCase());
    }

    private List<String> buildSelectionDates(List<Division> divisions) {
        return divisions.stream().map(subjectElection ->
                subjectElection.getComision()+": " + String.join(", ", subjectElection.getWeekdays())).collect(Collectors.toList());
    }

    private boolean completedSurvey(String id, List<Survey> surveys){
        Stream<Survey> studentSurvey = surveys.stream().filter(survey -> survey.getLegajo().equals(id));
        return (studentSurvey.count()>0);
    }

    private void validateSurvey(Survey survey) {
        LOGGER.info(String.format("Starting validation for user %s", survey.getStudentName()));
        Student studentByToken = mongoDAO.getStudentByToken(survey.getToken());
        if (null != studentByToken) {
            LOGGER.info("Token student validation ok");
            return;
        }
        throw new InvalidTokenException("Invalid token, user now exist for that token "+survey.getToken());
    }
}
