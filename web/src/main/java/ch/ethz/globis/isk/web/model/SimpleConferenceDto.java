package ch.ethz.globis.isk.web.model;

import ch.ethz.globis.isk.domain.Conference;
import ch.ethz.globis.isk.web.utils.EncodingUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleConferenceDto extends DTO<Conference> {

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

    public SimpleConferenceDto(String id, String name) {
        this.id = EncodingUtils.encode(id);
        this.name = name;
    }

    public SimpleConferenceDto() {
    }

    public SimpleConferenceDto(Conference entity) {
        this.id = EncodingUtils.encode(entity.getId());
        this.name = entity.getName();
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
    public DTO<Conference> convert(Conference entity) {
        return new SimpleConferenceDto(entity);
    }
}
