package ch.ethz.globis.isk.persistence.mongo;

import ch.ethz.globis.isk.domain.Proceedings;
import ch.ethz.globis.isk.domain.mongo.MongoProceedings;
import ch.ethz.globis.isk.persistence.ProceedingsDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MongoProceedingsDao extends MongoDao<String, Proceedings> implements ProceedingsDao {

    @Override
    protected Class<MongoProceedings> getStoredClass() {
        return MongoProceedings.class;
    }

    @Override
    protected String collection() {
        return "publication";
    }

    @Override
    public Proceedings findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public Proceedings createEntity() {
        return new MongoProceedings();
    }
}
