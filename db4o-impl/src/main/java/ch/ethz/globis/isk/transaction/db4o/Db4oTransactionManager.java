package ch.ethz.globis.isk.transaction.db4o;

import ch.ethz.globis.isk.service.cache.RequestResultCache;
import ch.ethz.globis.isk.transaction.CacheAwareTransactionManager;
import com.db4o.ObjectContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Db4oTransactionManager extends CacheAwareTransactionManager {

    @Autowired
    ObjectContainer db;

    @Autowired
    RequestResultCache cache;

    @Override
    public void rollback() {
        db.rollback();
    }

    @Override
    public void begin() {
    }

    @Override
    public void commit() {
        db.commit();
    }
}
