package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.DomainObject;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.OrderFilter;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *  Represents a Data Access Object for managing objects of type T.
 *
 *  As opposed to a Service object, which is concerned with business logic,
 *  a DAO object is concerned with the persistent storage of domain objects.
 *
 *  This Dao object contains the basic
 * @param <K>               The type of the key object.
 * @param <T>               The type of the domain object.
 */
public interface Dao<K extends Serializable, T extends DomainObject> {

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
     * Find one object using a Map of filters.
     * @param filterMap             The set of filters used to retrieve the object, as a map.
     * @return                      A single object matched using the Map of filters.
     */
    public T findOneByFilter(Map<String, Filter> filterMap);

    /**
     * Cont all objects that match all of the filters in the filterMap.
     * @param filterMap             The set of filters.
     * @return                      The number of objects in the database that match the set of filters.
     */
    public long countAllByFilter(Map<String, Filter> filterMap);

    /**
     * Retrieve a number of object from the database that match all of filters in the filterMap.
     * @param filterMap             The set of filters.
     * @return                      All objects matching the filterMap.
     */
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap);

    /**
     * Retrieves a page of objects from the database. All of these objects should match
     * the filters in the filterMap.
     * @param filterMap             The set of filters.
     * @param start                 The number of the page of objects.
     * @param size                  The size of an object page.
     * @return                      The object page matching the filter.
     */
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap, int start, int size);

    /**
     * Retrieve a number of object from the database that match all of filters in the filterMap.
     * The objects are ordered according to the OrderFilter objects orderList.
     *
     * @param filterMap             The set of filters.
     * @param orderList             A list of OrderFilter objects used to order the results.
     * @return                      All objects matching the filters in filterMap, ordered according
     *                              to the OrderFilter objects in orderList.
     */
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap, List<OrderFilter> orderList);

    /**
     * Retrieve a page from the result of querying the database based on the filterMap and subsequently ordering
     * the results based on the orderList.
     *
     * @param filterMap             The set of filters.
     * @param orderList             A list of OrderFilter objects used to order the results.
     * @param start                 The number of the page of objects.
     * @param size                  The size of an object page.
     * @return                      The object page matching the filter.
     */
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap, List<OrderFilter> orderList, int start, int size);

    /**
     * Returns all the objects of type T.
     * @return
     */
    public Iterable<T> findAll();

    /**
     * Counts all of the objects of type T.
     * @return                      The number of objects of type T.
     */
    public long count();
}
