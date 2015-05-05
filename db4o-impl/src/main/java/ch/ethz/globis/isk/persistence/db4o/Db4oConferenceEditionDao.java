package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.ConferenceEdition;
import ch.ethz.globis.isk.domain.db4o.Db4oConferenceEdition;
import ch.ethz.globis.isk.persistence.ConferenceEditionDao;
import com.db4o.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class Db4oConferenceEditionDao extends Db4oDao<String, ConferenceEdition> implements ConferenceEditionDao {

    @Override
    public Class getStoredClass() {
        return Db4oConferenceEdition.class;
    }

    @Override
    public ConferenceEdition createEntity() {
        return new Db4oConferenceEdition();
    }

    @Override
    public List<ConferenceEdition> findByConferenceOrderedByYear(String conferenceId) {
        Query query = db.query();
        query.constrain(Db4oConferenceEdition.class);
        query.descend("conference").descend("id").constrain(conferenceId);
        query.descend("year").orderAscending();
        return query.execute();
    }
}
