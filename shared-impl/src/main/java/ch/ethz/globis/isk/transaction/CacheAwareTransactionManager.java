package ch.ethz.globis.isk.transaction;


import ch.ethz.globis.isk.service.TransactionManager;
import ch.ethz.globis.isk.service.cache.RequestResultCache;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Cache-aware wrapper for the TransactionManager.
 */
public abstract class CacheAwareTransactionManager implements TransactionManager {

    @Autowired
    private RequestResultCache cache;

    @Override
    public void beginTransaction() {
        begin();
    }

    @Override
    public void commitTransaction() {
        try {
            commit();
        } catch (Exception ex) {
            handleTransactionFailure();
            throw ex;
        }
    }

    @Override
    public void rollbackTransaction() {
        handleTransactionFailure();
        rollback();
    }

    public abstract void commit();
    public abstract void rollback();
    public abstract void begin();

    private void handleTransactionFailure() {
        cache.clear();
    }
}