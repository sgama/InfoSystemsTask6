package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.*;
import ch.ethz.globis.isk.util.OrderFilter;

import java.util.List;
import java.util.Map;

public interface PublicationService extends BaseService<String, Publication> {

    public Publication findOneByTitle(String title);

    public Iterable<Publication> findByTitle(String name, List<OrderFilter> orderConditions, int start, int size);

    public long countByTitle(String name);

    public Map<Long, Long> countPerYears(Long startYear, Long endYear);

    public Double averageNumberOfAuthors();

    public List<Publication> findByAuthorIdOrderedByYear(String authorId);

    public List<Publication> findByEditorIdOrderedByYear(String editorId);

    public List<Publication> findByPublisherOrderedByYear(String publisherId);

    public List<Publication> findBySchoolOrderedByYear(String schoolId);

    public List<Publication> findBySeriesOrderedByYear(String seriesId);
}