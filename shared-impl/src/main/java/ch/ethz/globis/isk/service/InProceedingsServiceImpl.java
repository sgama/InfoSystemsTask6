package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.InProceedings;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.persistence.InProceedingsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InProceedingsServiceImpl extends BaseServiceImpl<String, InProceedings>
        implements InProceedingsService {

    private InProceedingsDao inProceedingsDao;

    @Autowired
    public InProceedingsServiceImpl(InProceedingsDao inProceedingsDao) {
        this.inProceedingsDao = inProceedingsDao;
    }

    @Override
    public Dao<String, InProceedings> dao() {
        return inProceedingsDao;
    }

    @Override
    public List<InProceedings> findByProceedingsIdOrderByYear(String proceedingsId) {
        return inProceedingsDao.findByProceedingsIdOrderByYear(proceedingsId);
    }
}
