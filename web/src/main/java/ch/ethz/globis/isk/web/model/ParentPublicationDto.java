package ch.ethz.globis.isk.web.model;

import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.web.utils.EncodingUtils;

public class ParentPublicationDto extends DTO<Publication>{

    private String id;
    private String title;

    public ParentPublicationDto() {
    }

    public ParentPublicationDto(Publication publication) {
        this.id = EncodingUtils.encode(publication.getId());
        this.title = publication.getTitle();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public DTO<Publication> convert(Publication entity) {
        return new ParentPublicationDto(entity);
    }
}