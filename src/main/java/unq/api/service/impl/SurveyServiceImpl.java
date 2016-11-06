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
import java.util.stream.Stream;

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
                new SubjectOptions(subject.getName(), buildSelectionDates(subject.getDivisions()))).collect(Collectors.toList()));
        LOGGER.info("All subject finished successfully");
        return options;
    }

    @Override
    public List<ClassOccupation> getClassOccupation() {

        LOGGER.info("Getting class occupation");
        List<ClassOccupation> classOccupations = new ArrayList<>();
        List<Survey> surveys = this.mongoDAO.getSurveys();
        List<Subject> subjects = this.mongoDAO.getSubjects();
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

    private boolean isSelected(SelectedSubject selectedSubject) {
        return !selectedSubject.getStatus().toLowerCase().equals(SubjectStatus.APPROVED.name().toLowerCase()) &&
                !selectedSubject.getStatus().toLowerCase().equals(SubjectStatus.BAD_SCHEDULE.name().toLowerCase()) &&
                !selectedSubject.getStatus().toLowerCase().equals(SubjectStatus.NOT_YET.name().toLowerCase());
    }

    private List<String> buildSelectionDates(List<Division> divisions) {
        return divisions.stream().map(subjectElection ->
                subjectElection.getComision()+": " + String.join(", ", subjectElection.getWeekdays())).collect(Collectors.toList());
    }

    class ComissionCount{
        public String comission;
        public Integer count;
    }
}
