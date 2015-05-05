package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.Book;

public interface BookDao extends Dao<String, Book> {

    public Book findOneByTitle(String title);
}
