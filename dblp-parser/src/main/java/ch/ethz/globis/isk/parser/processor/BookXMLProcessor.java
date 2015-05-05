package ch.ethz.globis.isk.parser.processor;

import ch.ethz.globis.isk.domain.Book;
import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Publisher;
import ch.ethz.globis.isk.domain.Series;
import ch.ethz.globis.isk.parser.DBLPTag;
import ch.ethz.globis.isk.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookXMLProcessor extends EntityXMLProcessor<Book> {

    @Autowired private PublicationXMLHelper helper;

    @Autowired
    protected BookXMLProcessor(BaseService<String, Book> service, EntityCache cache) {
        super(service, cache);
    }

    @Override
    public void setData(String key, String value) {
        Person person;
        switch (key) {
            case DBLPTag.KEY:
                String bookId = FieldMapper.publicationKey(value);
                Book book = cache.findAndCast(bookId, Book.class);
                if (book == null) {
                    instance.setId(bookId);
                } else {
                    instance = book;
                }
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
            case DBLPTag.MONTH:
                instance.setMonth(FieldMapper.extractMonth(value));
                break;
            case DBLPTag.CDROM:
                instance.setCdrom(value);
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
            case DBLPTag.SERIES:
                Series series = helper.findSeries(value);
                series.getPublications().add(instance);
                instance.setSeries(series);
                break;
            case DBLPTag.PUBLISHER:
                if (instance.getPublisher() == null) {
                    Publisher publisher = helper.findPublisher(value);
                    publisher.getPublications().add(instance);
                    instance.setPublisher(publisher);
                }
                break;
            default:
                break;
        }
    }
}