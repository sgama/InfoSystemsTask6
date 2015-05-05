package ch.ethz.globis.isk.domain.mongo;

import ch.ethz.globis.isk.domain.Book;
import ch.ethz.globis.isk.domain.InCollection;
import ch.ethz.globis.isk.domain.Publisher;
import ch.ethz.globis.isk.domain.Series;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "publication")
public class MongoBook extends MongoPublication implements Book {

    private String cdrom;

    private Integer month;

    private String volume;

    private String isbn;

    @DBRef(lazy = true)
    private Series series;

    @DBRef(lazy = true)
    private Publisher publisher;

    @DBRef(lazy = true)
    private Set<InCollection> publications;

    @PersistenceConstructor
    public MongoBook() {
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

    public Set<InCollection> getPublications() {
        return publications;
    }

    public void setPublications(Set<InCollection> publications) {
        this.publications = publications;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Book))
            return false;
        if (!super.equals(o))
            return false;
        Book mongoBook = (Book) o;
        if (getCdrom() != null ? !getCdrom().equals(mongoBook.getCdrom()) : mongoBook.getCdrom() != null)
            return false;
        if (getIsbn() != null ? !getIsbn().equals(mongoBook.getIsbn()) : mongoBook.getIsbn() != null)
            return false;
        if (getMonth() != null ? !getMonth().equals(mongoBook.getMonth()) : mongoBook.getMonth() != null)
            return false;
        if (getVolume() != null ? !getVolume().equals(mongoBook.getVolume()) : mongoBook.getVolume() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getCdrom() != null ? getCdrom().hashCode() : 0);
        result = 31 * result + (getMonth() != null ? getMonth().hashCode() : 0);
        result = 31 * result + (getVolume() != null ? getVolume().hashCode() : 0);
        result = 31 * result + (getIsbn() != null ? getIsbn().hashCode() : 0);
        return result;
    }
}
