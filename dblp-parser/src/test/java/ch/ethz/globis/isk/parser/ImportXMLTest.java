package ch.ethz.globis.isk.parser;

import ch.ethz.globis.isk.domain.*;
import ch.ethz.globis.isk.parser.config.ParserTestConfig;
import ch.ethz.globis.isk.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = {ParserTestConfig.class})
@ActiveProfiles(profiles = { "test" })
public class ImportXMLTest {

    @Autowired private PublicationService publicationService;
    @Autowired private BookService bookService;
    @Autowired private InCollectionService inCollectionService;
    @Autowired private PersonService personService;
    @Autowired private PublisherService publisherService;
    @Autowired private SeriesService seriesService;
    @Autowired private ConferenceService conferenceService;
    @Autowired private ConferenceEditionService conferenceEditionService;
    @Autowired private ProceedingsService proceedingsService;
    @Autowired private InProceedingsService inProceedingsService;
    @Autowired private ArticleService articleService;
    @Autowired private JournalService journalService;
    @Autowired private JournalEditionService journalEditionService;
    @Autowired private PhdThesisService phdThesisService;
    @Autowired private MasterThesisService masterThesisService;

    String ID = null;
    String TITLE = null;
    String VOLUME = null;
    String NUMBER = null;
    Integer YEAR = null;
    String PUBLISHER = null;
    String SERIES = null;
    String[] PERSONS = null;
    String PAGES = null;
    String ISBN = null;
    String NOTE = null;
    String EE = null;
    String JOURNAL = null;
    String SCHOOL = null;

    @Autowired @Qualifier("non-xml-parser")
    DBLPParser parser;

    @Test
    public void importTestXML() {
        String testFile = "dblp_test.xml";
        parser.process(testFile);

        checkImportedPublications();
        checkImportedArticles();
        checkImportedProceedings();
        checkImportedBooks();
        checkImportedTheses();
    }

    public void checkImportedPublications() {
        long publicationsImported = publicationService.count();
        assertEquals("Wrong number of total publications imported.", 15, publicationsImported);
    }

    public void checkImportedTheses() {
        long masterThesisImport = masterThesisService.count();
        assertEquals("Wrong number of phd theses imported.", 1, masterThesisImport);
        ID = "phd-VanRoy84";
        TITLE = "A Prolog Compiler for the PLM.";
        YEAR = 1984;
        SCHOOL = "University of California at Berkeley";
        MasterThesis masterThesis = masterThesisService.findOne(ID);
        assertEquals(ID, masterThesis.getId());
        assertEquals(TITLE, masterThesis.getTitle());
        assertEquals(YEAR, masterThesis.getYear());
        assertNotNull(masterThesis.getSchool());
        assertEquals(SCHOOL, masterThesis.getSchool().getName());

        long phdThesesImported = phdThesisService.count();
        assertEquals("Wrong number of phd theses imported.", 1, phdThesesImported);

        ID = "phd-Ganguly92";
        TITLE = "Parallel Evaluation of Deductive Database Queries.";
        YEAR = 1992;
        SCHOOL = "University of Texas, Austin";
        PhdThesis phdThesis = phdThesisService.findOne(ID);
        assertEquals(ID, phdThesis.getId());
        assertEquals(TITLE, phdThesis.getTitle());
        assertEquals(YEAR, phdThesis.getYear());
        assertNotNull(phdThesis.getSchool());
        assertEquals(SCHOOL, phdThesis.getSchool().getName());
    }

