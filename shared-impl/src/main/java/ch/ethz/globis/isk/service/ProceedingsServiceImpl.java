package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Proceedings;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.persistence.ProceedingsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProceedingsServiceImpl extends BaseServiceImpl<String, Proceedings>
        implements ProceedingsService{

    private ProceedingsDao proceedingsDao;

    @Autowired
    public ProceedingsServiceImpl(ProceedingsDao proceedingsDao) {
        this.proceedingsDao = proceedingsDao;
    }

    @Override
    public Dao<String, Proceedings> dao() {
        return proceedingsDao;
    }

}