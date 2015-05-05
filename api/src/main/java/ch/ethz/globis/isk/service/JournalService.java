package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Journal;
import ch.ethz.globis.isk.domain.JournalEdition;
import ch.ethz.globis.isk.util.OrderFilter;

import java.util.List;

public interface JournalService extends BaseService<String, Journal> {

    public Journal findOneByName(String name);

    public Iterable<Journal> findByName(String name, List<OrderFilter> orderConditions, int start, int size);

    public long countByName(String name);

    public JournalEdition findByEdition(String journalId, String editionId);
}
