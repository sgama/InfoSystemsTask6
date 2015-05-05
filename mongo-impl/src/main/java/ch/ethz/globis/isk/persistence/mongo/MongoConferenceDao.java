package ch.ethz.globis.isk.persistence.mongo;

import ch.ethz.globis.isk.domain.*;
import ch.ethz.globis.isk.domain.mongo.MongoConference;
import ch.ethz.globis.isk.persistence.ConferenceDao;
import ch.ethz.globis.isk.persistence.ProceedingsDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class MongoConferenceDao extends MongoDao<String, Conference> implements ConferenceDao {

    @Autowired
    ProceedingsDao proceedingsDao;

    @Override
    public Conference findOneByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.EQUAL, name));
        return findOneByFilter(filterMap);
    }

    @Override
    public Long countAuthorsForConference(String confId) {
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
        return Long.valueOf(persons.size());
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

    @Override
    public Conference createEntity() {
        return new MongoConference();
    }

    @Override
    protected Class<MongoConference> getStoredClass() {
        return MongoConference.class;
    }

    @Override
    protected String collection() {
        return "conference";
    }
}
