package unq.api.model;

import com.despegar.integration.mongo.entities.IdentifiableEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mrivero on 1/10/16.
 */
public class Survey implements IdentifiableEntity {

    private String id;
    private Student student;
    private List<SelectedSubject> selectedSubjects;


    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<SelectedSubject> getSelectedSubjects() {
        return selectedSubjects;
    }

    public void setSelectedSubjects(List<SelectedSubject> selectedSubjects) {
        this.selectedSubjects = selectedSubjects;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
