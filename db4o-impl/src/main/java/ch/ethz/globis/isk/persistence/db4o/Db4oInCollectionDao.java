package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.InCollection;
import ch.ethz.globis.isk.domain.db4o.Db4oInCollection;
import ch.ethz.globis.isk.persistence.InCollectionDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class Db4oInCollectionDao extends Db4oDao<String, InCollection> implements InCollectionDao {

    @Override
    public Class getStoredClass() {
        return Db4oInCollection.class;
    }

    @Override
    public InCollection findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public List<InCollection> findByBookIdOrderByYear(String bookId) {
        return queryByReferenceIdOrderByYear("parentPublication", bookId).execute();
    }

    @Override
    public InCollection createEntity() {
        return new Db4oInCollection();
    }
}
