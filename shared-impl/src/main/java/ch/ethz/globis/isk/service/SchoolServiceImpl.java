package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.School;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.persistence.SchoolDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import ch.ethz.globis.isk.util.OrderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchoolServiceImpl extends BaseServiceImpl<String, School> implements SchoolService {

    private SchoolDao schoolDao;

    @Autowired
    public SchoolServiceImpl(SchoolDao schoolDao) {
        this.schoolDao = schoolDao;
    }

    public School findOneByName(String name) {
        return schoolDao.findOneByName(name);
    }

    public Iterable<School> findByName(String name, List<OrderFilter> orderConditions, int start, int size) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.STRING_MATCH, name));

        return schoolDao.findAllByFilter(filterMap, orderConditions, start, size);
    }

    public long countByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.STRING_MATCH, name));

        return schoolDao.countAllByFilter(filterMap);
    }

    @Override
    public Dao<String, School> dao() {
        return schoolDao;
    }
}