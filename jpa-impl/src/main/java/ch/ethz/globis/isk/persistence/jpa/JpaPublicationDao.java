package ch.ethz.globis.isk.persistence.jpa;

import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.domain.jpa.JpaPublication;
import ch.ethz.globis.isk.persistence.PublicationDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class JpaPublicationDao extends JpaDao<String, Publication> implements PublicationDao {

    private static String AVERAGE_NUMBER_OF_AUTHORS = "select avg(convert(count, double)) as average " + "from ( " + "select  publication.id, NVL2(publication_author.id, count(publication_author.id), 0)  as count " + "from publication LEFT JOIN publication_author ON publication.id = publication_author.id " + "group by publication.id " + ")";

    private static String COUNT_PUBLICATIONS_PER_YEAR_INTERVAL = "select year, count(*) from publication " + "where year >= :startYear and year <= :endYear group by year";

    @Override
    protected Class<JpaPublication> getStoredClass() {
        return JpaPublication.class;
    }

    @Override
    public Publication createEntity() {
        return new JpaPublication();
    }

    @Override
    public Publication findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public Map<Long, Long> countPerYears(Long startYear, Long endYear) {
        Query query = em.createNativeQuery(COUNT_PUBLICATIONS_PER_YEAR_INTERVAL);
        query.setParameter("startYear", startYear);
        query.setParameter("endYear", endYear);
        Map<Long, Long> resultMap = new TreeMap<>();
        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Long year = Long.valueOf((Integer) result[0]);
            Long count = Long.valueOf(new BigInteger(String.valueOf(result[1])).longValue());
            resultMap.put(year, count);
        }
        return resultMap;
    }

    @Override
    public Double getAverageNumberOfAuthors() {
        Query query = em.createNativeQuery(AVERAGE_NUMBER_OF_AUTHORS);
        Double average = (Double) query.getSingleResult();
        return average;
    }

    @Override
    public List<Publication> findByAuthorIdOrderedByYear(String authorId) {
        return queryByReferenceIdOrderByYear("Publication", "authors", authorId);
    }

    @Override
    public List<Publication> findByEditorIdOrderedByYear(String editorId) {
        return queryByReferenceIdOrderByYear("Publication", "editors", editorId);
    }

    @Override
    public List<Publication> findByPublisherOrderedByYear(String publisherId) {
        return queryByReferenceIdOrderByYear("Publication", "publisher", publisherId);
    }

    @Override
    public List<Publication> findBySchoolOrderedByYear(String schoolId) {
        return queryByReferenceIdOrderByYear("Publication", "school", schoolId);
    }

    @Override
    public List<Publication> findBySeriesOrderedByYear(String seriesId) {
        return queryByReferenceIdOrderByYear("Publication", "series", seriesId);
    }
}
