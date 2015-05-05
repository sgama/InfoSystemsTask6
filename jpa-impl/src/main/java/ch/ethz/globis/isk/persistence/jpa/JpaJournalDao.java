package ch.ethz.globis.isk.persistence.jpa;

import ch.ethz.globis.isk.domain.Journal;
import ch.ethz.globis.isk.domain.jpa.JpaJournal;
import ch.ethz.globis.isk.persistence.JournalDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JpaJournalDao extends JpaDao<String, Journal> implements JournalDao {

    @Override
    protected Class<JpaJournal> getStoredClass() {
        return JpaJournal.class;
    }

    @Override
    public Journal findOneByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.EQUAL, name));
        return findOneByFilter(filterMap);
    }

    @Override
    public Journal createEntity() {
        return new JpaJournal();
    }
}
