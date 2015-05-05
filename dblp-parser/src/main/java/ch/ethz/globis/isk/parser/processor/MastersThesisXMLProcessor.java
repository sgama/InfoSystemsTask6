package ch.ethz.globis.isk.parser.processor;

import ch.ethz.globis.isk.domain.MasterThesis;
import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.School;
import ch.ethz.globis.isk.parser.DBLPTag;
import ch.ethz.globis.isk.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MastersThesisXMLProcessor extends EntityXMLProcessor<MasterThesis>{

    @Autowired private PublicationXMLHelper helper;

    @Autowired
    protected MastersThesisXMLProcessor(BaseService<String, MasterThesis> service, EntityCache cache) {
        super(service, cache);
    }

    @Override
    public void setData(String key, String value) {
        Person person = null;
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
            case DBLPTag.SCHOOL:
                School school = helper.findSchool(value);
                school.getPublications().add(instance);
                instance.setSchool(school);
                break;
            default:
                break;
        }
    }
}