    public void checkImportedProceedings() {
        long proceedingsImported = proceedingsService.count();
        assertEquals("Wrong number of proceedings imported.", 1, proceedingsImported);

        ID = "conf-bncod-1992";
        TITLE = "Advanced Database Systems, 10th British National Conference on Databases, BNCOD 10, Aberdeen, Scotland, July 6-8, 1992, Proceedings";
        VOLUME = "618";
        YEAR = 1992;
        ISBN = "3-540-55693-1";
        PUBLISHER = "Springer";
        SERIES = "Lecture Notes in Computer Science";
        PERSONS = new String[] {"Peter M. D. Gray", "Robert J. Lucas"};

        Proceedings proceedings = proceedingsService.findOne(ID);
        assertNotNull(proceedings);
        assertEquals(ID, proceedings.getId());
        assertEquals(TITLE, proceedings.getTitle());
        assertEquals(YEAR, proceedings.getYear());
        assertEquals(VOLUME, proceedings.getVolume());
        assertEquals(ISBN, proceedings.getIsbn());
        assertNotNull(proceedings.getSeries());
        assertEquals(SERIES, proceedings.getSeries().getName());
        assertNotNull(proceedings.getPublisher());
        assertEquals(PUBLISHER, proceedings.getPublisher().getName());
        assertEquals(2, proceedings.getEditors().size());
        assertTrue(checkPersons(proceedings.getEditors(), PERSONS));
        assertEquals(3, proceedings.getPublications().size());

        ID = "conf-bncod-SaakeJ92";
        PERSONS = new String[] {"Gunter Saake", "Ralf Jungclaus"};
        TITLE = "Views and Formal Implementation in a Three-Level " +
                "Schema Architecture for Dynamic Objects.";
        PAGES = "78-95";
        YEAR = 1992;
        EE = "http://dx.doi.org/10.1007/3-540-55693-1_33";
        InProceedings inProceedings = inProceedingsService.findOne(ID);
        assertEquals(ID, inProceedings.getId());
        assertEquals(TITLE, inProceedings.getTitle());
        assertEquals(PAGES, inProceedings.getPages());
        assertEquals(YEAR, inProceedings.getYear());
        assertEquals(EE, inProceedings.getElectronicEdition());
        assertEquals(proceedings, inProceedings.getProceedings());
        assertTrue(checkPersons(inProceedings.getAuthors(), PERSONS));

        ID = "conf-bncod-DiazE92";
        PERSONS = new String[] {"Oscar Diaz", "Suzanne M. Embury"};
        TITLE = "Generating Active Rules from High-Level Specifications.";
        PAGES = "227-243";
        YEAR = 1992;
        EE = "http://dx.doi.org/10.1007/3-540-55693-1_41";
        inProceedings = inProceedingsService.findOne(ID);
        assertEquals(ID, inProceedings.getId());
        assertEquals(TITLE, inProceedings.getTitle());
        assertEquals(PAGES, inProceedings.getPages());
        assertEquals(YEAR, inProceedings.getYear());
        assertEquals(EE, inProceedings.getElectronicEdition());
        assertEquals(proceedings, inProceedings.getProceedings());
        assertTrue(checkPersons(inProceedings.getAuthors(), PERSONS));

        ID = "conf-bncod-QutaishatFG92";
        PERSONS = new String[] {"M. A. Qutaishat", "N. J. Fiddian", "W. A. Gray"};
        TITLE = "Association Merging in a Schema Meta-Integration System for a " +
                "Heterogeneous Object-Oriented Database Environment.";
        PAGES = "209-226";
        YEAR = 1992;
        EE = "http://dx.doi.org/10.1007/3-540-55693-1_40";
        inProceedings = inProceedingsService.findOne(ID);
        assertEquals(ID, inProceedings.getId());
        assertEquals(TITLE, inProceedings.getTitle());
        assertEquals(PAGES, inProceedings.getPages());
        assertEquals(YEAR, inProceedings.getYear());
        assertEquals(EE, inProceedings.getElectronicEdition());
        assertEquals(proceedings, inProceedings.getProceedings());
        assertTrue(checkPersons(inProceedings.getAuthors(), PERSONS));
    }

