package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.Proceedings;

public interface ProceedingsDao extends Dao<String, Proceedings> {

    public Proceedings findOneByTitle(String title);
}
