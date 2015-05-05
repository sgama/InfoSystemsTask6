package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Journal;
import ch.ethz.globis.isk.domain.JournalEdition;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.persistence.JournalDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import ch.ethz.globis.isk.util.OrderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JournalServiceImpl extends BaseServiceImpl<String, Journal> implements JournalService {

    private JournalDao journalDao;

    @Autowired
    public JournalServiceImpl(JournalDao journalDao) {
        this.journalDao = journalDao;
    }

    public Journal findOneByName(String name) {
        return journalDao.findOneByName(name);
    }

    public Iterable<Journal> findByName(String name, List<OrderFilter> orderConditions, int start, int size) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.STRING_MATCH, name));

        return journalDao.findAllByFilter(filterMap, orderConditions, start, size);
    }

    public long countByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.STRING_MATCH, name));

        return journalDao.countAllByFilter(filterMap);
    }

    public JournalEdition findByEdition(String journalId, String editionId) {
        Journal journal = journalDao.findOne(journalId);
        for (JournalEdition edition : journal.getEditions()) {
            if (edition.getId().equals(editionId)) {
                return edition;
            }
        }
        return null;
    }

    @Override
    public Dao<String, Journal> dao() {
        return journalDao;
    }
}
