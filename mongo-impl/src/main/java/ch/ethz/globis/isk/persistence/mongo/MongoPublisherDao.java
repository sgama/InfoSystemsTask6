package ch.ethz.globis.isk.persistence.mongo;

import ch.ethz.globis.isk.domain.Publisher;
import ch.ethz.globis.isk.domain.mongo.MongoPublisher;
import ch.ethz.globis.isk.persistence.PublisherDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MongoPublisherDao extends MongoDao<String, Publisher> implements PublisherDao {

    @Override
    public Publisher findOneByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.EQUAL, name));
        return findOneByFilter(filterMap);
    }

    @Override
    public Publisher createEntity() {
        return new MongoPublisher();
    }

    @Override
    protected Class<MongoPublisher> getStoredClass() {
        return MongoPublisher.class;
    }

    @Override
    protected String collection() {
        return "publisher";
    }
}
