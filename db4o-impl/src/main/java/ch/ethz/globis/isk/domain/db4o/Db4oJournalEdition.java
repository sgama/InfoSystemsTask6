package ch.ethz.globis.isk.domain.db4o;

import ch.ethz.globis.isk.domain.Article;
import ch.ethz.globis.isk.domain.Journal;
import ch.ethz.globis.isk.domain.JournalEdition;

import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.collections.ActivatableHashSet;
import com.db4o.collections.ActivatableSet;
import com.db4o.config.annotations.Indexed;
import com.db4o.ta.Activatable;

import java.util.Set;

public class Db4oJournalEdition implements JournalEdition, Activatable {

	@Indexed()
    private String id;

    private String number;

    private String volume;

    private Integer year;

    private Journal journal;

    private ActivatableSet<Article> publications;

    private transient Activator activator;

    public Db4oJournalEdition() {
        publications = new ActivatableHashSet<>();
    }

    public void addArticle(Article article) {
        activate(ActivationPurpose.WRITE);
        publications.add(article);
    }

    public Journal getJournal() {
        activate(ActivationPurpose.READ);
        return journal;
    }

    public void setJournal(Journal journal) {
        activate(ActivationPurpose.WRITE);
        this.journal = journal;
    }

    public String getNumber() {
        activate(ActivationPurpose.READ);
        return number;
    }

    public void setNumber(String number) {
        activate(ActivationPurpose.WRITE);
        this.number = number;
    }

    public Set<Article> getPublications() {
        activate(ActivationPurpose.READ);
        return publications;
    }

    public void setPublications(Set<Article> publications) {
        activate(ActivationPurpose.WRITE);
        this.publications.clear();
        this.publications.addAll(publications);
    }

    public String getVolume() {
        activate(ActivationPurpose.READ);
        return volume;
    }

    public void setVolume(String volume) {
        activate(ActivationPurpose.WRITE);
        this.volume = volume;
    }

    public Integer getYear() {
        activate(ActivationPurpose.READ);
        return year;
    }

    public void setYear(Integer year) {
        activate(ActivationPurpose.WRITE);
        this.year = year;
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
    public String toString() {
        return "JournalEdition{" + "year=" + getYear() + ", volume='" + getYear() + '\'' + ", number='" + getYear() + '\'' + ", id='" + getYear() + '\'' + '}';
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
