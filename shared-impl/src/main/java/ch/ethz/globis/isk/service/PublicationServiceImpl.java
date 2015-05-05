package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.persistence.PublicationDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import ch.ethz.globis.isk.util.OrderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PublicationServiceImpl extends BaseServiceImpl<String, Publication> implements PublicationService {

    private PublicationDao publicationDao;

    @Autowired
    public PublicationServiceImpl(PublicationDao publicationDao) {
        this.publicationDao = publicationDao;
    }

    public Publication findOneByTitle(String title) {
        return publicationDao.findOneByTitle(title);
    }

    public Iterable<Publication> findByTitle(String name, List<OrderFilter> orderConditions, int start, int size) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.STRING_MATCH, name));
        return publicationDao.findAllByFilter(filterMap, orderConditions, start, size);
    }

    public long countByTitle(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.STRING_MATCH, name));
        return publicationDao.countAllByFilter(filterMap);
    }

    public Map<Long, Long> countPerYears(Long startYear, Long endYear) {
        return publicationDao.countPerYears(startYear, endYear);
    }

    public Double averageNumberOfAuthors() {
        return publicationDao.getAverageNumberOfAuthors();
    }

    @Override
    public List<Publication> findByAuthorIdOrderedByYear(String authorId) {
        return publicationDao.findByAuthorIdOrderedByYear(authorId);
    }

    @Override
    public List<Publication> findByEditorIdOrderedByYear(String editorId) {
        return publicationDao.findByEditorIdOrderedByYear(editorId);
    }

    @Override
    public List<Publication> findByPublisherOrderedByYear(String publisherId) {
        return publicationDao.findByPublisherOrderedByYear(publisherId);
    }

    @Override
    public List<Publication> findBySchoolOrderedByYear(String schoolId) {
        return publicationDao.findBySchoolOrderedByYear(schoolId);
    }

    @Override
    public List<Publication> findBySeriesOrderedByYear(String seriesId) {
        return publicationDao.findBySeriesOrderedByYear(seriesId);
    }

    @Override
    public Dao<String, Publication> dao() {
        return publicationDao;
    }
}
