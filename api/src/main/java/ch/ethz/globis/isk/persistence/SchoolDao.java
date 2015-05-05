package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.School;

public interface SchoolDao extends Dao<String, School> {

    public School findOneByName(String name);
}
