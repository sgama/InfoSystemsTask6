package ch.ethz.globis.isk.web.model;

import ch.ethz.globis.isk.domain.Conference;
import ch.ethz.globis.isk.domain.ConferenceEdition;
import ch.ethz.globis.isk.web.utils.EncodingUtils;
import java.util.List;

public class ConferenceDto extends DTO<Conference> {

    private String id;

    private String name;

    private List<DTO<ConferenceEdition>> editions;

    public ConferenceDto() {
    }

    public ConferenceDto(Conference conference) {
        this.id = EncodingUtils.encode(conference.getId());
        this.name = conference.getName();
        this.editions = DTOs.create(conference.getEditions(), ConferenceEditionDto.class);
    }

    public List<DTO<ConferenceEdition>> getEditions() {
        return editions;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public DTO<Conference> convert(Conference entity) {
        return new ConferenceDto(entity);
    }
}
