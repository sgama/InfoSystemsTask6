package ch.ethz.globis.isk.persistence.mongo;

import ch.ethz.globis.isk.domain.Book;
import ch.ethz.globis.isk.domain.mongo.MongoBook;
import ch.ethz.globis.isk.persistence.BookDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MongoBookDao extends MongoDao<String, Book> implements BookDao {

    @Override
    public Book findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    protected Class<MongoBook> getStoredClass() {
        return MongoBook.class;
    }

    @Override
    protected String collection() {
        return "publication";
    }

    @Override
    public Book createEntity() {
        return new MongoBook();
    }
}
