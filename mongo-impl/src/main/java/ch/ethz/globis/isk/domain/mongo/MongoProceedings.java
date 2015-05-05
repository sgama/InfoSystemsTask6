package ch.ethz.globis.isk.domain.mongo;

import ch.ethz.globis.isk.domain.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "publication")
public class MongoProceedings extends MongoPublication implements Proceedings {

    private String note;

    private Integer number;

    private String volume;

    private String isbn;

    @DBRef(lazy = true)
    private Publisher publisher;

    @DBRef(lazy = true)
    private Series series;

    @DBRef(lazy = true)
    private ConferenceEdition conferenceEdition;

    @DBRef(lazy = true)
    private Set<InProceedings> publications;

    public MongoProceedings() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Proceedings))
            return false;
        if (!super.equals(o))
            return false;
        Proceedings that = (Proceedings) o;
        if (getIsbn() != null ? !getIsbn().equals(that.getIsbn()) : that.getIsbn() != null)
            return false;
        if (getNote() != null ? !getNote().equals(that.getNote()) : that.getNote() != null)
            return false;
        if (getNumber() != null ? !getNumber().equals(that.getNumber()) : that.getNumber() != null)
            return false;
        if (getVolume() != null ? !getVolume().equals(that.getVolume()) : that.getVolume() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getNote() != null ? getNote().hashCode() : 0);
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        result = 31 * result + (getVolume() != null ? getVolume().hashCode() : 0);
        result = 31 * result + (getIsbn() != null ? getIsbn().hashCode() : 0);
        return result;
    }
}
