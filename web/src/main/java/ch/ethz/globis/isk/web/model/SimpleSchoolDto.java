package ch.ethz.globis.isk.web.model;

import ch.ethz.globis.isk.domain.School;
import ch.ethz.globis.isk.web.utils.EncodingUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleSchoolDto extends DTO<School> {

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

    public SimpleSchoolDto(String id, String name) {
        this.id = EncodingUtils.encode(id);
        this.name = name;
    }

    public SimpleSchoolDto() {
    }

    public SimpleSchoolDto(School entity) {
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
    public DTO<School> convert(School entity) {
        return new SimpleSchoolDto(entity);
    }
}
