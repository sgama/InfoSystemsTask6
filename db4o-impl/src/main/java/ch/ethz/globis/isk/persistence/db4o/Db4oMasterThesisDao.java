package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.MasterThesis;
import ch.ethz.globis.isk.domain.db4o.Db4oMasterThesis;
import ch.ethz.globis.isk.persistence.MasterThesisDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class Db4oMasterThesisDao extends Db4oDao<String, MasterThesis> implements MasterThesisDao {

    @Override
    public Class getStoredClass() {
        return Db4oMasterThesis.class;
    }

    @Override
    public MasterThesis findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public MasterThesis createEntity() {
        return new Db4oMasterThesis();
    }
}
