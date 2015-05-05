package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Publisher;
import ch.ethz.globis.isk.util.OrderFilter;

import java.util.List;

public interface PublisherService extends BaseService<String, Publisher> {

    public Publisher findOneByName(String name);

    public Iterable<Publisher> findByName(String name, List<OrderFilter> orderConditions, int start, int size);

    public long countByName(String name);
}