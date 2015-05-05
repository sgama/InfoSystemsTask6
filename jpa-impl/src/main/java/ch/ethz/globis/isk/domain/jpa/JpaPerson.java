package ch.ethz.globis.isk.domain.jpa;

import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Publication;

import javax.persistence.*;

import org.hibernate.annotations.Index;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Person")
@Table(name = "person")
public class JpaPerson implements Person {

    @Id
    @Index(name="index_id_person")
    private String id;

    private String name;

    @ManyToMany(mappedBy = "authors", targetEntity = JpaPublication.class)
    private Set<Publication> authoredPublications;

    @ManyToMany(mappedBy = "editors", targetEntity = JpaPublication.class)
    private Set<Publication> editedPublications;

    public JpaPerson() {
        authoredPublications = new HashSet<>();
        editedPublications = new HashSet<>();
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

    public Set<Publication> getAuthoredPublications() {
        return authoredPublications;
    }

    public void setAuthoredPublications(Set<Publication> publications) {
        this.authoredPublications = publications;
    }

    public Set<Publication> getEditedPublications() {
        return editedPublications;
    }

    public void setEditedPublications(Set<Publication> editedPublications) {
        this.editedPublications = editedPublications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Person))
            return false;
        Person person = (Person) o;
        if (getId() != null ? !getId().equals(person.getId()) : person.getId() != null)
            return false;
        if (getName() != null ? !getName().equals(person.getName()) : person.getName() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
