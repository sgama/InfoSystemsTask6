package ch.ethz.globis.isk.persistence.jpa;

import ch.ethz.globis.isk.domain.DomainObject;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import ch.ethz.globis.isk.util.Order;
import ch.ethz.globis.isk.util.OrderFilter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class JpaDao<K extends Serializable, T extends DomainObject> implements Dao<K, T> {

    @Autowired
    @Qualifier("entityManager")
    EntityManager em;

    @Override
    public long countAllByFilter(Map<String, Filter> filterMap) {
        CriteriaQuery<Long> query = countQueryFromFilterMap(filterMap, null);
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public long count() {
        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(getStoredClass())));
        return em.createQuery(cq).getSingleResult();
    }

    @Override
    public Iterable<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(getStoredClass());
        Root<T> entity = query.from(getStoredClass());
        query.select(entity);
        return em.createQuery(query).getResultList();
    }

    @Override
    public T findOne(K id) {
        return em.find(getStoredClass(), id);
    }

    @Override
    public T findOneByFilter(Map<String, Filter> filterMap) {
        CriteriaQuery<T> query = selectQueryFromFilterMap(filterMap, null);
        try {
            return em.createQuery(query).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap) {
        CriteriaQuery<T> query = selectQueryFromFilterMap(filterMap, null);
        return em.createQuery(query).getResultList();
    }

    @Override
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap, int start, int size) {
        CriteriaQuery<T> query = selectQueryFromFilterMap(filterMap, null);
        return em.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }

    @Override
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap, List<OrderFilter> orderList, int start, int size) {
        CriteriaQuery<T> query = selectQueryFromFilterMap(filterMap, orderList);
        return em.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }

    @Override
    public Iterable<T> findAllByFilter(Map<String, Filter> filterMap, List<OrderFilter> orderList) {
        CriteriaQuery<T> query = selectQueryFromFilterMap(filterMap, orderList);
        return em.createQuery(query).getResultList();
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
        em.persist(entity);
        return entity;
    }

    protected abstract <S extends T> Class<S> getStoredClass();

    protected Session getSession() {
        return em.unwrap(Session.class);
    }

    protected List<T> queryByReferenceIdOrderByYear(String entity, String referenceName, String referenceId) {
        String idField = referenceName + "Id";
        String findAuthorsQuery = "Select p from %s p JOIN p.%s e " + "WHERE e.id = :%s ORDER BY p.year ASC";
        findAuthorsQuery = String.format(findAuthorsQuery, entity, referenceName, idField);
        Query query = em.createQuery(findAuthorsQuery);
        query.setParameter(idField, referenceId);
        return query.getResultList();
    }

    private Predicate[] constructRestrictions(Map<String, Filter> filterMap, Root<T> entity, CriteriaBuilder cb) {
        if (filterMap == null) {
            return null;
        }
        //add filter values
        Predicate[] restrictions = new Predicate[filterMap.size()];
        int i = 0;
        for (Map.Entry<String, Filter> entry : filterMap.entrySet()) {
            String attributeName = entry.getKey();
            Filter filter = entry.getValue();
            if (Operator.STRING_MATCH.equals(filter.getOperator())) {
                Expression<String> fieldName = entity.get(attributeName);
                String filterExpression = "%" + String.valueOf(filter.getValue()) + "%";
                restrictions[i++] = cb.like(fieldName, filterExpression);
            } else {
                restrictions[i++] = cb.equal(entity.get(attributeName), filter.getValue());
            }
        }
        return restrictions;
    }

    private List<javax.persistence.criteria.Order> constructOrderList(List<OrderFilter> orderList, Root<T> entity, CriteriaBuilder cb) {
        if (orderList == null) {
            return null;
        }
        //add order fields
        List<javax.persistence.criteria.Order> criteriaOrderList = new ArrayList<>();
        for (OrderFilter orderFilter : orderList) {
            String fieldName = orderFilter.getField();
            if (Order.ASC.equals(orderFilter.getOrder())) {
                criteriaOrderList.add(cb.asc(entity.get(fieldName)));
            } else {
                criteriaOrderList.add(cb.desc(entity.get(fieldName)));
            }
        }
        return criteriaOrderList;
    }

    private CriteriaQuery<Long> countQueryFromFilterMap(Map<String, Filter> filterMap, List<OrderFilter> orderList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<T> entity = query.from(getStoredClass());
        query = query.select(cb.count(entity));
        //add filter values
        Predicate[] restrictions = constructRestrictions(filterMap, entity, cb);
        if (restrictions != null) {
            query.where(restrictions);
        }
        //add order fields
        List<javax.persistence.criteria.Order> criteriaOrderList = constructOrderList(orderList, entity, cb);
        if (criteriaOrderList != null) {
            query.orderBy(criteriaOrderList);
        }
        return query;
    }

    private CriteriaQuery<T> selectQueryFromFilterMap(Map<String, Filter> filterMap, List<OrderFilter> orderList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(getStoredClass());
        Root<T> entity = query.from(getStoredClass());
        query = query.select(entity);
        //add filter values
        Predicate[] restrictions = constructRestrictions(filterMap, entity, cb);
        if (restrictions != null) {
            query.where(restrictions);
        }
        //add order fields
        List<javax.persistence.criteria.Order> criteriaOrderList = constructOrderList(orderList, entity, cb);
        if (criteriaOrderList != null) {
            query.orderBy(criteriaOrderList);
        }
        return query;
    }
}
