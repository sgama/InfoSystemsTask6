package ch.ethz.globis.isk.persistence.mongo;

import ch.ethz.globis.isk.domain.ConferenceEdition;
import ch.ethz.globis.isk.domain.mongo.MongoConferenceEdition;
import ch.ethz.globis.isk.persistence.ConferenceEditionDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import ch.ethz.globis.isk.util.Order;
import ch.ethz.globis.isk.util.OrderFilter;
import org.springframework.stereotype.Repository;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class MongoConferenceEditionDao extends MongoDao<String, ConferenceEdition> implements ConferenceEditionDao {

    @Override
    protected Class<MongoConferenceEdition> getStoredClass() {
        return MongoConferenceEdition.class;
    }

    @Override
    protected String collection() {
        return "conferenceEdition";
    }

    @Override
    public ConferenceEdition createEntity() {
        return new MongoConferenceEdition();
    }

    @Override
    public List<ConferenceEdition> findByConferenceOrderedByYear(String conferenceId) {
        Map<String, Filter> filterMap = new TreeMap<>();
        filterMap.put("conference.$id", new Filter(Operator.EQUAL, conferenceId));
        List<OrderFilter> orderFilters = Arrays.asList(new OrderFilter("year", Order.ASC));
        return (List<ConferenceEdition>) findAllByFilter(filterMap, orderFilters);
    }
}
