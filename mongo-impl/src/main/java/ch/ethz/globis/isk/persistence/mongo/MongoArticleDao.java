package ch.ethz.globis.isk.persistence.mongo;

import ch.ethz.globis.isk.domain.Article;
import ch.ethz.globis.isk.domain.mongo.MongoArticle;
import ch.ethz.globis.isk.persistence.ArticleDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MongoArticleDao extends MongoDao<String, Article> implements ArticleDao {

    @Override
    protected Class<MongoArticle> getStoredClass() {
        return MongoArticle.class;
    }

    @Override
    protected String collection() {
        return "publication";
    }

    @Override
    public Article createEntity() {
        return new MongoArticle();
    }

    @Override
    public Article findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public List<Article> findByJournalEditionOrderedByYear(String journalEditionId) {
        return queryByReferenceIdOrderByYear("journalEdition.$id", journalEditionId);
    }
}
