package ch.ethz.globis.isk.mongo.benchmark;

import ch.ethz.globis.isk.config.PersistenceConfig;
import ch.ethz.globis.isk.service.ConferenceService;
import ch.ethz.globis.isk.service.PersonService;
import ch.ethz.globis.isk.service.PublicationService;
import ch.ethz.globis.isk.test.benchmark.PerformanceIndividualAnalysis;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceConfig.class })
@ActiveProfiles({ "production" })
public class MongoPerformanceIndividualAnalysis extends PerformanceIndividualAnalysis {

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
    public void setPublicationService(PublicationService publicationService) {
        super.setPublicationService(publicationService);
    }
}
