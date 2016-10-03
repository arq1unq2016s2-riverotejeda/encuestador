package unq.api.model.catalogs;

import com.despegar.integration.mongo.entities.IdentifiableEntity;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mrivero on 2/10/16.
 */
public class Subject implements IdentifiableEntity, Serializable{

    private String id;
    private String name;
    private List<String> dates;

    public Subject(){
        super();
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
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
