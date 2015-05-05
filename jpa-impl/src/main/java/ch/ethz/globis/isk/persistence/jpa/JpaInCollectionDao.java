package ch.ethz.globis.isk.persistence.jpa;

import ch.ethz.globis.isk.domain.InCollection;
import ch.ethz.globis.isk.domain.jpa.JpaInCollection;
import ch.ethz.globis.isk.persistence.InCollectionDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JpaInCollectionDao extends JpaDao<String, InCollection> implements InCollectionDao {

    @Override
    public InCollection findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public List<InCollection> findByBookIdOrderByYear(String bookId) {
        return queryByReferenceIdOrderByYear("InCollection", "parentPublication", bookId);
    }

    @Override
    protected Class<JpaInCollection> getStoredClass() {
        return JpaInCollection.class;
    }

    @Override
    public InCollection createEntity() {
        return new JpaInCollection();
    }
}
