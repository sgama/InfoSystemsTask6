package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.DomainObject;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.OrderFilter;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents a Service object used for managing objects of type T.
 *
 * As opposed to a DAO object, which is concerned with the persistent storage
 * of objects, a Service object is concerned with the business logic..
 *
 * @param <K>               The type of the key object.
 * @param <T>               The type of the domain object.
 */
public interface BaseService<K extends Serializable, T extends DomainObject> {

    /**
     * Create an object of type T. This methods is meant to abstract
     * the fact that there could be more implementations of T.
     * @return                      A new object of type T. This object is not
     *                              persisted yet.
     */
    public T createEntity();

    /**
     * Inserts a new object in the database.
     * @param entity                The new object.
     * @param <S>                   The type of the object.
     * @return                      True if the object was inserted, false otherwise.
     */
    public <S extends T> S insert(S entity);

    /**
     * Inserts a set of objects in the database.
     * @param entities              A set of objects to be inserted.
     * @param <S>                   The type of the objects.
     * @return                      The inserted objects.
     */
    public <S extends T> Iterable<S> insert(Iterable<S> entities);

    /**
     * Retrieves an object from the database, using the unique identified id.
     * @param id                    The id that uniquely identifies the object.
     * @return                      The object identified by id, if it is stored in the database
     *                              or null if no object identified by id is stored in the database.
     */
    public T findOne(K id);

    /**
     * Returns all the objects of type T.
     * @return
     */
    public Iterable<T> findAll();

    /**
     * Returns a page of objects of type T.
     * @param start                 The number of the page of objects.
     * @param size                  The size of an object page.
     * @return                      A page of objects.
     */
    public Iterable<T> find(int start, int size);

    /**
     * Returns a page of objects of type T as they are ordered by the OrderFilter
     * objects in orderFilterList.
     * @param orderFilterList       A list of OrderFilter objects used to order the results.
     * @param start                 The size of an object page.
     * @param size                  The size of an object page.
     * @return                      A page of objects.
     */
    public Iterable<T> find(List<OrderFilter> orderFilterList, int start, int size);

    /**
     * Retrieve a page from the result of querying the database based on the filterMap and subsequently ordering
     * the results based on the orderFilterList.
     *
     * @param filterMap             The set of filters.
     * @param orderFilterList       A list of OrderFilter objects used to order the results.
     * @param start                 The number of the page of objects.
     * @param size                  The size of an object page.
     * @return                      The object page matching the filter.
     */
    public Iterable<T> find(Map<String, Filter> filterMap, List<OrderFilter> orderFilterList, int start, int size);

    /**
     * Counts all of the objects of type T.
     * @return                      The number of objects of type T.
     */
    public long count();

    /**
     * Sets wether the cache should be used or not.
     * @param useCache
     */
    public void setUseCache(boolean useCache);

    /**
     * @return                          A reference to the current TransactionManager.
     */
    public TransactionManager getTM();
}
