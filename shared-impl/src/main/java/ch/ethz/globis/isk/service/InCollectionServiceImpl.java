package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.InCollection;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.persistence.InCollectionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InCollectionServiceImpl extends BaseServiceImpl<String, InCollection> implements InCollectionService {

    private InCollectionDao inCollectionDao;

    @Autowired
    public InCollectionServiceImpl(InCollectionDao inCollectionDao) {
        this.inCollectionDao = inCollectionDao;
    }

    @Override
    public Dao<String, InCollection> dao() {
        return inCollectionDao;
    }

    @Override
    public List<InCollection> findByBookIdOrderByYear(String bookId) {
        return inCollectionDao.findByBookIdOrderByYear(bookId);
    }
}
