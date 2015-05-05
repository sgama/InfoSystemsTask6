package ch.ethz.globis.isk.persistence.db4o;

import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.domain.db4o.Db4oPublication;
import ch.ethz.globis.isk.persistence.PublicationDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class Db4oPublicationDao extends Db4oDao<String, Publication> implements PublicationDao {

    @Override
    public Publication createEntity() {
        return new Db4oPublication();
    }

    @Override
    public Publication findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public Map<Long, Long> countPerYears(Long startYear, Long endYear) {
        Map<Long, Long> resultMap = new TreeMap<>();
        for (long year = startYear; year <= endYear; year++) {
            Query query = db.query();
            query.constrain(getStoredClass());
            query.descend("year").constrain(year).equal();
            Long number = Long.valueOf(query.execute().size());
            if (number > 0) {
                resultMap.put(year, number);
            }
        }
        return resultMap;
    }

    @Override
    public Double getAverageNumberOfAuthors() {
        Query query = db.query();
        query.constrain(getStoredClass());
        final ObjectSet<Publication> results = query.execute();
        long sum = 0;
        long count = 0;
        while (results.hasNext()) {
            Publication publication = results.next();
            Set<Person> authors = publication.getAuthors();
            if (authors != null && authors.size() > 0) {
                sum += publication.getAuthors().size();
            }
            count += 1;
        }
        return sum / (1.0 * count);
    }

    @Override
    public Class getStoredClass() {
        return Db4oPublication.class;
    }

    @Override
    public List<Publication> findByAuthorIdOrderedByYear(String authorId) {
        return queryByReferenceIdOrderByYear("authors", authorId).execute();
    }

    @Override
    public List<Publication> findByEditorIdOrderedByYear(String editorId) {
        return queryByReferenceIdOrderByYear("editors", editorId).execute();
    }

    @Override
    public List<Publication> findByPublisherOrderedByYear(String publisherId) {
        return queryByReferenceIdOrderByYear("publisher", publisherId).execute();
    }

    @Override
    public List<Publication> findBySchoolOrderedByYear(String schoolId) {
        return queryByReferenceIdOrderByYear("school", schoolId).execute();
    }

    @Override
    public List<Publication> findBySeriesOrderedByYear(String seriesId) {
        return queryByReferenceIdOrderByYear("series", seriesId).execute();
    }
}
