package ch.ethz.globis.isk.transaction.mongo;

import ch.ethz.globis.isk.transaction.CacheAwareTransactionManager;
import org.springframework.stereotype.Component;

@Component
public class MongoTransactionManager extends CacheAwareTransactionManager {

    @Override
    public void begin() {
    }

    @Override
    public void commit() {
    }

    @Override
    public void rollback() {
    }
}
