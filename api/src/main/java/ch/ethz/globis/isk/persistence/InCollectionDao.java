package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.InCollection;

import java.util.List;

public interface InCollectionDao extends Dao<String, InCollection> {

    public InCollection findOneByTitle(String title);

    public List<InCollection> findByBookIdOrderByYear(String bookId);
}