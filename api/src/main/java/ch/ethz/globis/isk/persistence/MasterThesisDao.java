package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.MasterThesis;

public interface MasterThesisDao extends Dao<String, MasterThesis> {

    public MasterThesis findOneByTitle(String title);
}
