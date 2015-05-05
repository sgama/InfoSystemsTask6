package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.InProceedings;
import ch.ethz.globis.isk.domain.db4o.Db4oInProceedings;
import ch.ethz.globis.isk.persistence.InProceedingsDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class Db4oInProceedingsDao extends Db4oDao<String, InProceedings> implements InProceedingsDao {

    @Override
    public Class getStoredClass() {
        return Db4oInProceedings.class;
    }

    @Override
    public InProceedings findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public List<InProceedings> findByProceedingsIdOrderByYear(String proceedingsId) {
        return queryByReferenceIdOrderByYear("proceedings", proceedingsId).execute();
    }

    @Override
    public InProceedings createEntity() {
        return new Db4oInProceedings();
    }
}
