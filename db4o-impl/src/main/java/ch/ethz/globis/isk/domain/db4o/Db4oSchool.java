package ch.ethz.globis.isk.domain.db4o;

import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.domain.School;

import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.collections.ActivatableHashSet;
import com.db4o.collections.ActivatableSet;
import com.db4o.config.annotations.Indexed;
import com.db4o.ta.Activatable;

import java.util.Set;

public class Db4oSchool implements School, Activatable {

	@Indexed()
    private String id;

    private String name;

    private ActivatableSet<Publication> publications;

    private transient Activator activator;

    public Db4oSchool() {
        publications = new ActivatableHashSet<>();
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

    @Override
    public Set<Publication> getPublications() {
        activate(ActivationPurpose.READ);
        return publications;
    }

    @Override
    public void setPublications(Set<Publication> publications) {
        activate(ActivationPurpose.WRITE);
        this.publications.clear();
        this.publications.addAll(publications);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("School{");
        sb.append("id=").append(getId());
        sb.append(", name='").append(getName()).append('\'');
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
}
