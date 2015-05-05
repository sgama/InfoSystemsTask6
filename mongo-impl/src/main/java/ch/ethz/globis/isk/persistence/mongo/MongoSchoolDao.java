package ch.ethz.globis.isk.persistence.mongo;

import ch.ethz.globis.isk.domain.School;
import ch.ethz.globis.isk.domain.mongo.MongoSchool;
import ch.ethz.globis.isk.persistence.SchoolDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MongoSchoolDao extends MongoDao<String, School> implements SchoolDao {

    @Override
    public School findOneByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.EQUAL, name));
        return findOneByFilter(filterMap);
    }

    @Override
    public School createEntity() {
        return new MongoSchool();
    }

    @Override
    protected Class<MongoSchool> getStoredClass() {
        return MongoSchool.class;
    }

    @Override
    protected String collection() {
        return "school";
    }
}
