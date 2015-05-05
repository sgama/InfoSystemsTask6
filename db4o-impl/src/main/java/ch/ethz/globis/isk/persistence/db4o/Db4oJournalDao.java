package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.Journal;
import ch.ethz.globis.isk.domain.db4o.Db4oJournal;
import ch.ethz.globis.isk.persistence.JournalDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class Db4oJournalDao extends Db4oDao<String, Journal> implements JournalDao {

    @Override
    public Journal findOneByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.EQUAL, name));
        return findOneByFilter(filterMap);
    }

    @Override
    public Journal createEntity() {
        return new Db4oJournal();
    }

    @Override
    public Class getStoredClass() {
        return Db4oJournal.class;
    }
}
