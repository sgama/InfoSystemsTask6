package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.DomainObject;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import ch.ethz.globis.isk.util.Order;
import ch.ethz.globis.isk.util.OrderFilter;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public abstract class Db4oDao<K extends Serializable, T extends DomainObject> implements Dao<K, T> {

    @Autowired
    protected ObjectContainer db;

    public abstract Class getStoredClass();

    @Override
    public long count() {
        Query query = db.query();
        query.constrain(getStoredClass());
        return query.execute().size();
    }

    @Override
    public long countAllByFilter(Map<String, Filter> filterMap) {
        Query query = db.query();
        query.constrain(getStoredClass());
        return applyFilters(query, filterMap).execute().size();
    }

    @Override
    public Iterable<T> findAll() {
        return db.query(getStoredClass());
    }

    @Override
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap) {
        Query query = db.query();
        query.constrain(getStoredClass());
        return applyFilters(query, filterMap).execute();
    }

    @Override
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap, List<OrderFilter> orderList) {
        Query query = db.query();
        query.constrain(getStoredClass());
        applyFilters(query, filterMap);
        applySorts(query, orderList);
        return query.execute();
    }

    @Override
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap, List<OrderFilter> orderList, int start, int size) {
        Query query = db.query();
        query.constrain(getStoredClass());
        applyFilters(query, filterMap);
        applySorts(query, orderList);
        List<T> results = query.execute();
        return pagedResults(results, start, size);
    }

    @Override
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap, int start, int size) {
        Query query = db.query();
        query.constrain(getStoredClass());
        applyFilters(query, filterMap);
        List<T> results = query.execute();
        return pagedResults(results, start, size);
    }

    @Override
    public final T findOne(K id) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("id", new Filter(Operator.EQUAL, id));
        return findOneByFilter(filterMap);
    }

    @Override
    public T findOneByFilter(Map<String, Filter> filterMap) {
        Query query = db.query();
        query.constrain(getStoredClass());
        final ObjectSet<Object> results = applyFilters(query, filterMap).execute();
        if (results.size() == 0) {
            return null;
        }
        return (T) results.get(0);
    }

    @Override
    public <S extends T> Iterable<S> insert(Iterable<S> entities) {
        for (S entity : entities) {
            insert(entity);
        }
        return entities;
    }

    @Override
    public <S extends T> S insert(S entity) {
        if (entity == null) {
            return null;
        }
        db.store(entity);
        return entity;
    }

    protected Query queryByReferenceIdOrderByYear(String referenceName, String referenceId) {
        final String FIELD_ID = "id";
        final String FIELD_YEAR = "year";
        final String FIELD_TITLE = "title";
        Query query = db.query();
        query.constrain(getStoredClass());
        query.descend(referenceName).descend(FIELD_ID).constrain(referenceId);
        query.descend(FIELD_YEAR).orderAscending();
        query.descend(FIELD_TITLE).orderAscending();
        return query;
    }

    private Query applySorts(Query query, List<OrderFilter> orderList) {
        if (orderList == null) {
            return query;
        }
        for (OrderFilter orderFilter : orderList) {
            String attribute = orderFilter.getField();
            Order order = orderFilter.getOrder();
            if (Order.ASC.equals(order)) {
                query.descend(attribute).orderAscending();
            } else {
                query.descend(attribute).orderDescending();
            }
        }
        return query;
    }

    private Query applyFilters(Query query, Map<String, Filter> filterMap) {
        if (filterMap == null) {
            return query;
        }
        for (Map.Entry<String, Filter> entry : filterMap.entrySet()) {
            String attribute = entry.getKey();
            Filter filter = entry.getValue();
            if (Operator.STRING_MATCH.equals(filter.getOperator())) {
                query.descend(attribute).constrain(filter.getValue()).like();
            } else {
                query.descend(attribute).constrain(filter.getValue());
            }
        }
        return query;
    }

    private Iterable<T> pagedResults(List<T> resultList, int start, int limit) {
        int end = start + limit;
        if (end >= resultList.size()) {
            end = resultList.size();
        }
        return resultList.subList(start, end);
    }
}
