package ch.ethz.globis.isk.web.model;

import ch.ethz.globis.isk.domain.Series;
import ch.ethz.globis.isk.web.utils.EncodingUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleSeriesDto extends DTO<Series> {

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

    public SimpleSeriesDto() {
    }

    public SimpleSeriesDto(Series entity) {
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
    public DTO<Series> convert(Series entity) {
        return new SimpleSeriesDto(entity);
    }
}
