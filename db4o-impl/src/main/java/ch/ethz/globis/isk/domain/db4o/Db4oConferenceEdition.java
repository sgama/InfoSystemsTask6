package ch.ethz.globis.isk.domain.db4o;

import ch.ethz.globis.isk.domain.Conference;
import ch.ethz.globis.isk.domain.ConferenceEdition;
import ch.ethz.globis.isk.domain.Proceedings;

import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.config.annotations.Indexed;
import com.db4o.ta.Activatable;

public class Db4oConferenceEdition implements ConferenceEdition, Activatable {

	@Indexed()
    private String id;

    private Integer year;

    @Indexed()
    private Conference conference;

    private Proceedings proceedings;

    private transient Activator activator;

    public Db4oConferenceEdition() {
    }

    public Conference getConference() {
        activate(ActivationPurpose.READ);
        return conference;
    }

    public void setConference(Conference conference) {
        activate(ActivationPurpose.WRITE);
        this.conference = conference;
    }

    public Proceedings getProceedings() {
        activate(ActivationPurpose.READ);
        return proceedings;
    }

    public void setProceedings(Proceedings proceedings) {
        activate(ActivationPurpose.WRITE);
        this.proceedings = proceedings;
    }

    public String getId() {
        activate(ActivationPurpose.READ);
        return id;
    }

    public void setId(String id) {
        activate(ActivationPurpose.WRITE);
        this.id = id;
    }

    public Integer getYear() {
        activate(ActivationPurpose.READ);
        return year;
    }

    public void setYear(Integer year) {
        activate(ActivationPurpose.WRITE);
        this.year = year;
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
    public String toString() {
        return "ConferenceEdition{" + "id='" + getId() + '\'' + ", year=" + getYear() + '}';
    }
}
