package ch.ethz.globis.isk.persistence.mongo;

import ch.ethz.globis.isk.domain.InProceedings;
import ch.ethz.globis.isk.domain.mongo.MongoInProceedings;
import ch.ethz.globis.isk.persistence.InProceedingsDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MongoInProceedingsDao extends MongoDao<String, InProceedings> implements InProceedingsDao {

    @Override
    public InProceedings findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public List<InProceedings> findByProceedingsIdOrderByYear(String proceedingsId) {
        return queryByReferenceIdOrderByYear("parentPublication.$id", proceedingsId);
    }

    @Override
    protected Class<MongoInProceedings> getStoredClass() {
        return MongoInProceedings.class;
    }

    @Override
    protected String collection() {
        return "publication";
    }

    @Override
    public InProceedings createEntity() {
        return new MongoInProceedings();
    }
}
