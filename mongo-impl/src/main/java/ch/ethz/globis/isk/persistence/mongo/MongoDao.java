package ch.ethz.globis.isk.persistence.mongo;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ch.ethz.globis.isk.domain.DomainObject;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import ch.ethz.globis.isk.util.Order;
import ch.ethz.globis.isk.util.OrderFilter;

@Repository
public abstract class MongoDao<K extends Serializable, T extends DomainObject> implements Dao<K, T> {

    protected abstract <S extends T> Class<S> getStoredClass();

    protected abstract String collection();

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public T findOne(K id) {
        return mongoOperations.findById(id, getStoredClass(), collection());
    }

    @Override
    public T findOneByFilter(Map<String, Filter> filterMap) {
        Query query = basicQuery();
        applyFilters(query, filterMap);
        return mongoOperations.findOne(query, getStoredClass(), collection());
    }

    @Override
    public long countAllByFilter(Map<String, Filter> filterMap) {
        Query query = basicQuery();
        applyFilters(query, filterMap);
        return mongoOperations.count(query, collection());
    }

    @Override
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap) {
        Query query = basicQuery();
        applyFilters(query, filterMap);
        return mongoOperations.find(query, getStoredClass(), collection());
    }

    @Override
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap, int start, int size) {
        Query query = basicQuery();
        applyFilters(query, filterMap);
        applyPaging(query, start, size);
        return mongoOperations.find(query, getStoredClass(), collection());
    }

    @Override
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap, List<OrderFilter> orderList) {
        Query query = basicQuery();
        applyFilters(query, filterMap);
        applySorts(query, orderList);
        return mongoOperations.find(query, getStoredClass(), collection());
    }

    @Override
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap, List<OrderFilter> orderList, int start, int size) {
        Query query = basicQuery();
        applyFilters(query, filterMap);
        applySorts(query, orderList);
        applyPaging(query, start, size);
        return mongoOperations.find(query, getStoredClass(), collection());
    }

    @Override
    public Iterable<T> findAll() {
        return mongoOperations.findAll(getStoredClass(), collection());
    }

    @Override
    public long count() {
        return mongoOperations.count(basicQuery(), getStoredClass());
    }

    @Override
    public <S extends T> S insert(S entity) {
        if (entity == null) {
            return null;
        }
        mongoOperations.insert(entity);
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> insert(Iterable<S> entities) {
        for (S entity : entities) {
            insert(entity);
        }
        return entities;
    }

    protected List<T> queryByReferenceIdOrderByYear(String referenceName, String referenceId) {
        Map<String, Filter> filterMap = new TreeMap<>();
        filterMap.put(referenceName, new Filter(Operator.EQUAL, referenceId));
        List<OrderFilter> orderFilters = Arrays.asList(new OrderFilter("year", Order.ASC));
        return (List<T>) findAllByFilter(filterMap, orderFilters);
    }

    private Query applyFilters(Query query, Map<String, Filter> filterMap) {
        if (filterMap == null) {
            return query;
        }
        for (Map.Entry<String, Filter> entry : filterMap.entrySet()) {
            String attribute = entry.getKey();
            Filter filter = entry.getValue();
            Criteria constraint;
            if (Operator.EQUAL.equals(filter.getOperator())) {
                constraint = where(attribute).is(filter.getValue());
            } else {
                String f = (String) filter.getValue();
                f = Pattern.quote(f);
                //Escape special characters for regex
                constraint = where(attribute).regex(f);
            }
            query.addCriteria(constraint);
        }
        return query;
    }

    private Query applySorts(Query query, List<OrderFilter> orderList) {
        if (orderList == null) {
            return query;
        }
        for (OrderFilter orderFilter : orderList) {
            query.with(toSort(orderFilter));
        }
        return query;
    }

    private Query applyPaging(Query query, int start, int size) {
        int page = start / size;
        return query.with(new PageRequest(page, size));
    }

    protected Query basicQuery() {
        return new Query(where(DefaultMongoTypeMapper.DEFAULT_TYPE_KEY).is(getStoredClass().getCanonicalName()));
    }

    protected MongoTemplate template() {
        return (MongoTemplate) mongoOperations;
    }

    private Sort toSort(OrderFilter orderFilter) {
        Order order = orderFilter.getOrder();
        if (Order.DESC.equals(order)) {
            return new Sort(Sort.Direction.DESC, orderFilter.getField());
        } else {
            return new Sort(Sort.Direction.ASC, orderFilter.getField());
        }
    }
}
