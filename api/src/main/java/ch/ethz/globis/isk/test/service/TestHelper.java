package ch.ethz.globis.isk.test.service;

import ch.ethz.globis.isk.domain.*;
import ch.ethz.globis.isk.service.MasterThesisService;
import ch.ethz.globis.isk.service.PublisherService;
import ch.ethz.globis.isk.service.SchoolService;
import ch.ethz.globis.isk.service.SeriesService;

import java.util.Random;

import static org.junit.Assert.assertTrue;

public class TestHelper {

    private static int instanceCounter = 0;

    public static void assertSize(String message, long expected, long actual) {
        assertTrue(message, expected <= actual);
    }

    public static MasterThesis newMasterThesis(MasterThesisService service) {
        MasterThesis masterThesis = service.createEntity();
        basicPublicationFields(masterThesis);
        return masterThesis;
    }

    private static School newSchool(SchoolService schoolService) {
        School school = schoolService.createEntity();
        school.setId(id());
        school.setName(name());
        return school;
    }

    private static Series newSeries(SeriesService seriesService) {
        Series series = seriesService.createEntity();
        series.setId(id());
        series.setName(name());
        return series;
    }

    private static Publisher newPublisher(PublisherService service) {
        Publisher publisher = service.createEntity();
        publisher.setId(id());
        publisher.setName(name());
        return publisher;
    }

    private static void basicPublicationFields(Publication publication) {
        publication.setId(id());
        publication.setTitle(title());
        publication.setElectronicEdition(ee());
        publication.setYear(year());
    }

    private static String id() {
        return stringField("id");
    }

    private static String title() {
        return stringField("title");
    }

    private static String name() {
        return stringField("name");
    }
    private static String ee() {
        return stringField("ee");
    }

    private static int year() {
        Random random = new Random();
        return random.nextInt(23) + 1990;
    }

    private static String stringField(String root) {
        return root + "_" + getAndIncreaseCounter();
    }

    private static int getAndIncreaseCounter() {
        return ++instanceCounter;
    }

}
