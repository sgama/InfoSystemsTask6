package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.School;
import ch.ethz.globis.isk.domain.db4o.Db4oSchool;
import ch.ethz.globis.isk.persistence.SchoolDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class Db4oSchoolDao extends Db4oDao<String, School> implements SchoolDao {

    @Override
    public School findOneByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.EQUAL, name));
        return findOneByFilter(filterMap);
    }

    @Override
    public School createEntity() {
        return new Db4oSchool();
    }

    @Override
    public Class getStoredClass() {
        return Db4oSchool.class;
    }
}
