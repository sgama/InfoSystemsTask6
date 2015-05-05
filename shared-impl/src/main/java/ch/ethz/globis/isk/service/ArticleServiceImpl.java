package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Article;
import ch.ethz.globis.isk.persistence.ArticleDao;
import ch.ethz.globis.isk.persistence.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArticleServiceImpl extends BaseServiceImpl<String, Article> implements ArticleService {

    private ArticleDao articleDao;

    @Autowired
    protected ArticleServiceImpl(ArticleDao dao) {
        this.articleDao = dao;
    }

    @Override
    public Dao<String, Article> dao() {
        return articleDao;
    }

    @Override
    public List<Article> findByJournalEditionOrderedByYear(String journalEditionId) {
        return articleDao.findByJournalEditionOrderedByYear(journalEditionId);
    }
}
