package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.Series;

public interface SeriesDao extends Dao<String, Series> {

    public Series findOneByName(String name);
}
