package ch.ethz.globis.isk.web.model;

import ch.ethz.globis.isk.domain.Journal;
import ch.ethz.globis.isk.domain.JournalEdition;
import ch.ethz.globis.isk.web.utils.EncodingUtils;

import java.util.List;

import static ch.ethz.globis.isk.web.model.DTOs.create;

public class JournalDto extends DTO<Journal> {

    private String id;
    private String name;
    private List<DTO<JournalEdition>> editions;

    public JournalDto() {
    }

    public JournalDto(List<DTO<JournalEdition>> editions, String id, String name) {
        this.editions = editions;
        this.id = EncodingUtils.encode(id);
        this.name = name;
    }

    public List<DTO<JournalEdition>> getEditions() {
        return editions;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public DTO<Journal> convert(Journal entity) {
        List<DTO<JournalEdition>> editionDtos =
                create(entity.getEditions(), JournalEditionDto.class);
        return new JournalDto(editionDtos, entity.getId(), entity.getName());
    }
}