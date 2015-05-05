package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Series;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.persistence.SeriesDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import ch.ethz.globis.isk.util.OrderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeriesServiceImpl extends BaseServiceImpl<String, Series> implements SeriesService {

    private SeriesDao seriesDao;

    @Autowired
    public SeriesServiceImpl(SeriesDao seriesDao) {
        this.seriesDao = seriesDao;
    }

    public Series findOneByName(String name) {
        return seriesDao.findOneByName(name);
    }

    public Iterable<Series> findByName(String name, List<OrderFilter> orderConditions, int start, int size) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.STRING_MATCH, name));

        return seriesDao.findAllByFilter(filterMap, orderConditions, start, size);
    }

    public long countByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.STRING_MATCH, name));

        return seriesDao.countAllByFilter(filterMap);
    }

    @Override
    public Dao<String, Series> dao() {
        return seriesDao;
    }
}