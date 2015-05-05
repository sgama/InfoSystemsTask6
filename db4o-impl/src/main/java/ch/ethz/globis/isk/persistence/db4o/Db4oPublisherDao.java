package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.Publisher;
import ch.ethz.globis.isk.domain.db4o.Db4oPublisher;
import ch.ethz.globis.isk.persistence.PublisherDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class Db4oPublisherDao extends Db4oDao<String, Publisher> implements PublisherDao {

    @Override
    public Publisher findOneByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.EQUAL, name));
        return findOneByFilter(filterMap);
    }

    @Override
    public Publisher createEntity() {
        return new Db4oPublisher();
    }

    @Override
    public Class getStoredClass() {
        return Db4oPublisher.class;
    }
}
