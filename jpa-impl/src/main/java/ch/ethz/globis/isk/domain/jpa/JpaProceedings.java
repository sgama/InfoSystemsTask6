package ch.ethz.globis.isk.domain.jpa;

import ch.ethz.globis.isk.domain.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Proceedings")
public class JpaProceedings extends JpaPublication implements Proceedings {

    private String note;

    private Integer number;

    private String volume;

    private String isbn;

    @ForeignKey(name = "fk_publisher")
    @ManyToOne(targetEntity = JpaPublisher.class, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    private Publisher publisher;

    @ForeignKey(name = "fk_series")
    @ManyToOne(targetEntity = JpaSeries.class, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    private Series series;

    @ForeignKey(name = "fk_confed")
    @OneToOne(fetch = FetchType.LAZY, targetEntity = JpaConferenceEdition.class, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "conference_edition_id")
    private ConferenceEdition conferenceEdition;

    @ForeignKey(name = "fk_proceedings_children", inverseName = "fk_proceedings_parent")
    @OneToMany(mappedBy = "proceedings", targetEntity = JpaInProceedings.class)
    @Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    private Set<InProceedings> publications;

    public JpaProceedings() {
        publications = new HashSet<>();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (this.getIsbn() != null) {
            if (this.getNote() == null) {
                this.setNote("");
            }
            this.setNote(this.getNote() + "\nISBN updated, old value was " + this.getIsbn());
        }
        this.isbn = isbn;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public ConferenceEdition getConferenceEdition() {
        return conferenceEdition;
    }

    public void setConferenceEdition(ConferenceEdition conferenceEdition) {
        this.conferenceEdition = conferenceEdition;
    }

    public Set<InProceedings> getPublications() {
        return publications;
    }

    public void setPublications(Set<InProceedings> publications) {
        this.publications = publications;
    }
}
