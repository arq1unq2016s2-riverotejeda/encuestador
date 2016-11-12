package unq.api.model.catalogs;

import unq.api.model.SubjectStatus;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mrivero on 9/10/16.
 */
public class SubjectOptions implements Serializable{

    String subjectName;
    //Comision: horario
    List<String> date;
    List<SubjectStatus> options = Arrays.stream(SubjectStatus.values()).collect(Collectors.toList());
    List<String> generalOptions;

    public SubjectOptions(String subjectName, List<String> date) {
        this.subjectName = subjectName;
        this.date = date;
        this.generalOptions = date; //options.stream().map(elem -> elem.toString()).collect(Collectors.toList());
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<String> getGeneralOptions() {
        return generalOptions;
    }

    public void setGeneralOptions(List<String> generalOptions) {
        this.generalOptions = generalOptions;
    }
}
