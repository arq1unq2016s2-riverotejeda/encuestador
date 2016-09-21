package unq.api.model;

import java.io.Serializable;

/**
 * Created by marina.rivero on 19/09/2016.
 */
public class Student implements Serializable{

    private String name;
    private String legajo;

    public Student(String legajo, String name) {
        this.legajo = legajo;
        this.name = name;
    }

    public String getLegajo() {
        return legajo;
    }

    public String getName() {
        return name;
    }

}
