package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.Article;

import java.util.List;

public interface ArticleDao extends Dao<String, Article> {

    public Article findOneByTitle(String title);

    public List<Article> findByJournalEditionOrderedByYear(String journalEditionId);
}
