package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.Journal;

public interface JournalDao extends Dao<String, Journal> {

    public Journal findOneByName(String name);
}
