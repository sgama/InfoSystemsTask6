package ch.ethz.globis.isk.domain.db4o;

import ch.ethz.globis.isk.domain.Journal;
import ch.ethz.globis.isk.domain.JournalEdition;

import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.collections.ActivatableHashSet;
import com.db4o.collections.ActivatableSet;
import com.db4o.config.annotations.Indexed;
import com.db4o.ta.Activatable;

import java.util.Set;

public class Db4oJournal implements Journal, Activatable {

	@Indexed()
    private String id;

    private String name;

    private ActivatableSet<JournalEdition> editions;

    private transient Activator activator;

    public Db4oJournal() {
        editions = new ActivatableHashSet<>();
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

    public Set<JournalEdition> getEditions() {
        activate(ActivationPurpose.READ);
        return editions;
    }

    public void addEdition(JournalEdition edition) {
        activate(ActivationPurpose.WRITE);
        editions.add(edition);
    }

    public void setEditions(Set<JournalEdition> editions) {
        activate(ActivationPurpose.WRITE);
        this.editions.clear();
        this.editions.addAll(editions);
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
