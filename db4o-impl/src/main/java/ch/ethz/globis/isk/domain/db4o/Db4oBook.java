package ch.ethz.globis.isk.domain.db4o;

import ch.ethz.globis.isk.domain.Book;
import ch.ethz.globis.isk.domain.InCollection;
import ch.ethz.globis.isk.domain.Publisher;
import ch.ethz.globis.isk.domain.Series;
import com.db4o.activation.ActivationPurpose;
import com.db4o.collections.ActivatableHashSet;
import com.db4o.collections.ActivatableSet;
import com.db4o.ta.Activatable;
import java.util.Set;

public class Db4oBook extends Db4oPublication implements Book, Activatable {

    private String cdrom;

    private Integer month;

    private String volume;

    private String isbn;

    private Series series;

    private Publisher publisher;

    private ActivatableSet<InCollection> publications;

    public Db4oBook() {
        publications = new ActivatableHashSet<>();
    }

    @Override
    public String getCdrom() {
        activate(ActivationPurpose.READ);
        return cdrom;
    }

    @Override
    public void setCdrom(String cdrom) {
        activate(ActivationPurpose.WRITE);
        this.cdrom = cdrom;
    }

    @Override
    public String getIsbn() {
        activate(ActivationPurpose.READ);
        return isbn;
    }

    @Override
    public void setIsbn(String isbn) {
        activate(ActivationPurpose.WRITE);
        this.isbn = isbn;
    }

    @Override
    public Integer getMonth() {
        activate(ActivationPurpose.READ);
        return month;
    }

    @Override
    public void setMonth(Integer month) {
        activate(ActivationPurpose.WRITE);
        this.month = month;
    }

    @Override
    public Set<InCollection> getPublications() {
        activate(ActivationPurpose.READ);
        return publications;
    }

    @Override
    public void setPublications(Set<InCollection> publications) {
        activate(ActivationPurpose.WRITE);
        this.publications.clear();
        this.publications.addAll(publications);
    }

    @Override
    public Publisher getPublisher() {
        activate(ActivationPurpose.READ);
        return publisher;
    }

    @Override
    public void setPublisher(Publisher publisher) {
        activate(ActivationPurpose.WRITE);
        this.publisher = publisher;
    }

    @Override
    public Series getSeries() {
        activate(ActivationPurpose.READ);
        return series;
    }

    @Override
    public void setSeries(Series series) {
        activate(ActivationPurpose.WRITE);
        this.series = series;
    }

    @Override
    public String getVolume() {
        activate(ActivationPurpose.READ);
        return volume;
    }

    @Override
    public void setVolume(String volume) {
        activate(ActivationPurpose.WRITE);
        this.volume = volume;
    }
}
