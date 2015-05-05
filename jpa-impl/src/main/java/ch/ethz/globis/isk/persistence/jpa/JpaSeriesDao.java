package ch.ethz.globis.isk.persistence.jpa;

import ch.ethz.globis.isk.domain.Series;
import ch.ethz.globis.isk.domain.jpa.JpaSeries;
import ch.ethz.globis.isk.persistence.SeriesDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JpaSeriesDao extends JpaDao<String, Series> implements SeriesDao {

    @Override
    protected Class<JpaSeries> getStoredClass() {
        return JpaSeries.class;
    }

    @Override
    public Series createEntity() {
        return new JpaSeries();
    }

    @Override
    public Series findOneByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.EQUAL, name));
        return findOneByFilter(filterMap);
    }
}
