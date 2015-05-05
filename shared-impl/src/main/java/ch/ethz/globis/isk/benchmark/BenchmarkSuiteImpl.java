package ch.ethz.globis.isk.benchmark;

import ch.ethz.globis.isk.service.ConferenceService;
import ch.ethz.globis.isk.service.PersonService;
import ch.ethz.globis.isk.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BenchmarkSuiteImpl extends BenchmarkSuite {

    @Autowired
    @Override
    public void setPersonService(PersonService personService) {
        super.setPersonService(personService);
    }

    @Autowired
    @Override
    public void setConferenceService(ConferenceService conferenceService) {
        super.setConferenceService(conferenceService);
    }

    @Autowired
    @Override
    public void setPublicationService(PublicationService publicationService) {
        super.setPublicationService(publicationService);
    }
}