    public void checkImportedArticles() {
        long articlesImported = articleService.count();
        assertEquals("Wrong number of articles imported.", 4, articlesImported);

        ID = "journals-alife-LindgrenN94";
        TITLE = "Cooperation and Community Structure in Artificial Ecosystems.";
        PAGES = "15-38";
        YEAR = 1994;
        VOLUME = "1";
        NUMBER = "1-2";
        JOURNAL = "Artificial Life";
        EE = "http://dx.doi.org/10.1162/artl.1993.1.1_2.15";
        PERSONS = new String[] { "Kristian Lindgren", "Mats G. Nordahl"};

        Article article = articleService.findOne(ID);
        assertNotNull(article);
        assertEquals(ID, article.getId());
        assertEquals(TITLE, article.getTitle());
        assertEquals(PAGES, article.getPages());
        assertEquals(YEAR, article.getYear());
        assertNotNull(article.getJournalEdition());
        assertEquals(VOLUME, article.getJournalEdition().getVolume());
        assertEquals(NUMBER, article.getJournalEdition().getNumber());
        assertEquals(YEAR, article.getJournalEdition().getYear());
        assertEquals(EE, article.getElectronicEdition());
        Journal journal = article.getJournalEdition().getJournal();
        assertNotNull(journal);
        assertEquals(JOURNAL, journal.getName());
        assertEquals(2, journal.getEditions().size());
        assertEquals(2, article.getJournalEdition().getPublications().size());
        assertTrue(checkPersons(article.getAuthors(), PERSONS));

        ID = "journals-alife-Steels94";
        TITLE = "The Artificial Life Roots of Artificial Intelligence.";
        PAGES = "76-110";
        YEAR = 1994;
        VOLUME = "1";
        NUMBER = "1-2";
        JOURNAL = "Artificial Life";
        EE = "http://dx.doi.org/10.1162/artl.1993.1.1_2.76";
        PERSONS = new String[] { "Luc Steels" };

        article = articleService.findOne(ID);
        assertNotNull(article);
        assertEquals(ID, article.getId());
        assertEquals(TITLE, article.getTitle());
        assertEquals(PAGES, article.getPages());
        assertEquals(YEAR, article.getYear());
        assertNotNull(article.getJournalEdition());
        assertEquals(VOLUME, article.getJournalEdition().getVolume());
        assertEquals(NUMBER, article.getJournalEdition().getNumber());
        assertEquals(YEAR, article.getJournalEdition().getYear());
        assertEquals(EE, article.getElectronicEdition());
        journal = article.getJournalEdition().getJournal();
        assertNotNull(journal);
        assertEquals(JOURNAL, journal.getName());
        assertEquals(2, journal.getEditions().size());
        assertEquals(2, article.getJournalEdition().getPublications().size());
        assertTrue(checkPersons(article.getAuthors(), PERSONS));

        ID = "journals-alife-HosokawaSM94";
        TITLE = "Dynamics of Self-Assembling Systems: Analogy with Chemical Kinetics.";
        PAGES = "413-427";
        YEAR = 1994;
        VOLUME = "1";
        NUMBER = "4";
        JOURNAL = "Artificial Life";
        EE = "http://dx.doi.org/10.1162/artl.1994.1.4.413";
        PERSONS = new String[] { "Kazuo Hosokawa", "Isao Shimoyama", "Hirofumi Miura" };

        article = articleService.findOne(ID);
        assertNotNull(article);
        assertEquals(ID, article.getId());
        assertEquals(TITLE, article.getTitle());
        assertEquals(PAGES, article.getPages());
        assertEquals(YEAR, article.getYear());
        assertNotNull(article.getJournalEdition());
        assertEquals(VOLUME, article.getJournalEdition().getVolume());
        assertEquals(NUMBER, article.getJournalEdition().getNumber());
        assertEquals(YEAR, article.getJournalEdition().getYear());
        assertEquals(EE, article.getElectronicEdition());
        journal = article.getJournalEdition().getJournal();
        assertNotNull(journal);
        assertEquals(JOURNAL, journal.getName());
        assertEquals(2, journal.getEditions().size());
        assertEquals(2, article.getJournalEdition().getPublications().size());
        assertTrue(checkPersons(article.getAuthors(), PERSONS));

        ID = "journals-alife-OReilly94";
        TITLE = "Book Review: Genetic Programming II - " +
                "Automatic Discovery of Reusable Programs by John R Koza.";
        PAGES = "439-441";
        YEAR = 1994;
        VOLUME = "1";
        NUMBER = "4";
        JOURNAL = "Artificial Life";
        EE = "http://dx.doi.org/10.1162/artl.1994.1.4.439";
        PERSONS = new String[] { "Una-May O'Reilly" };

        article = articleService.findOne(ID);
        assertNotNull(article);
        assertEquals(ID, article.getId());
        assertEquals(TITLE, article.getTitle());
        assertEquals(PAGES, article.getPages());
        assertEquals(YEAR, article.getYear());
        assertNotNull(article.getJournalEdition());
        assertEquals(VOLUME, article.getJournalEdition().getVolume());
        assertEquals(NUMBER, article.getJournalEdition().getNumber());
        assertEquals(YEAR, article.getJournalEdition().getYear());
        assertEquals(EE, article.getElectronicEdition());
        journal = article.getJournalEdition().getJournal();
        assertNotNull(journal);
        assertEquals(JOURNAL, journal.getName());
        assertEquals(2, journal.getEditions().size());
        assertEquals(2, article.getJournalEdition().getPublications().size());
        assertTrue(checkPersons(article.getAuthors(), PERSONS));

    }

