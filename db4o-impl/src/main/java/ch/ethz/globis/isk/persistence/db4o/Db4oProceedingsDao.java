package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.Proceedings;
import ch.ethz.globis.isk.domain.db4o.Db4oProceedings;
import ch.ethz.globis.isk.persistence.ProceedingsDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class Db4oProceedingsDao extends Db4oDao<String, Proceedings> implements ProceedingsDao {

    @Override
    public Class getStoredClass() {
        return Db4oProceedings.class;
    }

    @Override
    public Proceedings findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public Proceedings createEntity() {
        return new Db4oProceedings();
    }
}
