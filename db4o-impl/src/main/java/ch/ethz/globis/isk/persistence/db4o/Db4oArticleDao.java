package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.Article;
import ch.ethz.globis.isk.domain.db4o.Db4oArticle;
import ch.ethz.globis.isk.persistence.ArticleDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class Db4oArticleDao extends Db4oDao<String, Article> implements ArticleDao {

    @Override
    public Article findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public List<Article> findByJournalEditionOrderedByYear(String journalEditionId) {
        return queryByReferenceIdOrderByYear("journalEdition", journalEditionId).execute();
    }

    @Override
    public Class getStoredClass() {
        return Db4oArticle.class;
    }

    @Override
    public Article createEntity() {
        return new Db4oArticle();
    }
}
