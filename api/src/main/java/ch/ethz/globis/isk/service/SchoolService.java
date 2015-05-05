package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.School;
import ch.ethz.globis.isk.util.OrderFilter;

import java.util.List;

public interface SchoolService extends BaseService<String, School> {

    public School findOneByName(String name);

    public Iterable<School> findByName(String name, List<OrderFilter> orderConditions, int start, int size);

    public long countByName(String name);
}