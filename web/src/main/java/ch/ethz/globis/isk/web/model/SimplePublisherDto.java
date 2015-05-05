package ch.ethz.globis.isk.web.model;

import ch.ethz.globis.isk.domain.Publisher;
import ch.ethz.globis.isk.web.utils.EncodingUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimplePublisherDto extends DTO<Publisher> {

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

    public SimplePublisherDto() {
    }

    public SimplePublisherDto(Publisher entity) {
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
    public DTO<Publisher> convert(Publisher entity) {
        return new SimplePublisherDto(entity);
    }
}