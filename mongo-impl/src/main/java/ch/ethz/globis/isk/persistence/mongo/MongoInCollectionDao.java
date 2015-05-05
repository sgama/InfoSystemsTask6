package ch.ethz.globis.isk.persistence.mongo;

import ch.ethz.globis.isk.domain.InCollection;
import ch.ethz.globis.isk.domain.mongo.MongoInCollection;
import ch.ethz.globis.isk.persistence.InCollectionDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MongoInCollectionDao extends MongoDao<String, InCollection> implements InCollectionDao {

    @Override
    protected Class<MongoInCollection> getStoredClass() {
        return MongoInCollection.class;
    }

    @Override
    protected String collection() {
        return "publication";
    }

    @Override
    public InCollection createEntity() {
        return new MongoInCollection();
    }

    @Override
    public InCollection findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public List<InCollection> findByBookIdOrderByYear(String bookId) {
        return queryByReferenceIdOrderByYear("parentPublication.$id", bookId);
    }
}
