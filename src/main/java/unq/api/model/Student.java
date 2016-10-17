package unq.api.model;

import com.despegar.integration.mongo.entities.IdentifiableEntity;

import java.io.Serializable;

/**
 * Created by marina.rivero on 19/09/2016.
 */
public class Student implements IdentifiableEntity, Serializable{

    private String id;
    private String name;
    private String legajo;

    public Student(String name, String legajo) {
        this.name = name;
        this.legajo = legajo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public String getLegajo() {
        return legajo;
    }

    public String getName() {
        return name;
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
