package ch.ethz.globis.isk.domain.mongo;

import ch.ethz.globis.isk.domain.MasterThesis;
import ch.ethz.globis.isk.domain.School;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "publication")
public class MongoMasterThesis extends MongoPublication implements MasterThesis {

    @DBRef(lazy = true)
    private School school;

    public MongoMasterThesis() {
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
