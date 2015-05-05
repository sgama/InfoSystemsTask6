package ch.ethz.globis.isk.parser.processor;

import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Publisher;
import ch.ethz.globis.isk.domain.School;
import ch.ethz.globis.isk.domain.Series;
import ch.ethz.globis.isk.service.PersonService;
import ch.ethz.globis.isk.service.PublisherService;
import ch.ethz.globis.isk.service.SchoolService;
import ch.ethz.globis.isk.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PublicationXMLHelper {

    private PublisherService publisherService;
    private SeriesService seriesService;
    private SchoolService schoolService;
    private PersonService personService;
    private EntityCache entityCache;

    @Autowired
    public PublicationXMLHelper(PersonService personService, PublisherService publisherService,
                                SchoolService schoolService, SeriesService seriesService,
                                EntityCache entityCache) {
        this.personService = personService;
        this.publisherService = publisherService;
        this.schoolService = schoolService;
        this.seriesService = seriesService;
        this.entityCache = entityCache;
    }

    public Series findSeries(String seriesName) {
        String seriesId = FieldMapper.seriesId(seriesName);
        seriesId = FieldMapper.SERIES_ID_PREFIX + seriesId;
        Series series = entityCache.findSeries(seriesId);
        if (series == null) {
            series = seriesService.createEntity();
            series.setId(seriesId);
            series.setName(seriesName);
        }
        entityCache.putSeries(seriesId, series);
        return series;
    }

    public Publisher findPublisher(String publisherName) {
        String publisherId = FieldMapper.publisherId(publisherName);
        publisherId = FieldMapper.PUBLISHER_ID_PREFIX + publisherId;
        Publisher publisher = entityCache.findPublisher(publisherId);

        if (publisher == null) {
            publisher = publisherService.createEntity();
            publisher.setId(publisherId);
            publisher.setName(publisherName);
        }

        entityCache.putPublisher(publisherId, publisher);
        return publisher;
    }

    public School findSchool(String schoolName) {
        String schoolId = FieldMapper.schoolId(schoolName);
        schoolId = FieldMapper.SCHOOl_ID_PREFIX + schoolId;
        School school = entityCache.findSchool(schoolId);
        if (school == null) {
            school = schoolService.createEntity();
            school.setId(schoolId);
            school.setName(schoolName);
        }
        entityCache.putSchool(schoolId, school);
        return school;
    }

    public Person findPerson(String personName) {
        String personId = FieldMapper.personId(personName);
        personId = FieldMapper.PERSON_ID_PREFIX + personId;
        Person person = entityCache.findPerson(personId);
        if (person == null) {
            person = personService.createEntity();
            person.setId(personId);
            person.setName(personName);
        }
        entityCache.put(personId, person);
        return person;
    }
}