package ch.ethz.globis.isk.persistence.jpa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.ethz.globis.isk.domain.Conference;
import ch.ethz.globis.isk.domain.ConferenceEdition;
import ch.ethz.globis.isk.domain.InProceedings;
import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Proceedings;
import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.domain.jpa.JpaConference;
import ch.ethz.globis.isk.persistence.ConferenceDao;
import ch.ethz.globis.isk.persistence.ProceedingsDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;

@Repository
public class JpaConferenceDao extends JpaDao<String, Conference> implements ConferenceDao {

    @Autowired
    ProceedingsDao proceedingsDao;

    @Override
    protected Class<JpaConference> getStoredClass() {
        return JpaConference.class;
    }

    @Override
    public Conference createEntity() {
        return new JpaConference();
    }

    @Override
    public Conference findOneByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.EQUAL, name));
        return findOneByFilter(filterMap);
    }

    @Override
    public Long countAuthorsForConference(String confId) {
        return Long.valueOf(findAuthorsForConference(confId).size());
    }

    @Override
    public Set<Person> findAuthorsForConference(String confId) {
        Conference conference = findOne(confId);
        Set<Person> persons = new HashSet<>();
        for (ConferenceEdition edition : conference.getEditions()) {
            Proceedings proceedings = edition.getProceedings();
            if (proceedings != null) {
                persons.addAll(proceedings.getAuthors());
                persons.addAll(proceedings.getEditors());
                for (InProceedings inProceedings : proceedings.getPublications()) {
                    persons.addAll(inProceedings.getEditors());
                    persons.addAll(inProceedings.getAuthors());
                }
            }
        }
        return persons;
    }

    @Override
    public Set<Publication> findPublicationsForConference(String confId) {
        Conference conference = findOne(confId);
        Set<Publication> publications = new HashSet<>();
        for (ConferenceEdition edition : conference.getEditions()) {
            Proceedings proceedings = edition.getProceedings();
            if (proceedings != null) {
                publications.add(proceedings);
                publications.addAll(proceedings.getPublications());
            }
        }
        return publications;
    }

    @Override
    public Long countPublicationsForConference(String confId) {
        Conference conference = findOne(confId);
        long count = 0;
        for (ConferenceEdition edition : conference.getEditions()) {
            Proceedings proceedings = edition.getProceedings();
            if (proceedings != null) {
                count++;
                count += proceedings.getPublications().size();
            }
        }
        return count;
    }
}
