package ch.ethz.globis.isk.parser.processor;

import ch.ethz.globis.isk.domain.Article;
import ch.ethz.globis.isk.domain.Journal;
import ch.ethz.globis.isk.domain.JournalEdition;
import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.parser.DBLPTag;
import ch.ethz.globis.isk.service.BaseService;
import ch.ethz.globis.isk.service.JournalEditionService;
import ch.ethz.globis.isk.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleXMLProcessor extends EntityXMLProcessor<Article> {

    @Autowired private JournalService journalService;
    @Autowired private JournalEditionService journalEditionService;

    @Autowired private PublicationXMLHelper helper;

    private String volume = null;
    private Integer year = null;
    private String number = null;
    private String journalName = null;

    @Autowired
    public ArticleXMLProcessor(BaseService<String, Article> service, EntityCache cache) {
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
            case DBLPTag.YEAR:
                year = FieldMapper.extractYear(value);
                instance.setYear(year);
                break;
            case DBLPTag.EE:
                instance.setElectronicEdition(value);
                break;
            case DBLPTag.CDROM:
                instance.setCdrom(value);
                break;
            case DBLPTag.PAGES:
                instance.setPages(value);
                break;
            case DBLPTag.NUMBER:
                number = value;
                break;
            case DBLPTag.VOLUME:
                volume = value;
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
            case DBLPTag.JOURNAL:
                journalName = value;
                break;
            default:
                break;
        }
    }

    @Override
    public Article build() {
        String journalId = FieldMapper.journalId(instance.getId());
        if (journalId == null) {
            return super.build();
        }
        journalId = FieldMapper.JOURNAL_ID_PREFIX + journalId;
        //build the journal
        Journal journal = cache.findJournal(journalId);
        if (journal == null) {
            journal = journalService.createEntity();
            journal.setId(journalId);
            journal.setName(journalName);
            cache.putJournal(journalId, journal);
        }

        //build the journal edition
        String journalEditionId = FieldMapper.journalEditionId(journalId, volume, year, number);
        journalEditionId = FieldMapper.JOURNAL_EDITION_ID_PREFIX + journalEditionId;

        JournalEdition journalEdition = (JournalEdition) cache.lookup(journalEditionId);
        if (journalEdition == null) {
            journalEdition = journalEditionService.createEntity();
            journalEdition.setId(journalEditionId);
            journalEdition.setJournal(journal);
            journalEdition.setVolume(volume);
            journalEdition.setYear(year);
            journalEdition.setNumber(number);
            journal.addEdition(journalEdition);
            cache.put(journalEditionId, journalEdition);
        }
        assert journalEdition.getJournal() == journal;

        journalEdition.addArticle(instance);
        instance.setJournalEdition(journalEdition);

        return super.build();
    }

    @Override
    public void clear() {
        super.clear();
        year = null;
        number = null;
        volume = null;
        journalName = null;
    }
}