package ch.ethz.globis.isk.domain.db4o;

import ch.ethz.globis.isk.domain.*;
import com.db4o.activation.ActivationPurpose;
import com.db4o.collections.ActivatableHashSet;
import com.db4o.collections.ActivatableSet;
import com.db4o.ta.Activatable;
import java.util.Set;

public class Db4oProceedings extends Db4oPublication implements Proceedings, Activatable {

    private String note;

    private Integer number;

    private String volume;

    private String isbn;

    private Publisher publisher;

    private Series series;

    private ConferenceEdition conferenceEdition;

    private ActivatableSet<InProceedings> publications;

    public Db4oProceedings() {
        publications = new ActivatableHashSet<>();
    }

    public String getNote() {
        activate(ActivationPurpose.READ);
        return note;
    }

    public void setNote(String note) {
        activate(ActivationPurpose.WRITE);
        this.note = note;
    }

    public Integer getNumber() {
        activate(ActivationPurpose.READ);
        return number;
    }

    public void setNumber(Integer number) {
        activate(ActivationPurpose.WRITE);
        this.number = number;
    }

    public Publisher getPublisher() {
        activate(ActivationPurpose.READ);
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        activate(ActivationPurpose.WRITE);
        this.publisher = publisher;
    }

    public String getVolume() {
        activate(ActivationPurpose.READ);
        return volume;
    }

    public void setVolume(String volume) {
        activate(ActivationPurpose.WRITE);
        this.volume = volume;
    }

    public String getIsbn() {
        activate(ActivationPurpose.READ);
        return isbn;
    }

    public void setIsbn(String isbn) {
        activate(ActivationPurpose.WRITE);
        if (this.getIsbn() != null) {
            if (this.getNote() == null) {
                this.setNote("");
            }
            this.setNote(this.getNote() + "\nISBN updated, old value was " + this.getIsbn());
        }
        this.isbn = isbn;
    }

    public Series getSeries() {
        activate(ActivationPurpose.READ);
        return series;
    }

    public void setSeries(Series series) {
        activate(ActivationPurpose.WRITE);
        this.series = series;
    }

    public ConferenceEdition getConferenceEdition() {
        activate(ActivationPurpose.READ);
        return conferenceEdition;
    }

    public void setConferenceEdition(ConferenceEdition conferenceEdition) {
        activate(ActivationPurpose.WRITE);
        this.conferenceEdition = conferenceEdition;
    }

    public Set<InProceedings> getPublications() {
        activate(ActivationPurpose.READ);
        return publications;
    }

    public void setPublications(Set<InProceedings> publications) {
        activate(ActivationPurpose.WRITE);
        this.publications = new ActivatableHashSet<>(publications);
    }
}
