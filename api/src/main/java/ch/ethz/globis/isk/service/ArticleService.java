package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Article;

import java.util.List;

public interface ArticleService extends BaseService<String, Article> {

    public List<Article> findByJournalEditionOrderedByYear(String journalEditionId);
}

