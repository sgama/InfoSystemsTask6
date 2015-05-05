package ch.ethz.globis.isk.parser.processor;

import ch.ethz.globis.isk.domain.Book;
import ch.ethz.globis.isk.domain.InCollection;
import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.parser.DBLPTag;
import ch.ethz.globis.isk.service.BaseService;
import ch.ethz.globis.isk.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InCollectionXMLProcessor extends EntityXMLProcessor<InCollection> {

    @Autowired private PublicationXMLHelper helper;
    @Autowired private BookService bookService;

    @Autowired
    public InCollectionXMLProcessor(BaseService<String, InCollection> service, EntityCache cache) {
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
            case DBLPTag.PAGES:
                instance.setPages(value);
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
            case DBLPTag.CROSSREF:
                String bookId = FieldMapper.publicationKey(value);
                Book book = resolvedBook(bookId);
                if (book != null) {
                    book.getPublications().add(instance);
                    instance.setParentPublication(book);
                }
                break;
            default:
                break;
        }
    }

    private Book resolvedBook(String bookId) {
        Book book = cache.findAndCast(bookId, Book.class);
        if (book == null) {
            book = bookService.createEntity();
            book.setId(bookId);
            cache.put(bookId, book);
        }
        return book;
    }
}