package ch.ethz.globis.isk.mongo.service;

import ch.ethz.globis.isk.config.PersistenceConfig;
import ch.ethz.globis.isk.service.*;
import ch.ethz.globis.isk.test.service.PublicationServiceTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceConfig.class })
@ActiveProfiles({ "test" })
public class MongoPublicationServiceTest extends PublicationServiceTest {

    @Autowired
    @Override
    public void setProceedingsService(ProceedingsService proceedingsService) {
        super.setProceedingsService(proceedingsService);
    }

    @Autowired
    @Override
    public void setInProceedingsService(InProceedingsService inProceedingsService) {
        super.setInProceedingsService(inProceedingsService);
    }

    @Autowired
    @Override
    public void setConferenceEditionService(ConferenceEditionService conferenceEditionService) {
        super.setConferenceEditionService(conferenceEditionService);
    }

    @Autowired
    @Override
    public void setConferenceService(ConferenceService conferenceService) {
        super.setConferenceService(conferenceService);
    }

    @Autowired
    @Override
    public void setPersonService(PersonService personService) {
        super.setPersonService(personService);
    }

    @Autowired
    @Override
    public void setBookService(BookService bookService) {
        super.setBookService(bookService);
    }

    @Autowired
    @Override
    public void setPublisherService(PublisherService publisherService) {
        super.setPublisherService(publisherService);
    }

    @Autowired
    @Override
    public void setSeriesService(SeriesService seriesService) {
        super.setSeriesService(seriesService);
    }

    @Autowired
    @Override
    public void setPhdThesisService(PhdThesisService phdThesisService) {
        super.setPhdThesisService(phdThesisService);
    }

    @Autowired
    @Override
    public void setSchoolService(SchoolService schoolService) {
        super.setSchoolService(schoolService);
    }

    @Autowired
    @Override
    public void setArticleService(ArticleService articleService) {
        super.setArticleService(articleService);
    }

    @Autowired
    @Override
    public void setJournalService(JournalService journalService) {
        super.setJournalService(journalService);
    }

    @Autowired
    @Override
    public void setJournalEditionService(JournalEditionService journalEditionService) {
        super.setJournalEditionService(journalEditionService);
    }

    @Autowired
    @Override
    public void setMasterThesisService(MasterThesisService masterThesisService) {
        super.setMasterThesisService(masterThesisService);
    }

    @Autowired
    @Override
    public void setInCollectionService(InCollectionService inCollectionService) {
        super.setInCollectionService(inCollectionService);
    }

    @Autowired
    @Override
    public void setPublicationService(PublicationService publicationService) {
        super.setPublicationService(publicationService);
    }

    @Autowired
    @Override
    public void setTm(TransactionManager tm) {
        super.setTm(tm);
    }
}
