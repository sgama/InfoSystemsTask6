package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.PhdThesis;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.persistence.PhdThesisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhdThesisServiceImpl extends BaseServiceImpl<String, PhdThesis>
        implements PhdThesisService {

    private PhdThesisDao phdThesisDao;

    @Autowired
    public PhdThesisServiceImpl(PhdThesisDao phdThesisDao) {
        this.phdThesisDao = phdThesisDao;
    }

    @Override
    public Dao<String, PhdThesis> dao() {
        return phdThesisDao;
    }
}
