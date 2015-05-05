package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.Book;
import ch.ethz.globis.isk.domain.db4o.Db4oBook;
import ch.ethz.globis.isk.persistence.BookDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class Db4oBookDao extends Db4oDao<String, Book> implements BookDao {

    @Override
    public Book findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public Class getStoredClass() {
        return Db4oBook.class;
    }

    @Override
    public Book createEntity() {
        return new Db4oBook();
    }
}
