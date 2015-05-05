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

public class Db4oPublication implements Publication, Activatable {

	@Indexed()
    private String id;

	@Indexed()
    private String title;

    private String electronicEdition;

    @Indexed()
    private int year;

    private ActivatableSet<Person> authors;

    private ActivatableSet<Person> editors;

    protected transient Activator activator;

    public Db4oPublication() {
        authors = new ActivatableHashSet<>();
        editors = new ActivatableHashSet<>();
    }

    public String getId() {
        activate(ActivationPurpose.READ);
        return id;
    }

    public void setId(String key) {
        activate(ActivationPurpose.WRITE);
        this.id = key;
    }

    public String getTitle() {
        activate(ActivationPurpose.READ);
        return title;
    }

    public void setTitle(String title) {
        activate(ActivationPurpose.WRITE);
        this.title = title;
    }

    public Set<Person> getAuthors() {
        activate(ActivationPurpose.READ);
        return authors;
    }

    @Override
    public void setAuthors(Set<Person> authors) {
        activate(ActivationPurpose.WRITE);
        this.authors.clear();
        this.authors.addAll(authors);
    }

    public Set<Person> getEditors() {
        activate(ActivationPurpose.READ);
        return editors;
    }

    @Override
    public void setEditors(Set<Person> editors) {
        activate(ActivationPurpose.WRITE);
        this.editors.clear();
        this.editors.addAll(editors);
    }

    public Integer getYear() {
        activate(ActivationPurpose.READ);
        return year;
    }

    public void setYear(Integer year) {
        activate(ActivationPurpose.WRITE);
        this.year = year;
    }

    public String getElectronicEdition() {
        activate(ActivationPurpose.READ);
        return electronicEdition;
    }

    public void setElectronicEdition(String electronicEdition) {
        activate(ActivationPurpose.WRITE);
        this.electronicEdition = electronicEdition;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Publication{");
        sb.append("id='").append(getId()).append('\'');
        sb.append(", title='").append(getTitle()).append('\'');
        sb.append(", year=").append(getYear());
        sb.append('}');
        return sb.toString();
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Publication))
            return false;
        Publication that = (Publication) o;
        if (!getId().equals(that.getId()))
            return false;
        if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }
}
