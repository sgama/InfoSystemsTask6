package ch.ethz.globis.isk.domain.db4o;

import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Publication;

import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.collections.ActivatableHashSet;
import com.db4o.collections.ActivatableSet;
import com.db4o.config.annotations.Indexed;
import com.db4o.ta.Activatable;

import java.util.Set;

public class Db4oPerson implements Person, Activatable {

    private String id;

    private String name;
    public ActivatableSet<Publication> authoredPublications;
    public ActivatableSet<Publication> editedPublications;

    private transient Activator activator;

    public Db4oPerson() {
        authoredPublications = new ActivatableHashSet<>();
        editedPublications = new ActivatableHashSet<>();
    }

    public String getId() {
        activate(ActivationPurpose.READ);
        return id;
    }

    public void setId(String id) {
        activate(ActivationPurpose.WRITE);
        this.id = id;
    }

    public String getName() {
        activate(ActivationPurpose.READ);
        return name;
    }

    public void setName(String name) {
        activate(ActivationPurpose.WRITE);
        this.name = name;
    }

    public Set<Publication> getAuthoredPublications() {
        activate(ActivationPurpose.READ);
        return authoredPublications;
    }

    public void setAuthoredPublications(Set<Publication> publications) {
        activate(ActivationPurpose.WRITE);
        this.authoredPublications = new ActivatableHashSet<>(publications);
    }

    public Set<Publication> getEditedPublications() {
        activate(ActivationPurpose.READ);
        return editedPublications;
    }

    public void setEditedPublications(Set<Publication> publications) {
        activate(ActivationPurpose.WRITE);
        this.editedPublications = new ActivatableHashSet<>(publications);
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
    public String toString() {
        return "Db4oPerson{" + "id='" + getId() + '\'' + ", name='" + getName() + '\'' + '}';
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    @Override
    public void bind(Activator activator) {
        if (this.activator == activator) {
            return;
        }
        if (activator != null && this.activator != null) {
            throw new IllegalStateException();
        }
        this.activator = activator;
    }

    @Override
    public void activate(ActivationPurpose activationPurpose) {
        if (this.activator != null) {
            activator.activate(activationPurpose);
        }
    }
}
