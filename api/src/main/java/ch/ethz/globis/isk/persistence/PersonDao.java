package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.Person;

import java.util.Set;

public interface PersonDao extends Dao<String, Person> {

    public Person findOneByName(String name);

    public Set<Person> getCoauthors(String id);

    public Long computeAuthorDistance(String firstId, String secondId);
}