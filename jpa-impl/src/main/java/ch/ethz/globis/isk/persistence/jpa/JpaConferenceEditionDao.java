package ch.ethz.globis.isk.persistence.jpa;

import ch.ethz.globis.isk.domain.ConferenceEdition;
import ch.ethz.globis.isk.domain.jpa.JpaConferenceEdition;
import ch.ethz.globis.isk.persistence.ConferenceEditionDao;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.util.List;

@Repository
public class JpaConferenceEditionDao extends JpaDao<String, ConferenceEdition> implements ConferenceEditionDao {

    @Override
    protected Class<JpaConferenceEdition> getStoredClass() {
        return JpaConferenceEdition.class;
    }

    @Override
    public ConferenceEdition createEntity() {
        return new JpaConferenceEdition();
    }

    @Override
    public List<ConferenceEdition> findByConferenceOrderedByYear(String conferenceId) {
        String findAuthorsQuery = "Select ce from ConferenceEdition ce JOIN ce.conference c " + "WHERE c.id = :conferenceId ORDER BY ce.year ASC";
        Query query = em.createQuery(findAuthorsQuery);
        query.setParameter("conferenceId", conferenceId);
        return query.getResultList();
    }
}
