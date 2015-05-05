package ch.ethz.globis.isk.web.model;

import ch.ethz.globis.isk.domain.Journal;
import ch.ethz.globis.isk.web.utils.EncodingUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleJournalDto extends DTO<Journal> {

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

    public SimpleJournalDto() {
    }

    public SimpleJournalDto(Journal journal) {
        this.id = EncodingUtils.encode(journal.getId());
        this.name = journal.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public DTO<Journal> convert(Journal entity) {
        return new SimpleJournalDto(entity);
    }
}
