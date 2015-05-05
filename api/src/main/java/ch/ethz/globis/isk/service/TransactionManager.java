package ch.ethz.globis.isk.service;

/**
 * Isolate transactional behaviour
 */
public interface TransactionManager {

    /**
     * Begin a new transaction.
     */
    public void beginTransaction();

    /**
     * Commit the current transaction.
     */
    public void commitTransaction();

    /**
     * Rollback the current transaction, if it is active.
     */
    public void rollbackTransaction();
}