package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.PhdThesis;

public interface PhdThesisDao extends Dao<String, PhdThesis> {

    public PhdThesis findOneByTitle(String title);
}
