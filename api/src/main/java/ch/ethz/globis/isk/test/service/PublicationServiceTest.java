package ch.ethz.globis.isk.test.service;

import ch.ethz.globis.isk.domain.*;
import ch.ethz.globis.isk.service.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PublicationServiceTest {

    private TransactionManager tm;
    private ProceedingsService proceedingsService;
    private InProceedingsService inProceedingsService;
    private ConferenceEditionService conferenceEditionService;
    private ConferenceService conferenceService;
    private PersonService personService;
    private BookService bookService;
    private PublisherService publisherService;
    private PhdThesisService phdThesisService;
    private SchoolService schoolService;
    private SeriesService seriesService;
    private PublicationService publicationService;

    private ArticleService articleService;
    private JournalService journalService;
    private JournalEditionService journalEditionService;
    private MasterThesisService masterThesisService;
    private InCollectionService inCollectionService;

    @Before
    public void setUp() {
        publicationService.setUseCache(false);
    }

    @After
    public void cleanupTransaction() {
        //if the current transaction was not commited for some reason
        try { tm.rollbackTransaction(); } catch (Exception ex) {}
    }

    @Test
    public void test_1_insertConferenceInfo() {
        //test data
        String TEST_ID = "1";
        String PROCEEDINGS_ID = "test-proc-" + TEST_ID;
        String PROCEEDINGS_TITLE = "Test Proceedings " + TEST_ID;

        String PERSON_ID = "test-person-" + TEST_ID;
        String PERSON_NAME = "Test Person " + TEST_ID;

        String INPROCEEDINGS_ID_1 = "test-inproceedings-1-" + TEST_ID;
        String INPROCEEDINGS_TITLE_1 = "Test InProceedings 1 " + TEST_ID;
        String INPROCEEDINGS_ID_2 = "test-inproceedings-2" + TEST_ID;
        String INPROCEEDINGS_TITLE_2 = "Test InProceedings 1 " + TEST_ID;

        String CONFERENCE_ID = "test-conference-" + TEST_ID;
        String CONFERENCE_NAME = "Test Conference " + TEST_ID;

        String CONF_ED_ID = "test-confed-" + TEST_ID;
        Integer currentYear = 2014;

        tm.beginTransaction();

        //Create a dummy proceedings
        Proceedings proceedings = proceedingsService.createEntity();
        proceedings.setId(PROCEEDINGS_ID);
        proceedings.setTitle(PROCEEDINGS_TITLE);
        proceedings.setYear(2001);

        //Create an editor
        Person person = personService.createEntity();
        person.setId(PERSON_ID);
        person.setName(PERSON_NAME);
        person.getEditedPublications().add(proceedings);
        proceedings.getEditors().add(person);

        //Create some inProceedings objects
        InProceedings inProceedings1 = inProceedingsService.createEntity();
        inProceedings1.setId(INPROCEEDINGS_ID_1);
        inProceedings1.setTitle(INPROCEEDINGS_TITLE_1);
        inProceedings1.setYear(currentYear);
        inProceedings1.getAuthors().add(person);
        person.getAuthoredPublications().add(inProceedings1);
        inProceedings1.setProceedings(proceedings);
        proceedings.getPublications().add(inProceedings1);

        InProceedings inProceedings2 = inProceedingsService.createEntity();
        inProceedings2.setId(INPROCEEDINGS_ID_2);
        inProceedings2.setTitle(INPROCEEDINGS_TITLE_2);
        inProceedings2.setYear(currentYear);
        inProceedings2.getAuthors().add(person);
        person.getAuthoredPublications().add(inProceedings2);
        inProceedings2.setProceedings(proceedings);
        proceedings.getPublications().add(inProceedings2);

        //Create newConference and newConference edition
        Conference conference = conferenceService.createEntity();
        conference.setId(CONFERENCE_ID);
        conference.setName(CONFERENCE_NAME);

        ConferenceEdition conferenceEdition = conferenceEditionService.createEntity();
        conferenceEdition.setId(CONF_ED_ID);
        conferenceEdition.setConference(conference);
        conference.getEditions().add(conferenceEdition);

        conferenceEdition.setProceedings(proceedings);
        proceedings.setConferenceEdition(conferenceEdition);

        //now save all the entities
        inProceedingsService.insert(inProceedings1);
        inProceedingsService.insert(inProceedings2);
        proceedingsService.insert(proceedings);
        personService.insert(person);
        conferenceService.insert(conference);
        conferenceEditionService.insert(conferenceEdition);

        tm.commitTransaction();

        tm.beginTransaction();
        //check objects
        person = personService.findOne(PERSON_ID);
        assertNotNull(person);
        assertEquals(PERSON_ID, person.getId());
        assertEquals(PERSON_NAME, person.getName());
        assertEquals(2, person.getAuthoredPublications().size());
        assertEquals(1, person.getEditedPublications().size());

        conference = conferenceService.findOne(CONFERENCE_ID);
        assertNotNull(conference);
        assertEquals(CONFERENCE_ID, conference.getId());
        assertEquals(CONFERENCE_NAME, conference.getName());
        assertEquals(1, conference.getEditions().size());

        conferenceEdition = conferenceEditionService.findOne(CONF_ED_ID);
        assertNotNull(conferenceEdition);
        assertEquals(conference, conferenceEdition.getConference());

        proceedings = proceedingsService.findOne(PROCEEDINGS_ID);
        assertNotNull(proceedings);
        assertEquals(PROCEEDINGS_ID, proceedings.getId());
        assertEquals(PROCEEDINGS_TITLE, proceedings.getTitle());
        assertEquals(conferenceEdition, proceedings.getConferenceEdition());
        assertEquals(proceedings, conferenceEdition.getProceedings());
        assertEquals(1, proceedings.getEditors().size());
        assertEquals(0, proceedings.getAuthors().size());
        assertEquals(2, proceedings.getPublications().size());

        inProceedings1 = inProceedingsService.findOne(INPROCEEDINGS_ID_1);
        assertNotNull(inProceedings1);
        assertEquals(INPROCEEDINGS_ID_1, inProceedings1.getId());
        assertEquals(INPROCEEDINGS_TITLE_1, inProceedings1.getTitle());
        assertEquals(1, inProceedings1.getAuthors().size());
        assertEquals(0, inProceedings1.getEditors().size());
        assertEquals(proceedings, inProceedings1.getProceedings());

        inProceedings1 = inProceedingsService.findOne(INPROCEEDINGS_ID_2);
        assertNotNull(inProceedings2);
        assertEquals(INPROCEEDINGS_ID_2, inProceedings2.getId());
        assertEquals(INPROCEEDINGS_TITLE_2, inProceedings2.getTitle());
        assertEquals(1, inProceedings2.getAuthors().size());
        assertEquals(0, inProceedings2.getEditors().size());
        assertEquals(proceedings, inProceedings2.getProceedings());

        assertTrue(proceedings.getPublications().contains(inProceedings1));
        assertTrue(proceedings.getPublications().contains(inProceedings2));

        tm.commitTransaction();
    }

    @Test
    public void test_2_insertPublisher() {
        //test data
        String TEST_ID = "2";
        String BOOK_ID = "test-book-" + TEST_ID;
        String BOOK_TITLE = "Test Book " + TEST_ID;
        String PHD_THESIS_ID = "test-phdthesis-1" + TEST_ID;
        String PHD_THESIS_TITLE = "Test PHD Thesis 1" + TEST_ID;
        String PERSON_ID = "test-person-" + TEST_ID;
        String PERSON_NAME = "Test Person " + TEST_ID;
        String PUBLISHER_ID = "test-publisher-" + TEST_ID;
        String PUBLISHER_NAME = "Test Publisher " + TEST_ID;
        Integer currentYear = 2014;

        //insert objects
        tm.beginTransaction();
        Book book = bookService.createEntity();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setYear(currentYear);

        PhdThesis phdThesis = phdThesisService.createEntity();
        phdThesis.setId(PHD_THESIS_ID);
        phdThesis.setTitle(PHD_THESIS_TITLE);
        phdThesis.setYear(currentYear);

        Person person = personService.createEntity();
        person.setId(PERSON_ID);
        person.setName(PERSON_NAME);
        person.getAuthoredPublications().add(book);
        book.getAuthors().add(person);

        person.getAuthoredPublications().add(phdThesis);
        phdThesis.getAuthors().add(person);

        Publisher publisher = publisherService.createEntity();
        publisher.setId(PUBLISHER_ID);
        publisher.setName(PUBLISHER_NAME);

        book.setPublisher(publisher);
        publisher.getPublications().add(book);

        phdThesis.setPublisher(publisher);
        publisher.getPublications().add(phdThesis);

        personService.insert(person);
        publisherService.insert(publisher);
        bookService.insert(book);
        phdThesisService.insert(phdThesis);

        tm.commitTransaction();

        tm.beginTransaction();
        //check objects
        //check publisher
        publisher = publisherService.findOne(PUBLISHER_ID);
        assertNotNull(publisher.getPublications());
        assertEquals(2, publisher.getPublications().size());
        assertEquals(PUBLISHER_ID, publisher.getId());
        assertEquals(PUBLISHER_NAME, publisher.getName());

        //check book
        book = bookService.findOne(BOOK_ID);
        assertNotNull(book);
        assertEquals(BOOK_ID, book.getId());
        assertEquals(BOOK_TITLE, book.getTitle());
        assertNotNull(book.getPublisher());
        assertEquals(PUBLISHER_ID, book.getPublisher().getId());
        assertEquals(PUBLISHER_NAME, book.getPublisher().getName());

        //check phd thesis
        phdThesis = phdThesisService.findOne(PHD_THESIS_ID);
        assertNotNull(phdThesis);
        assertEquals(PHD_THESIS_ID, phdThesis.getId());
        assertEquals(PHD_THESIS_TITLE, phdThesis.getTitle());
        assertNotNull(phdThesis.getPublisher());
        assertEquals(PUBLISHER_ID, phdThesis.getPublisher().getId());
        assertEquals(PUBLISHER_NAME, phdThesis.getPublisher().getName());

        tm.commitTransaction();
    }

    @Test
    public void test_3_insertSchool() {
        //Test data
        String TEST_ID = "3";
        String PHD_THESIS_ID = "test-phdthesis-" + TEST_ID;
        String PHD_THESIS_TITLE = "Test PHD Thesis " + TEST_ID;
        String MASTER_THESIS_ID = "test-master-" + TEST_ID;
        String MASTER_THESIS_TITLE = "Test Master " + TEST_ID;
        String PERSON_ID = "test-person-" + TEST_ID;
        String PERSON_NAME = "Test Person " + TEST_ID;
        String SCHOOL_ID = "test-school-" + TEST_ID;
        String SCHOOL_NAME = "Test School " + TEST_ID;
        Integer currentYear = 2014;

        //Insert objects
        tm.beginTransaction();
        PhdThesis phdThesis = phdThesisService.createEntity();
        phdThesis.setId(PHD_THESIS_ID);
        phdThesis.setTitle(PHD_THESIS_TITLE);
        phdThesis.setYear(currentYear);

        MasterThesis masterThesis = masterThesisService.createEntity();
        masterThesis.setId(MASTER_THESIS_ID);
        masterThesis.setTitle(MASTER_THESIS_TITLE);
        masterThesis.setYear(currentYear);

        Person person = personService.createEntity();
        person.setId(PERSON_ID);
        person.setName(PERSON_NAME);
        person.getAuthoredPublications().add(masterThesis);
        masterThesis.getAuthors().add(person);

        person.getAuthoredPublications().add(phdThesis);
        phdThesis.getAuthors().add(person);

        School school = schoolService.createEntity();
        school.setId(SCHOOL_ID);
        school.setName(SCHOOL_NAME);
        school.getPublications().add(masterThesis);
        masterThesis.setSchool(school);
        school.getPublications().add(phdThesis);
        phdThesis.setSchool(school);

        personService.insert(person);
        schoolService.insert(school);
        masterThesisService.insert(masterThesis);
        phdThesisService.insert(phdThesis);

        tm.commitTransaction();

        tm.beginTransaction();
        //check objects
        school = schoolService.findOne(SCHOOL_ID);
        assertNotNull(school);
        assertEquals(2, school.getPublications().size());

        person = personService.findOne(PERSON_ID);
        assertNotNull(person);
        assertEquals(PERSON_ID, person.getId());
        assertEquals(PERSON_NAME, person.getName());
        assertEquals(2, person.getAuthoredPublications().size());
        assertEquals(0, person.getEditedPublications().size());

        masterThesis = masterThesisService.findOne(MASTER_THESIS_ID);
        assertNotNull(masterThesis);
        assertEquals(MASTER_THESIS_ID, masterThesis.getId());
        assertEquals(MASTER_THESIS_TITLE, masterThesis.getTitle());
        assertEquals(school, masterThesis.getSchool());

        phdThesis = phdThesisService.findOne(PHD_THESIS_ID);
        assertNotNull(phdThesis);
        assertEquals(PHD_THESIS_ID, phdThesis.getId());
        assertEquals(PHD_THESIS_TITLE, phdThesis.getTitle());
        assertEquals(school, phdThesis.getSchool());

        tm.commitTransaction();
    }

    @Test
    public void test_4_insertSeries() {
        //Test data
        String TEST_ID = "4";
        String PROCEEDINGS_ID = "test-proce-" + TEST_ID;
        String PROCEEDINGS_TITLE = "Test Proceedings " + TEST_ID;
        String BOOK_ID = "test-book-" + TEST_ID;
        String BOOK_TITLE = "Test Book " + TEST_ID;
        String PERSON_ID = "test-person-" + TEST_ID;
        String PERSON_NAME = "Test Person " + TEST_ID;
        String SERIES_ID = "test-series-" + TEST_ID;
        String SERIES_NAME = "Test Series " + TEST_ID;
        Integer currentYear = 2014;
        //Insert objects
        tm.beginTransaction();
        Proceedings proceedings = proceedingsService.createEntity();
        proceedings.setId(PROCEEDINGS_ID);
        proceedings.setTitle(PROCEEDINGS_TITLE);
        proceedings.setYear(currentYear);

        Book book = bookService.createEntity();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setYear(currentYear);
        Person person = personService.createEntity();
        person.setId(PERSON_ID);
        person.setName(PERSON_NAME);
        person.getEditedPublications().add(proceedings);
        proceedings.getEditors().add(person);
        person.getAuthoredPublications().add(book);
        book.getAuthors().add(person);

        Series series = seriesService.createEntity();
        series.setId(SERIES_ID);
        series.setName(SERIES_NAME);
        series.getPublications().add(book);
        book.setSeries(series);
        series.getPublications().add(proceedings);
        proceedings.setSeries(series);

        personService.insert(person);
        bookService.insert(book);
        proceedingsService.insert(proceedings);
        seriesService.insert(series);
        tm.commitTransaction();

        tm.beginTransaction();
        //check objects
        person = personService.findOne(PERSON_ID);
        assertNotNull(person);
        assertEquals(PERSON_ID, person.getId());
        assertEquals(PERSON_NAME, person.getName());
        assertEquals(1, person.getAuthoredPublications().size());
        assertEquals(1, person.getEditedPublications().size());

        series = seriesService.findOne(SERIES_ID);
        assertNotNull(series);
        assertEquals(SERIES_ID, series.getId());
        assertEquals(SERIES_NAME, series.getName());
        assertEquals(2, series.getPublications().size());

        book = bookService.findOne(BOOK_ID);
        assertNotNull(book);
        assertEquals(BOOK_ID, book.getId());
        assertEquals(BOOK_TITLE, book.getTitle());
        assertEquals(series, book.getSeries());
        assertEquals(1, book.getAuthors().size());
        assertEquals(0, book.getEditors().size());

        proceedings = proceedingsService.findOne(PROCEEDINGS_ID);
        assertNotNull(proceedings);
        assertEquals(PROCEEDINGS_ID, proceedings.getId());
        assertEquals(PROCEEDINGS_TITLE, proceedings.getTitle());
        assertEquals(series, proceedings.getSeries());
        assertEquals(0, proceedings.getAuthors().size());
        assertEquals(1, proceedings.getEditors().size());
        tm.commitTransaction();
    }

    @Test
    public void test_5_insertJournalInfo() {
        //Test data
        String TEST_ID = "5";
        String PERSON_ID = "test-person-" + TEST_ID;
        String PERSON_NAME = "Test Person " + TEST_ID;
        String JOURNAL_ID = "test-journal-" + TEST_ID;
        String JOURNAL_NAME = "Test Journal " + TEST_ID;
        String JOURNAL_ED_ID = "test-journal-ed_id " + TEST_ID;
        Integer JOURNAL_ED_YEAR = 2001;
        String JOURNAL_ED_VOL = "1";
        String JOURNAL_ED_NUMBER = "2";
        String ARTICLE_ID = "test-article-id" + TEST_ID;
        String ARTICLE_TITLE = "Test Article " + TEST_ID;
        //Insert objects
        tm.beginTransaction();
        Article article = articleService.createEntity();
        article.setId(ARTICLE_ID);
        article.setTitle(ARTICLE_TITLE);
        article.setYear(JOURNAL_ED_YEAR);

        Person person = personService.createEntity();
        person.setId(PERSON_ID);
        person.setName(PERSON_NAME);
        person.getAuthoredPublications().add(article);
        article.getAuthors().add(person);

        Journal journal = journalService.createEntity();
        journal.setId(JOURNAL_ID);
        journal.setName(JOURNAL_NAME);

        JournalEdition journalEdition = journalEditionService.createEntity();
        journalEdition.setId(JOURNAL_ED_ID);
        journalEdition.setYear(JOURNAL_ED_YEAR);
        journalEdition.setNumber(JOURNAL_ED_NUMBER);
        journalEdition.setVolume(JOURNAL_ED_VOL);
        journalEdition.setJournal(journal);
        journal.getEditions().add(journalEdition);
        journalEdition.getPublications().add(article);
        article.setJournalEdition(journalEdition);

        articleService.insert(article);
        personService.insert(person);
        journalService.insert(journal);
        journalEditionService.insert(journalEdition);
        tm.commitTransaction();

        tm.beginTransaction();
        //Check objects
        article = articleService.findOne(ARTICLE_ID);
        assertNotNull(article);
        assertEquals(ARTICLE_ID, article.getId());
        assertEquals(ARTICLE_TITLE, article.getTitle());
        assertEquals(JOURNAL_ED_YEAR, article.getYear());
        assertEquals(1, article.getAuthors().size());
        assertEquals(0, article.getEditors().size());

        journal = journalService.findOne(JOURNAL_ID);
        assertNotNull(journal);
        assertEquals(JOURNAL_ID, journal.getId());
        assertEquals(JOURNAL_NAME, journal.getName());
        assertEquals(1, journal.getEditions().size());

        journalEdition = journalEditionService.findOne(JOURNAL_ED_ID);
        assertNotNull(journalEdition);
        assertEquals(JOURNAL_ED_ID, journalEdition.getId());
        assertEquals(JOURNAL_ED_NUMBER, journalEdition.getNumber());
        assertEquals(JOURNAL_ED_VOL, journalEdition.getVolume());
        assertEquals(JOURNAL_ED_YEAR, journalEdition.getYear());

        person = personService.findOne(PERSON_ID);
        assertNotNull(person);
        assertEquals(PERSON_ID, person.getId());
        assertEquals(PERSON_NAME, person.getName());
        assertEquals(1, person.getAuthoredPublications().size());
        assertEquals(0, person.getEditedPublications().size());

        tm.commitTransaction();
    }

    @Test
    public void test_6_retrievePublicationsByAuthor() {
        //test data
        String TEST_ID = "6";
        String BOOK_ID = "test-book-" + TEST_ID;
        String BOOK_TITLE = "Test Book " + TEST_ID;
        Integer BOOK_YEAR = 2001;
        String PHD_THESIS_ID = "test-phdthesis-1" + TEST_ID;
        String PHD_THESIS_TITLE = "Test PHD Thesis 1" + TEST_ID;
        Integer PHD_THESIS_YEAR =  2000;
        String PERSON_ID = "test-person-" + TEST_ID;
        String PERSON_NAME = "Test Person " + TEST_ID;
        tm.beginTransaction();
        Book book = bookService.createEntity();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setYear(BOOK_YEAR);

        PhdThesis phdThesis = phdThesisService.createEntity();
        phdThesis.setId(PHD_THESIS_ID);
        phdThesis.setTitle(PHD_THESIS_TITLE);
        phdThesis.setYear(PHD_THESIS_YEAR);

        Person person = personService.createEntity();
        person.setId(PERSON_ID);
        person.setName(PERSON_NAME);
        person.getAuthoredPublications().add(book);
        person.getAuthoredPublications().add(phdThesis);
        book.getAuthors().add(person);
        phdThesis.getAuthors().add(person);

        bookService.insert(book);
        phdThesisService.insert(phdThesis);
        personService.insert(person);
        tm.commitTransaction();

        tm.beginTransaction();
        List<Publication> publications = publicationService.findByAuthorIdOrderedByYear(PERSON_ID);
        assertNotNull("Publications should not be null", publications);
        assertEquals("Invalid size", 2, publications.size());
        assertEquals("Order not correct", PHD_THESIS_ID, publications.get(0).getId());
        assertEquals("Order not correct", BOOK_ID, publications.get(1).getId());

        tm.commitTransaction();
    }

    @Test
    public void test_7_retrievePublicationsByEditor() {
        //test data
        String TEST_ID = "7";
        String BOOK_ID = "test-book-" + TEST_ID;
        String BOOK_TITLE = "Test Book " + TEST_ID;
        Integer BOOK_YEAR = 2001;
        String PHD_THESIS_ID = "test-phdthesis-1" + TEST_ID;
        String PHD_THESIS_TITLE = "Test PHD Thesis 1" + TEST_ID;
        Integer PHD_THESIS_YEAR =  2000;
        String PERSON_ID = "test-person-" + TEST_ID;
        String PERSON_NAME = "Test Person " + TEST_ID;
        tm.beginTransaction();
        Book book = bookService.createEntity();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setYear(BOOK_YEAR);

        PhdThesis phdThesis = phdThesisService.createEntity();
        phdThesis.setId(PHD_THESIS_ID);
        phdThesis.setTitle(PHD_THESIS_TITLE);
        phdThesis.setYear(PHD_THESIS_YEAR);

        Person person = personService.createEntity();
        person.setId(PERSON_ID);
        person.setName(PERSON_NAME);
        person.getEditedPublications().add(book);
        person.getEditedPublications().add(phdThesis);
        book.getEditors().add(person);
        phdThesis.getEditors().add(person);

        bookService.insert(book);
        phdThesisService.insert(phdThesis);
        personService.insert(person);
        tm.commitTransaction();

        tm.beginTransaction();
        List<Publication> publications = publicationService.findByEditorIdOrderedByYear(PERSON_ID);
        assertNotNull("Publications should not be null", publications);
        assertEquals("Invalid size", 2, publications.size());
        assertEquals("Order not correct", PHD_THESIS_ID, publications.get(0).getId());
        assertEquals("Order not correct", BOOK_ID, publications.get(1).getId());

        tm.commitTransaction();
    }

    @Test
    public void test_8_retrievePublicationsBySeries() {
        String TEST_ID = "8";
        String PROCEEDINGS_ID = "test-proce-" + TEST_ID;
        String PROCEEDINGS_TITLE = "Test Proceedings " + TEST_ID;
        Integer PROCEEDINGS_YEAR = 2005;
        String BOOK_ID = "test-book-" + TEST_ID;
        String BOOK_TITLE = "Test Book " + TEST_ID;
        Integer BOOK_YEAR = 2000;
        String PERSON_ID = "test-person-" + TEST_ID;
        String PERSON_NAME = "Test Person " + TEST_ID;
        String SERIES_ID = "test-series-" + TEST_ID;
        String SERIES_NAME = "Test Series " + TEST_ID;

        //Insert objects
        tm.beginTransaction();
        Proceedings proceedings = proceedingsService.createEntity();
        proceedings.setId(PROCEEDINGS_ID);
        proceedings.setTitle(PROCEEDINGS_TITLE);
        proceedings.setYear(PROCEEDINGS_YEAR);

        Book book = bookService.createEntity();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setYear(BOOK_YEAR);

        Person person = personService.createEntity();
        person.setId(PERSON_ID);
        person.setName(PERSON_NAME);
        person.getEditedPublications().add(proceedings);
        proceedings.getEditors().add(person);
        person.getAuthoredPublications().add(book);
        book.getAuthors().add(person);

        Series series = seriesService.createEntity();
        series.setId(SERIES_ID);
        series.setName(SERIES_NAME);
        series.getPublications().add(book);
        book.setSeries(series);
        series.getPublications().add(proceedings);
        proceedings.setSeries(series);

        personService.insert(person);
        bookService.insert(book);
        proceedingsService.insert(proceedings);
        seriesService.insert(series);
        tm.commitTransaction();

        tm.beginTransaction();
        List<Publication> results = publicationService.findBySeriesOrderedByYear(SERIES_ID);
        assertNotNull("Results should not be null", results);
        assertEquals("Invalid size", 2, results.size());
        assertEquals("Invalid year", BOOK_YEAR, results.get(0).getYear());
        assertEquals("Invalid year", PROCEEDINGS_YEAR, results.get(1).getYear());

        tm.commitTransaction();
    }

    @Test
    public void test_9_retrievePublicationsBySchool() {
        String TEST_ID = "9";
        String PHD_THESIS_ID = "test-phdthesis-" + TEST_ID;
        String PHD_THESIS_TITLE = "Test PHD Thesis " + TEST_ID;
        Integer PHD_THESIS_YEAR = 2005;
        String MASTER_THESIS_ID = "test-master-" + TEST_ID;
        String MASTER_THESIS_TITLE = "Test Master " + TEST_ID;
        Integer MASTER_THESIS_YEAR = 2000;
        String PERSON_ID = "test-person-" + TEST_ID;
        String PERSON_NAME = "Test Person " + TEST_ID;
        String SCHOOL_ID = "test-school-" + TEST_ID;
        String SCHOOL_NAME = "Test School " + TEST_ID;

        //Insert objects
        tm.beginTransaction();
        PhdThesis phdThesis = phdThesisService.createEntity();
        phdThesis.setId(PHD_THESIS_ID);
        phdThesis.setTitle(PHD_THESIS_TITLE);
        phdThesis.setYear(PHD_THESIS_YEAR);

        MasterThesis masterThesis = masterThesisService.createEntity();
        masterThesis.setId(MASTER_THESIS_ID);
        masterThesis.setTitle(MASTER_THESIS_TITLE);
        masterThesis.setYear(MASTER_THESIS_YEAR);

        Person person = personService.createEntity();
        person.setId(PERSON_ID);
        person.setName(PERSON_NAME);
        person.getAuthoredPublications().add(masterThesis);
        masterThesis.getAuthors().add(person);

        person.getAuthoredPublications().add(phdThesis);
        phdThesis.getAuthors().add(person);

        School school = schoolService.createEntity();
        school.setId(SCHOOL_ID);
        school.setName(SCHOOL_NAME);
        school.getPublications().add(masterThesis);
        masterThesis.setSchool(school);
        school.getPublications().add(phdThesis);
        phdThesis.setSchool(school);

        personService.insert(person);
        schoolService.insert(school);
        masterThesisService.insert(masterThesis);
        phdThesisService.insert(phdThesis);

        tm.commitTransaction();

        tm.beginTransaction();
        List<Publication> results = publicationService.findBySchoolOrderedByYear(SCHOOL_ID);
        assertNotNull("Results should not be null", results);
        assertEquals("Invalid size", 2, results.size());
        assertEquals("Invalid year", MASTER_THESIS_YEAR, results.get(0).getYear());
        assertEquals("Invalid year", PHD_THESIS_YEAR, results.get(1).getYear());
        tm.commitTransaction();
    }

    @Test
    public void test_10_retrievePublicationsByPublisher() {
        //test data
        String TEST_ID = "10";
        String BOOK_ID = "test-book-" + TEST_ID;
        String BOOK_TITLE = "Test Book " + TEST_ID;
        Integer BOOK_YEAR = 2005;
        String PHD_THESIS_ID = "test-phdthesis-1" + TEST_ID;
        String PHD_THESIS_TITLE = "Test PHD Thesis 1" + TEST_ID;
        Integer PHD_THESIS_YEAR = 1999;
        String PERSON_ID = "test-person-" + TEST_ID;
        String PERSON_NAME = "Test Person " + TEST_ID;
        String PUBLISHER_ID = "test-publisher-" + TEST_ID;
        String PUBLISHER_NAME = "Test Publisher " + TEST_ID;

        //insert objects
        tm.beginTransaction();
        Book book = bookService.createEntity();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setYear(BOOK_YEAR);

        PhdThesis phdThesis = phdThesisService.createEntity();
        phdThesis.setId(PHD_THESIS_ID);
        phdThesis.setTitle(PHD_THESIS_TITLE);
        phdThesis.setYear(PHD_THESIS_YEAR);

        Person person = personService.createEntity();
        person.setId(PERSON_ID);
        person.setName(PERSON_NAME);
        person.getAuthoredPublications().add(book);
        book.getAuthors().add(person);

        person.getAuthoredPublications().add(phdThesis);
        phdThesis.getAuthors().add(person);

        Publisher publisher = publisherService.createEntity();
        publisher.setId(PUBLISHER_ID);
        publisher.setName(PUBLISHER_NAME);

        book.setPublisher(publisher);
        publisher.getPublications().add(book);

        phdThesis.setPublisher(publisher);
        publisher.getPublications().add(phdThesis);

        personService.insert(person);
        publisherService.insert(publisher);
        bookService.insert(book);
        phdThesisService.insert(phdThesis);

        tm.commitTransaction();

        tm.beginTransaction();
        List<Publication> results = publicationService.findByPublisherOrderedByYear(PUBLISHER_ID);
        assertNotNull("Results should not be null", results);
        assertEquals("Invalid size", 2, results.size());
        assertEquals("Invalid year", PHD_THESIS_YEAR, results.get(0).getYear());
        assertEquals("Invalid year", BOOK_YEAR, results.get(1).getYear());
        tm.commitTransaction();
    }

    @Test
    public void test_11_retrieveArticlesByJournalEdition() {
        String TEST_ID = "11";
        String ARTICLE_ID_1 = "test-article-1" + TEST_ID;
        String ARTICLE_TITLE_1 = "Test Article 1 " + TEST_ID;
        Integer ARTICLE_YEAR_1 = 2002;
        String ARTICLE_ID_2 = "test-article-2" + TEST_ID;
        String ARTICLE_TITLE_2 = "Test Article 2 " + TEST_ID;
        Integer ARTICLE_YEAR_2 = 2003;
        String JE_ID = "test-journal-edition-" + TEST_ID;
        String JE_NUMBER = TEST_ID;
        String JE_VOLUME = TEST_ID;
        Integer JE_YEAR = 2003;
        String PERSON_ID = "test-person-" + TEST_ID;
        String PERSON_NAME = "Test Person " + TEST_ID;
        tm.beginTransaction();

        Article article1 = newArticle(ARTICLE_ID_1, ARTICLE_TITLE_1, ARTICLE_YEAR_1);
        Article article2 = newArticle(ARTICLE_ID_2, ARTICLE_TITLE_2, ARTICLE_YEAR_2);
        Person person = newPerson(PERSON_ID, PERSON_NAME);
        authoredBy(person, article1, article2);
        JournalEdition edition = newJournalEdition(JE_ID, JE_NUMBER, JE_VOLUME, JE_YEAR);
        inJournalEdition(edition, article1, article2);

        articleService.insert(article1);
        articleService.insert(article2);
        journalEditionService.insert(edition);
        personService.insert(person);

        tm.commitTransaction();

        tm.beginTransaction();
        List<Article> results = articleService.findByJournalEditionOrderedByYear(JE_ID);
        assertNotNull("Results should not be null", results);
        assertEquals("Invalid size", 2, results.size());
        assertEquals("Invalid year", ARTICLE_YEAR_1, results.get(0).getYear());
        assertEquals("Invalid year", ARTICLE_YEAR_2, results.get(1).getYear());
        tm.commitTransaction();
    }

    @Test
    public void test_12_retrieveInCollectionsByBook() {
        String TEST_ID = "12";
        String BOOK_ID = "test-book-" + TEST_ID;
        String BOOK_TITLE = "Test Book " + TEST_ID;
        Integer BOOK_YEAR = 2005;
        String INCOLLECTION_ID_1 = "test-incollection-1" + TEST_ID;
        String INCOLLECTION_TITLE_1 = "Test InCollection 1 " + TEST_ID;
        Integer INCOLLECTION_YEAR_1 = 2002;
        String INCOLLECTION_ID_2 = "test-incollection-2" + TEST_ID;
        String INCOLLECTION_TITLE_2 = "Test InCollection 2 " + TEST_ID;
        Integer INCOLLECTION_YEAR_2 = 2003;
        String PERSON_ID = "test-person-" + TEST_ID;
        String PERSON_NAME = "Test Person " + TEST_ID;

        //insert objects
        tm.beginTransaction();
        Book book = bookService.createEntity();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setYear(BOOK_YEAR);

        InCollection inCollection1 = inCollectionService.createEntity();
        inCollection1.setId(INCOLLECTION_ID_1);
        inCollection1.setTitle(INCOLLECTION_TITLE_1);
        inCollection1.setYear(INCOLLECTION_YEAR_1);
        book.getPublications().add(inCollection1);
        inCollection1.setParentPublication(book);

        InCollection inCollection2 = inCollectionService.createEntity();
        inCollection2.setId(INCOLLECTION_ID_2);
        inCollection2.setTitle(INCOLLECTION_TITLE_2);
        inCollection2.setYear(INCOLLECTION_YEAR_2);
        book.getPublications().add(inCollection2);
        inCollection2.setParentPublication(book);

        book.getPublications().add(inCollection1);
        inCollection1.setParentPublication(book);
        Person person = personService.createEntity();
        person.setId(PERSON_ID);
        person.setName(PERSON_NAME);
        person.getAuthoredPublications().add(book);
        book.getAuthors().add(person);

        person.getAuthoredPublications().add(inCollection1);
        inCollection1.getAuthors().add(person);
        person.getAuthoredPublications().add(inCollection2);
        inCollection2.getAuthors().add(person);

        personService.insert(person);
        bookService.insert(book);
        inCollectionService.insert(inCollection1);
        inCollectionService.insert(inCollection2);
        tm.commitTransaction();

        tm.beginTransaction();
        List<InCollection> results = inCollectionService.findByBookIdOrderByYear(BOOK_ID);
        assertNotNull("Results should not be null", results);
        assertEquals("Invalid size", 2, results.size());
        assertEquals("Invalid year", INCOLLECTION_YEAR_1, results.get(0).getYear());
        assertEquals("Invalid year", INCOLLECTION_YEAR_2, results.get(1).getYear());

        tm.commitTransaction();
    }

    @Test
    public void test_13_retrieveInProceedingsByProceedings() {
        String TEST_ID = "13";
        String PROCEEDINGS_ID = "test-proc-" + TEST_ID;
        String PROCEEDINGS_TITLE = "Test Proceedings " + TEST_ID;

        String PERSON_ID = "test-person-" + TEST_ID;
        String PERSON_NAME = "Test Person " + TEST_ID;

        String INPROCEEDINGS_ID_1 = "test-inproceedings-1-" + TEST_ID;
        String INPROCEEDINGS_TITLE_1 = "Test InProceedings 1 " + TEST_ID;
        Integer INPROCEEDINGS_YEAR_1 = 2003;

        String INPROCEEDINGS_ID_2 = "test-inproceedings-2" + TEST_ID;
        String INPROCEEDINGS_TITLE_2 = "Test InProceedings 1 " + TEST_ID;
        Integer INPROCEEDINGS_YEAR_2 = 2002;

        String CONFERENCE_ID = "test-conference-" + TEST_ID;
        String CONFERENCE_NAME = "Test Conference " + TEST_ID;

        String CONF_ED_ID = "test-confed-" + TEST_ID;

        tm.beginTransaction();

        //Create a dummy proceedings
        Proceedings proceedings = proceedingsService.createEntity();
        proceedings.setId(PROCEEDINGS_ID);
        proceedings.setTitle(PROCEEDINGS_TITLE);
        proceedings.setYear(2001);

        //Create an editor
        Person person = personService.createEntity();
        person.setId(PERSON_ID);
        person.setName(PERSON_NAME);
        person.getEditedPublications().add(proceedings);
        proceedings.getEditors().add(person);

        //Create some inProceedings objects
        InProceedings inProceedings1 = inProceedingsService.createEntity();
        inProceedings1.setId(INPROCEEDINGS_ID_1);
        inProceedings1.setTitle(INPROCEEDINGS_TITLE_1);
        inProceedings1.setYear(INPROCEEDINGS_YEAR_1);
        inProceedings1.getAuthors().add(person);
        person.getAuthoredPublications().add(inProceedings1);
        inProceedings1.setProceedings(proceedings);
        proceedings.getPublications().add(inProceedings1);

        InProceedings inProceedings2 = inProceedingsService.createEntity();
        inProceedings2.setId(INPROCEEDINGS_ID_2);
        inProceedings2.setTitle(INPROCEEDINGS_TITLE_2);
        inProceedings2.setYear(INPROCEEDINGS_YEAR_2);
        inProceedings2.getAuthors().add(person);
        person.getAuthoredPublications().add(inProceedings2);
        inProceedings2.setProceedings(proceedings);
        proceedings.getPublications().add(inProceedings2);

        //Create newConference and newConference edition
        Conference conference = conferenceService.createEntity();
        conference.setId(CONFERENCE_ID);
        conference.setName(CONFERENCE_NAME);

        ConferenceEdition conferenceEdition = conferenceEditionService.createEntity();
        conferenceEdition.setId(CONF_ED_ID);
        conferenceEdition.setConference(conference);
        conference.getEditions().add(conferenceEdition);

        conferenceEdition.setProceedings(proceedings);
        proceedings.setConferenceEdition(conferenceEdition);

        //now save all the entities
        inProceedingsService.insert(inProceedings1);
        inProceedingsService.insert(inProceedings2);
        proceedingsService.insert(proceedings);
        personService.insert(person);
        conferenceService.insert(conference);
        conferenceEditionService.insert(conferenceEdition);

        tm.commitTransaction();

        tm.beginTransaction();
        List<InProceedings> results = inProceedingsService.findByProceedingsIdOrderByYear(PROCEEDINGS_ID);
        assertNotNull("Results should not be null", results);
        assertEquals("Invalid size", 2, results.size());
        assertEquals("Invalid year", INPROCEEDINGS_YEAR_2, results.get(0).getYear());
        assertEquals("Invalid year", INPROCEEDINGS_YEAR_1, results.get(1).getYear());
        tm.commitTransaction();
    }

    @Test
    public void test_14_retrieveJournalEditionsByJournal() {
        String TEST_ID = "14";
        String JE_ID_1 = "test-je-1-" + TEST_ID;
        String JE_VOLUME_1 = "test-je-vol-1-" + TEST_ID;
        String JE_NUMBER_1 = TEST_ID;
        Integer JE_YEAR_1 = 2005;

        String JE_ID_2 = "test-je-2-" + TEST_ID;
        String JE_VOLUME_2 = "test-je-vol-2-" + TEST_ID;
        String JE_NUMBER_2 = TEST_ID;
        Integer JE_YEAR_2 = 2003;

        String JE_ID_3 = "test-je-3-" + TEST_ID;
        String JE_VOLUME_3 = "test-je-vol-3-" + TEST_ID;
        String JE_NUMBER_3 = TEST_ID;
        Integer JE_YEAR_3 = 2004;

        String JOURNAL_ID = "test-journal-" + TEST_ID;
        String JOURNAL_NAME = "Test Journal " + TEST_ID;

        tm.beginTransaction();
        JournalEdition journalEdition1 = newJournalEdition(JE_ID_1, JE_NUMBER_1, JE_VOLUME_1, JE_YEAR_1);
        JournalEdition journalEdition2 = newJournalEdition(JE_ID_2, JE_NUMBER_2, JE_VOLUME_2, JE_YEAR_2);
        JournalEdition journalEdition3 = newJournalEdition(JE_ID_3, JE_NUMBER_3, JE_VOLUME_3, JE_YEAR_3);
        
        Journal journal = newJournal(JOURNAL_ID, JOURNAL_NAME, journalEdition1, journalEdition2, journalEdition3);
        journalService.insert((Iterable)Arrays.asList(journalEdition1, journalEdition2, journalEdition3));
        journalService.insert(journal);
        tm.commitTransaction();

        tm.beginTransaction();
        List<JournalEdition> results = journalEditionService.findByJournalOrdered(JOURNAL_ID);
        assertNotNull("Results should not be null", results);
        assertEquals("Invalid size", 3, results.size());
        assertEquals("Invalid year", JE_YEAR_2, results.get(0).getYear());
        assertEquals("Invalid year", JE_YEAR_3, results.get(1).getYear());
        assertEquals("Invalid year", JE_YEAR_1, results.get(2).getYear());
        tm.commitTransaction();
    }

    @Test
    public void test_15_retrieveConferenceEditionsByConference() {
        String TEST_ID = "15";
        String CONF_ID = "test-conf-" + TEST_ID;
        String CONF_NAME = "Test Conf " + TEST_ID;
        String CONF_ED_ID_1 = "test-confed-1-" +TEST_ID;
        Integer CONF_ED_YEAR_1 = 2009;
        String CONF_ED_ID_2 = "test-confed-2-" +TEST_ID;
        Integer CONF_ED_YEAR_2 = 2010;
        String CONF_ED_ID_3 = "test-confed-3-" +TEST_ID;
        Integer CONF_ED_YEAR_3 = 2007;
        tm.beginTransaction();

        ConferenceEdition conferenceEdition1 = newConferenceEdition(CONF_ED_ID_1, CONF_ED_YEAR_1, null);
        ConferenceEdition conferenceEdition2 = newConferenceEdition(CONF_ED_ID_2, CONF_ED_YEAR_2, null);
        ConferenceEdition conferenceEdition3 = newConferenceEdition(CONF_ED_ID_3, CONF_ED_YEAR_3, null);

        Conference conference = newConference(CONF_ID, CONF_NAME, conferenceEdition1, conferenceEdition2, conferenceEdition3);
        conferenceEditionService.insert((Iterable)Arrays.asList(conferenceEdition1, conferenceEdition2, conferenceEdition3));
        conferenceService.insert(conference);
        tm.commitTransaction();

        tm.beginTransaction();
        List<ConferenceEdition> results = conferenceEditionService.findByConferenceOrderedByYear(CONF_ID);
        assertNotNull("Results should not be null", results);
        assertEquals("Invalid size", 3, results.size());
        assertEquals("Invalid year", CONF_ED_YEAR_3, results.get(0).getYear());
        assertEquals("Invalid year", CONF_ED_YEAR_1, results.get(1).getYear());
        assertEquals("Invalid year", CONF_ED_YEAR_2, results.get(2).getYear());
        tm.commitTransaction();
    }

    public void setProceedingsService(ProceedingsService proceedingsService) {
        this.proceedingsService = proceedingsService;
    }

    public void setInProceedingsService(InProceedingsService inProceedingsService) {
        this.inProceedingsService = inProceedingsService;
    }

    public void setConferenceEditionService(ConferenceEditionService conferenceEditionService) {
        this.conferenceEditionService = conferenceEditionService;
    }

    public void setConferenceService(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public void setPublisherService(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    public void setSeriesService(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    public void setPhdThesisService(PhdThesisService phdThesisService) {
        this.phdThesisService = phdThesisService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public void setSchoolService(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public void setJournalService(JournalService journalService) {
        this.journalService = journalService;
    }

    public void setJournalEditionService(JournalEditionService journalEditionService) {
        this.journalEditionService = journalEditionService;
    }

    public void setMasterThesisService(MasterThesisService masterThesisService) {
        this.masterThesisService = masterThesisService;
    }

    public void setInCollectionService(InCollectionService inCollectionService) {
        this.inCollectionService = inCollectionService;
    }

    public void setPublicationService(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    public void setTm(TransactionManager tm) {
        this.tm = tm;
    }

    private ConferenceEdition newConferenceEdition(String id, Integer year, Proceedings proceedings) {
        ConferenceEdition edition = conferenceEditionService.createEntity();
        edition.setId(id);
        edition.setYear(year);
        if (proceedings != null) {
            proceedings.setConferenceEdition(edition);
            edition.setProceedings(proceedings);
        }
        return edition;
    }

    private Conference newConference(String id, String name, ConferenceEdition... editions) {
        Conference conference = conferenceService.createEntity();
        conference.setId(id);
        conference.setName(name);
        for (ConferenceEdition edition : editions) {
            conference.getEditions().add(edition);
            edition.setConference(conference);
        }
        return conference;
    }

    private JournalEdition newJournalEdition(String id, String number, String volume, Integer year) {
        JournalEdition journalEdition = journalEditionService.createEntity();
        journalEdition.setId(id);
        journalEdition.setNumber(number);
        journalEdition.setVolume(volume);
        journalEdition.setYear(year);

        return journalEdition;
    }

    private Journal newJournal(String id, String name, JournalEdition... editions) {
        Journal journal = journalService.createEntity();
        journal.setId(id);
        journal.setName(name);
        for (JournalEdition edition : editions) {
            journal.addEdition(edition);
            edition.setJournal(journal);
        }
        return journal;
    }

    private void inJournalEdition(JournalEdition edition, Article... articles) {
        for (Article article : articles) {
            article.setJournalEdition(edition);
            edition.addArticle(article);
        }
    }

    private Article newArticle(String id, String title, Integer year) {
        Article article = articleService.createEntity();
        return newPublication(article, id, title, year);
    }

    private <T extends Publication> T newPublication(T publication, String id, String title, Integer year) {
        publication.setId(id);
        publication.setTitle(title);
        publication.setYear(year);
        return publication;
    }

    private void authoredBy(Person person, Publication... publications) {
        for (Publication publication : publications) {
            publication.getAuthors().add(person);
            person.getAuthoredPublications().add(publication);
        }
    }

    private void editedBy(Person person, Publication... publications) {
        for (Publication publication : publications) {
            publication.getAuthors().add(person);
            person.getAuthoredPublications().add(publication);
        }
    }

    private Person newPerson(String id, String name) {
        Person person = personService.createEntity();
        person.setId(id);
        person.setName(name);
        return person;
    }
}