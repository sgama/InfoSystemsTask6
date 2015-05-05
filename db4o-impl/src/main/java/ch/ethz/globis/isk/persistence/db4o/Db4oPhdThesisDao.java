package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.PhdThesis;
import ch.ethz.globis.isk.domain.db4o.Db4oPhdThesis;
import ch.ethz.globis.isk.persistence.PhdThesisDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class Db4oPhdThesisDao extends Db4oDao<String, PhdThesis> implements PhdThesisDao {

    @Override
    public Class getStoredClass() {
        return Db4oPhdThesis.class;
    }

    @Override
    public PhdThesis findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public PhdThesis createEntity() {
        return new Db4oPhdThesis();
    }
}
