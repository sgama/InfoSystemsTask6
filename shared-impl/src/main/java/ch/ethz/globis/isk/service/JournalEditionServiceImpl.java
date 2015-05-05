package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.JournalEdition;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.persistence.JournalEditionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JournalEditionServiceImpl extends BaseServiceImpl<String, JournalEdition> implements JournalEditionService {

    private JournalEditionDao dao;

    @Autowired
    public JournalEditionServiceImpl(JournalEditionDao dao) {
        this.dao = dao;
    }

    @Override
    public Dao<String, JournalEdition> dao() {
        return dao;
    }

    @Override
    public List<JournalEdition> findByJournalOrdered(String journalId) {
        return dao.findByJournalIdOrdered(journalId);
    }
}
