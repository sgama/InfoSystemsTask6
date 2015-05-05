package ch.ethz.globis.isk.persistence.jpa;

import ch.ethz.globis.isk.domain.Article;
import ch.ethz.globis.isk.domain.jpa.JpaArticle;
import ch.ethz.globis.isk.persistence.ArticleDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JpaArticleDao extends JpaDao<String, Article> implements ArticleDao {

    @Override
    protected Class<JpaArticle> getStoredClass() {
        return JpaArticle.class;
    }

    @Override
    public Article createEntity() {
        return new JpaArticle();
    }

    @Override
    public Article findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public List<Article> findByJournalEditionOrderedByYear(String journalEditionId) {
        return queryByReferenceIdOrderByYear("Article", "journalEdition", journalEditionId);
    }
}
