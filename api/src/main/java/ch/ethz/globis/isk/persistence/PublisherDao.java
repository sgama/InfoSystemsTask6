package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.Publisher;

public interface PublisherDao extends Dao<String, Publisher> {

    public Publisher findOneByName(String name);
}
