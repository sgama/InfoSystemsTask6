package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.JournalEdition;
import ch.ethz.globis.isk.domain.db4o.Db4oJournalEdition;
import ch.ethz.globis.isk.persistence.JournalEditionDao;
import com.db4o.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class Db4oJournalEditionDao extends Db4oDao<String, JournalEdition> implements JournalEditionDao {

    @Override
    public Class getStoredClass() {
        return Db4oJournalEdition.class;
    }

    @Override
    public JournalEdition createEntity() {
        return new Db4oJournalEdition();
    }

    @Override
    public List<JournalEdition> findByJournalIdOrdered(String journalId) {
        Query query = db.query();
        query.constrain(getStoredClass());
        query.descend("journal").descend("id").constrain(journalId);
        query.descend("year").orderAscending();
        query.descend("volume").orderAscending();
        query.descend("number").orderAscending();
        return query.execute();
    }
}
