package unq.api.model;

import com.despegar.integration.mongo.entities.IdentifiableEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by mrivero on 2/10/16.
 */
public class Subject implements IdentifiableEntity, Serializable{

    private String id;
    private String name;
    private List<Division> divisions;

    public Subject(){
        super();
    }

    public List<Division> getDivisions() {
        return divisions;
    }

    public void setDivisions(List<Division> divisions) {
        this.divisions = divisions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
