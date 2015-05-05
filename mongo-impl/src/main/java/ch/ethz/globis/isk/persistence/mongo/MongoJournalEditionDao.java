package ch.ethz.globis.isk.persistence.mongo;

import ch.ethz.globis.isk.domain.JournalEdition;
import ch.ethz.globis.isk.domain.mongo.MongoJournalEdition;
import ch.ethz.globis.isk.persistence.JournalEditionDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import ch.ethz.globis.isk.util.Order;
import ch.ethz.globis.isk.util.OrderFilter;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class MongoJournalEditionDao extends MongoDao<String, JournalEdition> implements JournalEditionDao {

    @Override
    protected Class<MongoJournalEdition> getStoredClass() {
        return MongoJournalEdition.class;
    }

    @Override
    protected String collection() {
        return "journalEdition";
    }

    @Override
    public List<JournalEdition> findByJournalIdOrdered(String journalId) {
        Map<String, Filter> filterMap = new TreeMap<>();
        filterMap.put("journal.$id", new Filter(Operator.EQUAL, journalId));
        List<OrderFilter> orderFilters = Arrays.asList(new OrderFilter("year", Order.ASC), new OrderFilter("volume", Order.ASC), new OrderFilter("number", Order.ASC));
        return (List<JournalEdition>) findAllByFilter(filterMap, orderFilters);
    }

    @Override
    public JournalEdition createEntity() {
        return new MongoJournalEdition();
    }
}
