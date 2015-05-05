package ch.ethz.globis.isk.parser.processor;


import ch.ethz.globis.isk.domain.*;
import ch.ethz.globis.isk.parser.DBLPTag;
import ch.ethz.globis.isk.service.BaseService;
import ch.ethz.globis.isk.service.ConferenceEditionService;
import ch.ethz.globis.isk.service.ConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProceedingsXMLProcessor extends EntityXMLProcessor<Proceedings> {

    @Autowired private PublicationXMLHelper helper;
    @Autowired private ConferenceService conferenceService;
    @Autowired private ConferenceEditionService conferenceEditionService;

    private String proceedingsKey;
    private String conferenceName;

    @Autowired
    protected ProceedingsXMLProcessor(BaseService<String, Proceedings> service, EntityCache cache) {
        super(service, cache);
    }

    @Override
    public void setData(String key, String value) {
        Person person;

        switch (key) {
            case DBLPTag.KEY:
                String proceedingsId = FieldMapper.publicationKey(value);
                Proceedings proceedings = cache.findAndCast(proceedingsId, Proceedings.class);
                if (proceedings == null) {
                    instance.setId(FieldMapper.publicationKey(value));
                } else {
                    instance = proceedings;
                }
                proceedingsKey = value;
                break;
            case DBLPTag.BOOKTITLE:
                this.conferenceName = value;
                break;
            case DBLPTag.TITLE:
                instance.setTitle(value);
                break;
            case DBLPTag.EE:
                instance.setElectronicEdition(value);
                break;
            case DBLPTag.VOLUME:
                instance.setVolume(value);
                break;
            case DBLPTag.YEAR:
                instance.setYear(FieldMapper.extractYear(value));
                break;
            case DBLPTag.AUTHOR:
                person = helper.findPerson(value);
                person.getAuthoredPublications().add(instance);
                instance.getAuthors().add(person);
                break;
            case DBLPTag.EDITOR:
                person = helper.findPerson(value);
                person.getEditedPublications().add(instance);
                instance.getEditors().add(person);
                break;
            case DBLPTag.ISBN:
                instance.setIsbn(value);
                break;
            case DBLPTag.NOTE:
                instance.setNote(value);
                break;
            case DBLPTag.NUMBER:
                instance.setNumber(FieldMapper.extractInt(value));
                break;
            case DBLPTag.SERIES:
                Series series = helper.findSeries(value);
                series.getPublications().add(instance);
                instance.setSeries(series);
                break;
            case DBLPTag.PUBLISHER:
                Publisher publisher = helper.findPublisher(value);
                publisher.getPublications().add(instance);
                instance.setPublisher(publisher);
                break;
            default:
                break;
        }
    }

    @Override
    public Proceedings build() {
        //find the conference associated with the proceedings
        String conferenceId = FieldMapper.conferenceId(proceedingsKey);

        if (conferenceId != null) {
            conferenceId = FieldMapper.CONFERENCE_ID_PREFIX + conferenceId;

            Conference conference = cache.findConference(conferenceId);
            //create a conference edition object for this proceedings
            if (conference == null) {
                conference = conferenceService.createEntity();
                conference.setId(conferenceId);
                if (conferenceName == null) {
                    conferenceName = conferenceId;
                }
                conference.setName(conferenceName);
                cache.putConference(conferenceId, conference);
            }

            String conferenceEditionId = FieldMapper.conferenceEditionId(proceedingsKey);
            conferenceEditionId = FieldMapper.CONFERENCE_EDITION_ID_PREFIX + conferenceEditionId;
            ConferenceEdition conferenceEdition = conferenceEditionService.createEntity();
            conferenceEdition.setId(conferenceEditionId);
            conferenceEdition.setYear(instance.getYear());
            conferenceEdition.setConference(conference);
            conferenceEdition.setProceedings(instance);
            conference.getEditions().add(conferenceEdition);
            instance.setConferenceEdition(conferenceEdition);
        }
        return super.build();
    }

    @Override
    public void clear() {
        super.clear();
    }
}