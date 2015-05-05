package ch.ethz.globis.isk.persistence.mongo;

import ch.ethz.globis.isk.domain.PhdThesis;
import ch.ethz.globis.isk.domain.mongo.MongoPhdThesis;
import ch.ethz.globis.isk.persistence.PhdThesisDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MongoPhdThesisDao extends MongoDao<String, PhdThesis> implements PhdThesisDao {

    @Override
    protected Class<MongoPhdThesis> getStoredClass() {
        return MongoPhdThesis.class;
    }

    @Override
    protected String collection() {
        return "publication";
    }

    @Override
    public PhdThesis findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public PhdThesis createEntity() {
        return new MongoPhdThesis();
    }
}
