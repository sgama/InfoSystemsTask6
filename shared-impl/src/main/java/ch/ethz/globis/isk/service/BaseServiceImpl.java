package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.DomainObject;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.service.cache.RequestResultCache;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.OrderFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<K extends Serializable, T extends DomainObject> implements BaseService<K, T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseServiceImpl.class);

    private static boolean useCache = true;

    @Autowired
    private RequestResultCache cache;

    @Autowired
    private TransactionManager tm;

    @Autowired
    private Environment environment;

    public abstract Dao<K, T> dao();

    public long count() {
        return dao().count();
    }

    public Iterable<T> findAll() {
        return dao().findAll();
    }

    public Iterable<T> find(int start, int size) {
        return dao().findAllByFilter(null, start, size);
    }

    public Iterable<T> find(List<OrderFilter> orderFilterList, int start, int size) {
        return dao().findAllByFilter(null, orderFilterList, start, size);
    }

    public Iterable<T> find(Map<String, Filter> filterMap, List<OrderFilter> orderFilterList, int start, int size) {
        return dao().findAllByFilter(filterMap, orderFilterList, start, size);
    }

    public T findOne(K key) {
        if (key == null) {
            return null;
        }
        if (useCache) {
            LOG.debug("Searching cache using key {}.", key);
            T cached = (T) cache.get(key);
            if (cached != null) {
                LOG.debug("Returning cached object {}.", cached);
                return cached;
            }
        }
        LOG.debug("Database search using key {}.", key);
        T stored = dao().findOne(key);
        LOG.debug("Database search with key {} returned object {}", key, stored);
        if (useCache) {
            cache.put(key, stored);
        }
        return stored;
    }

    public <S extends T> S insert(S entity) {
        if (entity == null) {
            return null;
        }
        if (useCache) {
            cache.put(entity.getId(), entity);
        }
        return doInsert(entity);
    }

    private <S extends T> S doInsert(S entity) {
        return dao().insert(entity);
    }

    public <S extends T> Iterable<S> insert(Iterable<S> entities) {
        return dao().insert(entities);
    }

    public T createEntity() {
        return dao().createEntity();
    }

    public void setUseCache(boolean useCache) {
        BaseServiceImpl.useCache = useCache;
    }

    @Override
    public TransactionManager getTM() {
        return tm;
    }
}
