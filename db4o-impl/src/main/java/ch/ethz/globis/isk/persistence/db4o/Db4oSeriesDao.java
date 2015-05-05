package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.Series;
import ch.ethz.globis.isk.domain.db4o.Db4oSeries;
import ch.ethz.globis.isk.persistence.SeriesDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class Db4oSeriesDao extends Db4oDao<String, Series> implements SeriesDao {

    @Override
    public Series findOneByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.EQUAL, name));
        return findOneByFilter(filterMap);
    }

    @Override
    public Series createEntity() {
        return new Db4oSeries();
    }

    @Override
    public Class getStoredClass() {
        return Db4oSeries.class;
    }
}
