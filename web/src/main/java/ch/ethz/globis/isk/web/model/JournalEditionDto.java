package ch.ethz.globis.isk.web.model;

import ch.ethz.globis.isk.domain.JournalEdition;
import ch.ethz.globis.isk.web.utils.EncodingUtils;

public class JournalEditionDto extends DTO<JournalEdition> {

    private String id;

    private Integer year;

    private String volume;

    private String number;

    private SimpleJournalDto journal;

    public JournalEditionDto() {
    }

    public JournalEditionDto(JournalEdition edition) {
        this.id = EncodingUtils.encode(edition.getId());
        this.number = edition.getNumber();
        this.volume = edition.getVolume();
        this.year = edition.getYear();
        this.journal = new SimpleJournalDto(edition.getJournal());
    }

    public String getId() {
        return id;
    }

    public SimpleJournalDto getJournal() {
        return journal;
    }

    public String getNumber() {
        return number;
    }

    public String getVolume() {
        return volume;
    }

    public Integer getYear() {
        return year;
    }

    public static JournalEditionDto fromJournalEdition(JournalEdition journalEdition) {
        if (journalEdition == null) {
            return null;
        }
        return new JournalEditionDto(journalEdition);
    }

    @Override
    public DTO<JournalEdition> convert(JournalEdition entity) {
        return new JournalEditionDto(entity);
    }
}
