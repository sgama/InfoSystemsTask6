package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.InProceedings;

import java.util.List;

public interface InProceedingsService extends BaseService<String, InProceedings> {

    public List<InProceedings> findByProceedingsIdOrderByYear(String proceedingsId);
}
