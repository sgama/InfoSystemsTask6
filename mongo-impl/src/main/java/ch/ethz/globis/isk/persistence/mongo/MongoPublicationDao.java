package ch.ethz.globis.isk.persistence.mongo;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.domain.mongo.MongoPublication;
import ch.ethz.globis.isk.persistence.PublicationDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;

@Repository
public class MongoPublicationDao extends MongoDao<String, Publication> implements PublicationDao {

    @Override
    protected Query basicQuery() {
        return new Query();
    }

    @Override
    public Publication findOneByTitle(String title) {
        Map<String, Filter> filterMap = new TreeMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public Map<Long, Long> countPerYears(Long startYear, Long endYear) {
        Aggregation aggregation = newAggregation(match(where("year").gte(startYear).lte(endYear)), group("year").count().as("value"), sort(Sort.Direction.DESC, "year"));
        AggregationResults<KeyValuePair> aggregationResults = mongoOperations.aggregate(aggregation, collection(), KeyValuePair.class);
        Map<Long, Long> results = new TreeMap<>();
        for (KeyValuePair pair : aggregationResults.getMappedResults()) {
            results.put(Long.valueOf(pair.getKey()), Long.valueOf(pair.getValue()));
        }
        return results;
    }

    @Override
    public Double getAverageNumberOfAuthors() {
        GroupByResults<NumberOfAuthors> result = mongoOperations.group(collection(), GroupBy.key("name").initialDocument("{ count: 0, size: 0}").reduceFunction("function(doc, prev) { " + "prev.count += obj.authors.length; prev.size += 1 }"), NumberOfAuthors.class);
        Iterator<NumberOfAuthors> i = result.iterator();
        if (i.hasNext()) {
            return i.next().getAverage();
        }
        return -1d;
    }

    @Override
    public Publication createEntity() {
        return new MongoPublication();
    }

    @Override
    protected Class<MongoPublication> getStoredClass() {
        return MongoPublication.class;
    }

    @Override
    protected String collection() {
        return "publication";
    }

    @Override
    public List<Publication> findByAuthorIdOrderedByYear(String authorId) {
        return queryByReferenceIdOrderByYear("authors.$id", authorId);
    }

    @Override
    public List<Publication> findByEditorIdOrderedByYear(String editorId) {
        return queryByReferenceIdOrderByYear("editors.$id", editorId);
    }

    @Override
    public List<Publication> findByPublisherOrderedByYear(String publisherId) {
        return queryByReferenceIdOrderByYear("publisher.$id", publisherId);
    }

    @Override
    public List<Publication> findBySchoolOrderedByYear(String schoolId) {
        return queryByReferenceIdOrderByYear("school.$id", schoolId);
    }

    @Override
    public List<Publication> findBySeriesOrderedByYear(String seriesId) {
        return queryByReferenceIdOrderByYear("series.$id", seriesId);
    }
}

class NumberOfAuthors {

    long count;

    long size;

    public long getCount() {
        return count;
    }

    void setCount(long count) {
        this.count = count;
    }

    long getSize() {
        return size;
    }

    void setSize(long size) {
        this.size = size;
    }

    double getAverage() {
        return ((double) count) / size;
    }
}

class KeyValuePair {

    @Id
    String key;

    String value;

    KeyValuePair() {
    }

    String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }
}
