package ch.ethz.globis.isk.domain.db4o;

import ch.ethz.globis.isk.domain.Conference;
import ch.ethz.globis.isk.domain.ConferenceEdition;

import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.collections.ActivatableHashSet;
import com.db4o.collections.ActivatableSet;
import com.db4o.config.annotations.Indexed;
import com.db4o.ta.Activatable;

import java.util.Set;

public class Db4oConference implements Conference, Activatable {

    private String id;

    private String name;

    private ActivatableSet<ConferenceEdition> editions;

    private transient Activator activator;

    public Db4oConference() {
        editions = new ActivatableHashSet<>();
    }

    public String getName() {
        activate(ActivationPurpose.READ);
        return name;
    }

    public void setName(String name) {
        activate(ActivationPurpose.WRITE);
        this.name = name;
    }

    public Set<ConferenceEdition> getEditions() {
        activate(ActivationPurpose.READ);
        return editions;
    }

    public void setEditions(Set<ConferenceEdition> editions) {
        activate(ActivationPurpose.WRITE);
        this.editions.clear();
        this.editions.addAll(editions);
    }

    public String getId() {
        activate(ActivationPurpose.READ);
        return id;
    }

    public void setId(String id) {
        activate(ActivationPurpose.WRITE);
        this.id = id;
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
