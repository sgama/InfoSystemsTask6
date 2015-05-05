package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.InCollection;

import java.util.List;

public interface InCollectionService extends BaseService<String, InCollection> {

    public List<InCollection> findByBookIdOrderByYear(String bookId);
}
