package ch.ethz.globis.isk.web.model;

import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.web.utils.EncodingUtils;

public class PublicationSimpleDto extends DTO<Publication> {

    private String id;
    private String title;
    private String ee;
    private Integer year;

    public PublicationSimpleDto() { }

    public PublicationSimpleDto(String ee, String id, String title, Integer year) {
        this.ee = ee;
        this.id = EncodingUtils.encode(id);
        this.title = title;
        this.year = year;
    }

    public PublicationSimpleDto(Publication entity) {
        this.id = entity.getId();
        this.ee = entity.getElectronicEdition();
        this.title = entity.getTitle();
        this.year = entity.getYear();
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getEe() {
        return ee;
    }

    public void setEe(String ee) {
        this.ee = ee;
    }

    public String getId() {
        return id;
    }

    public void setId(String key) {
        this.id = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public DTO<Publication> convert(Publication entity) {
        return new PublicationSimpleDto(entity);
    }
}