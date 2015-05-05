package ch.ethz.globis.isk.domain.jpa;

import ch.ethz.globis.isk.domain.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Book")
public class JpaBook extends JpaPublication implements Book {

    private String cdrom;

    private Integer month;

    private String volume;

    private String isbn;

    @ManyToOne(targetEntity = JpaSeries.class)
    private Series series;

    @ManyToOne(targetEntity = JpaPublisher.class)
    private Publisher publisher;

    @OneToMany(mappedBy = "parentPublication", targetEntity = JpaInCollection.class)
    private Set<InCollection> publications;

    public JpaBook() {
        publications = new HashSet<>();
    }

    public String getCdrom() {
        return cdrom;
    }

    public void setCdrom(String cdrom) {
        this.cdrom = cdrom;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    @Override
    public Set<InCollection> getPublications() {
        return publications;
    }

    @Override
    public void setPublications(Set<InCollection> publications) {
        this.publications = publications;
    }

    @Override
    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
