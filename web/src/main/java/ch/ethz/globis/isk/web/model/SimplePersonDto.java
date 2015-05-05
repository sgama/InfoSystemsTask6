package ch.ethz.globis.isk.web.model;

import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.web.utils.EncodingUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimplePersonDto extends DTO<Person> {

    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("authored_publications")
    private int authoredPublications;

    @JsonProperty("edited_publications")
    private int editedPublications;

    public SimplePersonDto() { }

    public SimplePersonDto(Person person) {
        this.authoredPublications = person.getAuthoredPublications().size();
        this.editedPublications = person.getEditedPublications().size();
        this.name = person.getName();

        this.id = EncodingUtils.encode(person.getId());
    }

    public int getAuthoredPublications() {
        return authoredPublications;
    }

    public void setAuthoredPublications(int authoredPublications) {
        this.authoredPublications = authoredPublications;
    }

    public int getEditedPublications() {
        return editedPublications;
    }

    public void setEditedPublications(int editedPublications) {
        this.editedPublications = editedPublications;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @Override
    public DTO<Person> convert(Person entity) {
        return new SimplePersonDto(entity);
    }
}