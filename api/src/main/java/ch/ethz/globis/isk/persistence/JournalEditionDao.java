package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.JournalEdition;

import java.util.List;

public interface JournalEditionDao extends Dao<String, JournalEdition> {

    List<JournalEdition> findByJournalIdOrdered(String journalId);
}
