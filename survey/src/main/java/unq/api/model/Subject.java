package unq.api.model;

import com.despegar.integration.mongo.entities.IdentifiableEntity;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by mrivero on 2/10/16.
 */
public class Subject implements IdentifiableEntity, Serializable{

    private String id;
    private String name;
    private Map<Comision, List<String>> dates;

    public Subject(){
        super();
    }

    public Map<Comision, List<String>> getDates() {
        return dates;
    }

    public void setDates(Map<Comision, List<String>> dates) {
        this.dates = dates;
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
