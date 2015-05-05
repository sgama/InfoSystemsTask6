package ch.ethz.globis.isk.parser.processor;

import ch.ethz.globis.isk.domain.InProceedings;
import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Proceedings;
import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.parser.DBLPTag;
import ch.ethz.globis.isk.service.BaseService;
import ch.ethz.globis.isk.service.ProceedingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InProceedingsXMLProcessor extends EntityXMLProcessor<InProceedings> {

    @Autowired private PublicationXMLHelper helper;
    @Autowired private ProceedingsService proceedingsService;

    private String crossref;

    @Autowired
    public InProceedingsXMLProcessor(BaseService<String, InProceedings> service, EntityCache cache) {
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
            case DBLPTag.YEAR:
                instance.setYear(FieldMapper.extractYear(value));
                break;
            case DBLPTag.PAGES:
                instance.setPages(value);
                break;
            case DBLPTag.NOTE:
                instance.setNote(value);
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
            case DBLPTag.CROSSREF:
                this.crossref = value;
                break;
            default:
                break;
        }
    }

    @Override
    public InProceedings build() {
        String proceedingsId;
        Proceedings proceedings;
        if (this.crossref != null) {
            proceedingsId = FieldMapper.publicationKey(crossref);
            Publication publication = cache.findAndCast(proceedingsId, Publication.class);
            if (publication == null) {
                proceedings = proceedingsService.createEntity();
                proceedings.setId(proceedingsId);
                cache.put(proceedingsId, proceedings);
                instance.setProceedings(proceedings);
                proceedings.getPublications().add(instance);
            } else if (publication instanceof Proceedings) {
                proceedings = (Proceedings) publication;
                instance.setProceedings(proceedings);
                proceedings.getPublications().add(instance);
            }
        }
        return super.build();
    }

    @Override
    public void clear() {
        this.crossref = null;
        super.clear();
    }
}