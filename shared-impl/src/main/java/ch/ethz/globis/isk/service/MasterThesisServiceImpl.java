package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.MasterThesis;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.persistence.MasterThesisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterThesisServiceImpl extends BaseServiceImpl<String, MasterThesis>
        implements MasterThesisService {

    private MasterThesisDao masterThesisDao;

    @Autowired
    public MasterThesisServiceImpl(MasterThesisDao masterThesisDao) {
        this.masterThesisDao = masterThesisDao;
    }

    @Override
    public Dao<String, MasterThesis> dao() {
        return masterThesisDao;
    }
}
