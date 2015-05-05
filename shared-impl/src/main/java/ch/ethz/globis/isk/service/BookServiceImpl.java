package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Book;
import ch.ethz.globis.isk.persistence.BookDao;
import ch.ethz.globis.isk.persistence.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl extends BaseServiceImpl<String, Book> implements BookService {

    private BookDao bookDao;

    @Autowired
    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Dao<String, Book> dao() {
        return bookDao;
    }
}
