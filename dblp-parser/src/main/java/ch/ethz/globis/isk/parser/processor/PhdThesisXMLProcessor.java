package ch.ethz.globis.isk.parser.processor;

import ch.ethz.globis.isk.domain.*;
import ch.ethz.globis.isk.parser.DBLPTag;
import ch.ethz.globis.isk.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhdThesisXMLProcessor extends EntityXMLProcessor<PhdThesis> {

    @Autowired private PublicationXMLHelper helper;

    @Autowired
    protected PhdThesisXMLProcessor(BaseService<String, PhdThesis> service, EntityCache cache) {
        super(service, cache);
    }

    @Override
    public void setData(String key, String value) {
        Person person;
        switch (key) {
            case DBLPTag.KEY:
                instance.setId(FieldMapper.publicationKey(value));
                break;
            case DBLPTag.TITLE:
                instance.setTitle(value);
                break;
            case DBLPTag.EE:
                instance.setElectronicEdition(value);
                break;
            case DBLPTag.NOTE:
                instance.setNote(value);
                break;
            case DBLPTag.NUMBER:
                instance.setNumber(FieldMapper.extractInt(value));
                break;
            case DBLPTag.YEAR:
                instance.setYear(FieldMapper.extractYear(value));
                break;
            case DBLPTag.MONTH:
                instance.setMonth(FieldMapper.extractMonth(value));
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
            case DBLPTag.SCHOOL:
                School school = helper.findSchool(value);
                school.getPublications().add(instance);
                instance.setSchool(school);
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
}