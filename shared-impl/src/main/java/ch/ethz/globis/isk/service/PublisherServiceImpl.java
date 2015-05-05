package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Publisher;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.persistence.PublisherDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import ch.ethz.globis.isk.util.OrderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PublisherServiceImpl extends BaseServiceImpl<String, Publisher> implements PublisherService {

    private PublisherDao publisherDao;

    @Autowired
    public PublisherServiceImpl(PublisherDao publisherDao) {
        this.publisherDao = publisherDao;
    }

    public Publisher findOneByName(String name) {
        return publisherDao.findOneByName(name);
    }

    public Iterable<Publisher> findByName(String name, List<OrderFilter> orderConditions, int start, int size) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.STRING_MATCH, name));

        return publisherDao.findAllByFilter(filterMap, orderConditions, start, size);
    }

    public long countByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.STRING_MATCH, name));

        return publisherDao.countAllByFilter(filterMap);
    }

    @Override
    public Dao<String, Publisher> dao() {
        return publisherDao;
    }
}