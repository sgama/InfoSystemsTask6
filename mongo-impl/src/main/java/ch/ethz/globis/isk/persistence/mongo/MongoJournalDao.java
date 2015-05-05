package ch.ethz.globis.isk.persistence.mongo;

import ch.ethz.globis.isk.domain.Journal;
import ch.ethz.globis.isk.domain.mongo.MongoJournal;
import ch.ethz.globis.isk.persistence.JournalDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MongoJournalDao extends MongoDao<String, Journal> implements JournalDao {

    @Autowired
    MongoJournalEditionDao mongoJournalEditionDao;

    @Override
    public Journal findOneByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.EQUAL, name));
        return findOneByFilter(filterMap);
    }

    @Override
    public Journal createEntity() {
        return new MongoJournal();
    }

    @Override
    protected Class<MongoJournal> getStoredClass() {
        return MongoJournal.class;
    }

    @Override
    protected String collection() {
        return "journal";
    }
}