    public void checkImportedBooks() {
        long booksImported = bookService.count();
        assertEquals("Wrong number of books imported.", 1, booksImported);

        long inCollectionsImported = inCollectionService.count();
        assertEquals("Wrong number of inCollections imported.", 4, inCollectionsImported);

        ID = "books-sp-Atzeni93";
        PERSONS = new String[] { "Paolo Atzeni" };
        TITLE = "LOGIDATA+: Deductive Databases with Complex Objects";
        SERIES = "Lecture Notes in Computer Science";
        VOLUME = "701";
        PUBLISHER = "Springer";
        YEAR = 1993;
        ISBN = "3-540-56974-X";
        EE = "http://dx.doi.org/10.1007/BFb0021884";
        Book book = bookService.findOne(ID);
        assertNotNull(book);
        assertEquals(ID, book.getId());
        assertEquals(TITLE, book.getTitle());
        assertEquals(VOLUME, book.getVolume());
        assertEquals(YEAR, book.getYear());
        assertEquals(EE, book.getElectronicEdition());
        assertEquals(ISBN, book.getIsbn());
        assertTrue(checkPersons(book.getEditors(), PERSONS));
        assertEquals(0, book.getAuthors().size());
        assertNotNull(book.getSeries());
        assertEquals(SERIES, book.getSeries().getName());
        assertEquals(PUBLISHER, book.getPublisher().getName());

        ID = "journals-lncs-CabibboM93";
        PERSONS = new String[] { "Luca Cabibbo", "Giansalvatore Mecca" };
        TITLE = "Travel Agency: A LOGIDATA+ Application.";
        PAGES = "42-59";
        YEAR = 1993;
        EE = "http://dx.doi.org/10.1007/BFb0021889";
        InCollection inCollection = inCollectionService.findOne(ID);
        assertNotNull(inCollection);
        assertEquals(ID, inCollection.getId());
        assertEquals(TITLE, inCollection.getTitle());
        assertEquals(EE, inCollection.getElectronicEdition());
        assertEquals(PAGES, inCollection.getPages());
        assertEquals(YEAR, inCollection.getYear());
        assertEquals(book, inCollection.getParentPublication());
        assertTrue(checkPersons(inCollection.getAuthors(), PERSONS));
        assertEquals(0, inCollection.getEditors().size());

        ID = "journals-lncs-ArtaleBCCCFLGMM93";
        PERSONS = new String[] { "Alessandro Artale", "Jean Paul Ballerini",
        "Filippo Cacace", "Stefano Ceri", "Francesca Cesarini", "Anna Formica",
        "Herman Lam", "Sergio Greco", "G. Marrella", "Michele Missikoff", "Luigi Palopoli",
        "L. Pichetti", "Domenico Sacca", "Silvio Salza", "Claudio Sartori", "Giovanni Soda",
        "Letizia Tanca", "M. Toiati"};
        TITLE = "Prototypes in the LOGIDATA+ Project.";
        PAGES = "252-273";
        YEAR = 1993;
        EE = "http://dx.doi.org/10.1007/BFb0021901";
        inCollection = inCollectionService.findOne(ID);
        assertNotNull(inCollection);
        assertEquals(ID, inCollection.getId());
        assertEquals(TITLE, inCollection.getTitle());
        assertEquals(EE, inCollection.getElectronicEdition());
        assertEquals(PAGES, inCollection.getPages());
        assertEquals(YEAR, inCollection.getYear());
        assertEquals(book, inCollection.getParentPublication());
        assertTrue(checkPersons(inCollection.getAuthors(), PERSONS));
        assertEquals(0, inCollection.getEditors().size());

        ID = "journals-lncs-AtzeniCMT93";
        PERSONS = new String[] { "Paolo Atzeni", "Luca Cabibbo",
                "Giansalvatore Mecca", "Letizia Tanca" };
        TITLE = "The LOGIDATA+ Language and Semantics.";
        PAGES = "30-41";
        YEAR = 1993;
        EE = "http://dx.doi.org/10.1007/BFb0021888";
        inCollection = inCollectionService.findOne(ID);
        assertNotNull(inCollection);
        assertEquals(ID, inCollection.getId());
        assertEquals(TITLE, inCollection.getTitle());
        assertEquals(EE, inCollection.getElectronicEdition());
        assertEquals(PAGES, inCollection.getPages());
        assertEquals(YEAR, inCollection.getYear());
        assertEquals(book, inCollection.getParentPublication());
        assertTrue(checkPersons(inCollection.getAuthors(), PERSONS));
        assertEquals(0, inCollection.getEditors().size());

        ID = "journals-lncs-NanniST93a";
        PERSONS = new String[] { "Umberto Nanni", "Silvio Salza", "Mario Terranova" };
        TITLE = "The LOGIDATA+ Prototype System.";
        PAGES = "211-232";
        YEAR = 1993;
        EE = "http://dx.doi.org/10.1007/BFb0021899";
        inCollection = inCollectionService.findOne(ID);
        assertNotNull(inCollection);
        assertEquals(ID, inCollection.getId());
        assertEquals(TITLE, inCollection.getTitle());
        assertEquals(EE, inCollection.getElectronicEdition());
        assertEquals(PAGES, inCollection.getPages());
        assertEquals(YEAR, inCollection.getYear());
        assertEquals(book, inCollection.getParentPublication());
        assertTrue(checkPersons(inCollection.getAuthors(), PERSONS));
        assertEquals(0, inCollection.getEditors().size());
    }

    private boolean checkPersons(Set<Person> persons, String[] expectedPersonNames) {
        Set<String> expectedAuthorNamesSet = new HashSet<>(Arrays.asList(expectedPersonNames));
        Set<String> currentAuthorNamesSet = new HashSet<>(getPersonNames(persons));
        return expectedAuthorNamesSet.equals(currentAuthorNamesSet);
    }

    private Set<String> getPersonNames(Set<Person> persons) {
        Set<String> authorNames = new HashSet<>();
        for (Person author : persons) {
            authorNames.add(author.getName());
        }
        return authorNames;
    }
}
