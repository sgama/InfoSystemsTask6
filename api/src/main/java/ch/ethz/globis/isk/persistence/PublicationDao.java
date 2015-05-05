package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.Publication;

import java.util.List;
import java.util.Map;

public interface PublicationDao extends Dao<String, Publication> {

    public Publication findOneByTitle(String title);

    public Map<Long, Long> countPerYears(Long startYear, Long endYear);

    public Double getAverageNumberOfAuthors();

    public List<Publication> findByAuthorIdOrderedByYear(String authorId);

    public List<Publication> findByEditorIdOrderedByYear(String editorId);

    public List<Publication> findByPublisherOrderedByYear(String publisherId);

    public List<Publication> findBySchoolOrderedByYear(String schoolId);

    public List<Publication> findBySeriesOrderedByYear(String seriesId);
}