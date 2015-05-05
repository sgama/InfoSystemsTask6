package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Series;
import ch.ethz.globis.isk.util.OrderFilter;

import java.util.List;

public interface SeriesService extends BaseService<String, Series> {

    public Series findOneByName(String name);

    public Iterable<Series> findByName(String name, List<OrderFilter> orderConditions, int start, int size);

    public long countByName(String name);
}