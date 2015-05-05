package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.InProceedings;

import java.util.List;

public interface InProceedingsDao extends Dao<String, InProceedings> {

    public InProceedings findOneByTitle(String title);

    public List<InProceedings> findByProceedingsIdOrderByYear(String proceedingsId);
}
