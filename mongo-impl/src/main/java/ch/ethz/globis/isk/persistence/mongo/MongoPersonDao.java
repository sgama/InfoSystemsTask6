package ch.ethz.globis.isk.persistence.mongo;

import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.domain.mongo.MongoPerson;
import ch.ethz.globis.isk.persistence.PersonDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class MongoPersonDao extends MongoDao<String, Person> implements PersonDao {

    @Override
    public Person findOneByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.EQUAL, name));
        return findOneByFilter(filterMap);
    }

    @Override
    public Set<Person> getCoauthors(String id) {
        Person person = findOne(id);
        return getCoauthors(person);
    }

    @Override
    public Long computeAuthorDistance(String firstId, String secondId) {
        HashMap<String, Long> distanceFirst = new HashMap<>();
        HashMap<String, Long> distanceSecond = new HashMap<>();
        HashSet<Person> nextFirst = new HashSet<>();
        HashSet<Person> nextSecond = new HashSet<>();
        if (firstId.equals(secondId)) {
            return 0L;
        }
        distanceFirst.put(firstId, 0L);
        distanceSecond.put(secondId, 0L);
        nextFirst.addAll(getCoauthors(firstId));
        nextSecond.addAll(getCoauthors(secondId));
        int iteration = 0;
        while (!nextFirst.isEmpty() && !nextSecond.isEmpty()) {
            iteration++;
            HashSet<Person> nextIteration = new HashSet<>();
            for (Person person : nextFirst) {
                if (distanceSecond.containsKey(person.getId())) {
                    return distanceSecond.get(person.getId()) + iteration;
                } else {
                    distanceFirst.put(person.getId(), Long.valueOf(iteration));
                    nextIteration.addAll(getCoauthors(person));
                }
            }
            nextFirst = nextIteration;
            nextIteration.clear();
            for (Person person : nextSecond) {
                if (distanceFirst.containsKey(person.getId())) {
                    return distanceFirst.get(person.getId()) + iteration;
                } else {
                    distanceSecond.put(person.getId(), Long.valueOf(iteration));
                    nextIteration.addAll(getCoauthors(person));
                }
            }
            nextSecond = nextIteration;
        }
        return -1L;
    }

    @Override
    public Person createEntity() {
        return new MongoPerson();
    }

    @Override
    protected Class<MongoPerson> getStoredClass() {
        return MongoPerson.class;
    }

    @Override
    protected String collection() {
        return "person";
    }

    private Set<Person> getCoauthors(Person person) {
        Set<Person> coauthors = new HashSet<>();
        if (person == null) {
            return coauthors;
        }
        for (Publication publication : person.getAuthoredPublications()) {
            coauthors.addAll(publication.getAuthors());
        }
        for (Publication publication : person.getEditedPublications()) {
            coauthors.addAll(publication.getEditors());
        }
        coauthors.remove(person);
        return coauthors;
    }
}
