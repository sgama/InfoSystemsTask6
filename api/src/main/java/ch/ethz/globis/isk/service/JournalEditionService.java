package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.JournalEdition;

import java.util.List;

public interface JournalEditionService extends BaseService<String, JournalEdition> {

    public List<JournalEdition> findByJournalOrdered(String journalId);
}
