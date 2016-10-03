package unq.api.model;

import org.joda.time.DateTime;
import unq.api.model.catalogs.Subject;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by mrivero on 2/10/16.
 */
public class SelectedSubject implements Serializable{

    private String subject;
    private SubjectStatus status;
    private Optional<List<String>> date;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public SubjectStatus getStatus() {
        return status;
    }

    public void setStatus(SubjectStatus status) {
        this.status = status;
    }

    public Optional<List<String>> getDate() {
        return date;
    }

    public void setDate(Optional<List<String>> date) {
        this.date = date;
    }
}
