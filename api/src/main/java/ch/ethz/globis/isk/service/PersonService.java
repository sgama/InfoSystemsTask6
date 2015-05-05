package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.util.OrderFilter;

import java.util.List;
import java.util.Set;

public interface PersonService extends BaseService<String, Person> {

    public Person findOneByName(String name);

    public Iterable<Person> findByName(String name, List<OrderFilter> orderConditions, int start, int size);

    public long countByName(String name);

    public Set<Person> getCoauthors(String id);

    public Long computeAuthorDistance(String firstId, String secondId);
}