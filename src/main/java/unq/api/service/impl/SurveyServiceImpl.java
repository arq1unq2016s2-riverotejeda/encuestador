package unq.api.service.impl;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.utils.Assert;
import unq.api.exceptions.InvalidTokenException;
import unq.api.model.ClassOccupation;
import unq.api.model.Division;
import unq.api.model.SelectedSubject;
import unq.api.model.Student;
import unq.api.model.Subject;
import unq.api.model.SubjectStatus;
import unq.api.model.Survey;
import unq.api.model.SurveyModel;
import unq.api.model.SurveyStudentData;
import unq.api.model.catalogs.SubjectOptions;
import unq.api.service.RepositoryService;
import unq.api.service.SurveyService;
import unq.utils.EnvConfiguration;
import unq.utils.SecurityTokenGenerator;
import unq.utils.SendEmailTLS;

/**
 * Created by mrivero on 28/9/16.
 */
public class SurveyServiceImpl implements SurveyService {

    private static RepositoryService repositoryService = new RepositoryServiceImpl();
    private static Logger LOGGER = LoggerFactory.getLogger(SurveyServiceImpl.class);


    @Override
    public String saveStudent(Student student) {
        LOGGER.info(String.format("Saving student %s", student.getLegajo()));
        String token = SecurityTokenGenerator.getToken();
        student.setAuthToken(token);
        this.validateStudent(student);
        String saved = repositoryService.saveStudent(student);
        String url = getUrlNotification(token);
        SendEmailTLS.sendEmailSurveyNotification(student.getName(), student.getEmail(),url);
        LOGGER.info("Finish saving student and sending survey notification");
        return saved;
    }

    @Override
    public Student getStudentByID(String id) {
        LOGGER.info(String.format("Getting student %s", id));
        return repositoryService.getStudent(id);
    }

    @Override
    public String saveSurvey(Survey survey) {
        LOGGER.info(String.format("Saving survey for student %s", survey.getLegajo()));
        this.validateSurvey(survey);
        return repositoryService.saveSurvey(survey);
    }

    @Override
    public Survey getSurveyByStudent(String studentId, String year) {
        LOGGER.info(String.format("Getting survey by student %s and year %s", studentId, year));
        return repositoryService.getSurveyByStudent(studentId, year);
    }

    @Override
    public String saveSubject(Subject subject) {
        LOGGER.info(String.format("Starting saving subject %s", subject.getName()));
        return repositoryService.saveSubject(subject);
    }

	@Override
	public List<SubjectOptions> getAllSubjects(String year) {
        LOGGER.info(String.format("Start building subjects for year %s", year));
		List<SubjectOptions> options = new ArrayList<>();
		List<Subject> subjects = repositoryService.getSubjects(year);
		options.addAll(
				subjects.stream()
						.map(subject -> new SubjectOptions(subject.getName(),
								buildSelectionDates(subject.getDivisions()), subject.getGroup()))
						.collect(Collectors.toList()));
		LOGGER.info("All subject finished successfully");
		return options;
	}

    @Override
    public List<ClassOccupation> getClassOccupation(String year) {
        LOGGER.info(String.format("Getting class occupation for year %s", year));

        List<ClassOccupation> classOccupations = null;
        try {
            classOccupations = this.calculateClassOccupation.get(year);
        } catch (Exception e) {
            LOGGER.error("Error trying to calculate class occupation");
            throw new RuntimeException(e);
        }

        LOGGER.info("Finish calculating class occupation");
        return classOccupations;
    }

    @Override
    public SurveyStudentData getSurveyStudentData(String year) {
        LOGGER.info(String.format("Getting survey data for year %s", year));
        Integer cantStudents = repositoryService.cantStudents();
        Integer cantSurveys = repositoryService.cantSurveys(year);
        SurveyStudentData surveyStudentData = new SurveyStudentData(cantStudents, cantSurveys, this.getPercentageCompletedSurveys(cantStudents, cantSurveys));
        LOGGER.info("Finishing survey data");
        return surveyStudentData;
    }

    @Override
    public SurveyModel getSurveyModel(String token, String year) {
        LOGGER.info(String.format("Getting survey model for token %s and year %s", token, year));
        Student studentByToken = repositoryService.getStudentByToken(token);
        Assert.notNull(studentByToken, "Student must not be null for token");
        Survey completedSurvey = repositoryService.getSurveyByStudent(studentByToken.getLegajo(), year);
        return new SurveyModel(studentByToken.getName(), studentByToken.getLegajo(), this.getAllSubjects(year),
                completedSurvey);
    }

    @Override
    public Set<String> getAllYears(){
        LOGGER.info("Getting all years");
        List<Subject> allSubjects = repositoryService.getAllSubjects();
        Set<String> years = new HashSet<String>();


        allSubjects.stream().forEach(subject ->
                years.add(subject.getSchoolYear()));

        LOGGER.info("Finish getting all active year");
        return years;
    }

    private void validateStudent(Student student) {
        LOGGER.info(String.format("Validating if %s already exist", student.getLegajo()));

        Student savedStudent = repositoryService.getStudent(student.getLegajo());
        Assert.isTrue(savedStudent==null, String.format("Student %s already exist", student.getLegajo()));
    }

    private String getUrlNotification(String token) {
        String domain = EnvConfiguration.configuration.getString("frontendDomain");
        return domain+token;
    }


    private List<ClassOccupation> buildClassOccupations(String year) {
        List<ClassOccupation> classOccupations = new ArrayList<>();
        List<Survey> surveys = repositoryService.getSurveys(year);
        List<Subject> subjects = repositoryService.getSubjects(year);
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
        return classOccupations;
    }

    private LoadingCache<String, List<ClassOccupation>> calculateClassOccupation = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(15L, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, List<ClassOccupation>>() {
                        public List<ClassOccupation> load(String year) throws RuntimeException {
                            LOGGER.info("Missing key, calculating class occupation");
                            return buildClassOccupations(year);
                        }
                    });


	private double getPercentageCompletedSurveys(Integer cantStudents, Integer cantSurvey){
		LOGGER.info("Getting percentage completed survey");

		int percentage = (cantSurvey * 100) / cantStudents;

		LOGGER.info("Finishing percentage completed survey");

		return percentage;
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
        Student studentByToken = repositoryService.getStudentByToken(survey.getToken());
        if (null != studentByToken) {
            LOGGER.info("Token student validation ok");
            return;
        }
        throw new InvalidTokenException("Invalid token, user now exist for that token "+survey.getToken());
    }

    @Override
    public String getLastActiveYear(){
        LOGGER.info("Starting calculating active year");
        Subject oldest = null;
        try {
            oldest = this.calculateLastActiveYear.get("lastActiveYear");
        } catch (ExecutionException e) {
            LOGGER.error("Error trying to get las active year", e);
            throw new RuntimeException(e);
        }

        LOGGER.info(String.format("Finish calculating last active year %s", oldest.getSchoolYear()));
        return oldest.getSchoolYear();

    }

    private LoadingCache<String, Subject> calculateLastActiveYear = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(30L, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, Subject>() {
                        public Subject load(String year) throws RuntimeException {
                            LOGGER.info("Missing key, calculating last active year");
                            return calculateLastActiveYear();
                        }
                    });


    private Subject calculateLastActiveYear() {
        List<Subject> allSubjects = repositoryService.getAllSubjects();


        final Comparator<Subject> comp = (p1, p2) -> Integer.compare( Integer.valueOf(p1.getSchoolYear()), Integer.valueOf(p2.getSchoolYear()));
        return allSubjects.stream()
                .max(comp)
                .get();
    }
}
