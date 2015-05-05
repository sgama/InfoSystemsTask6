package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.persistence.PersonDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import ch.ethz.globis.isk.util.OrderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PersonServiceImpl extends BaseServiceImpl<String, Person> implements PersonService {

    private PersonDao personDao;

    @Autowired
    public PersonServiceImpl(PersonDao personDao) {
        this.personDao = personDao;
    }

    public Person findOneByName(String name) {
        return personDao.findOneByName(name);
    }

    public Iterable<Person> findByName(String name, List<OrderFilter> orderConditions, int start, int size) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.STRING_MATCH, name));

        return personDao.findAllByFilter(filterMap, orderConditions, start, size);
    }

    public long countByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.STRING_MATCH, name));

        return personDao.countAllByFilter(filterMap);
    }

    public Set<Person> getCoauthors(String id) {
        return personDao.getCoauthors(id);
    }

    public Long computeAuthorDistance(String firstId, String secondId) {
        return personDao.computeAuthorDistance(firstId, secondId);
    }

    @Override
    public Dao<String, Person> dao() {
        return personDao;
    }
}