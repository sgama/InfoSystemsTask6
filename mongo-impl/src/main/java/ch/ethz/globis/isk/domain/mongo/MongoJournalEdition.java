package ch.ethz.globis.isk.domain.mongo;

import ch.ethz.globis.isk.domain.Article;
import ch.ethz.globis.isk.domain.Journal;
import ch.ethz.globis.isk.domain.JournalEdition;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "journalEdition")
public class MongoJournalEdition implements JournalEdition {

    @Field("_id")
    @Id
    @Indexed()
    private String id;

    private String number;

    private String volume;

    private Integer year;

    @DBRef(lazy = true)
    private Journal journal;

    @DBRef(lazy = true)
    private Set<Article> publications;

    public MongoJournalEdition() {
        publications = new HashSet<>();
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void addArticle(Article article) {
        publications.add(article);
    }

    public Set<Article> getPublications() {
        return publications;
    }

    public void setPublications(Set<Article> publications) {
        this.publications = publications;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "JournalEdition{" + "year=" + getYear() + ", volume='" + getYear() + '\'' + ", number='" + getYear() + '\'' + ", id='" + getYear() + '\'' + '}';
    }

    /*
        Equals and hashCode don't check references to other domain objects to avoid
        infinite loops.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof JournalEdition))
            return false;
        JournalEdition that = (JournalEdition) o;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null)
            return false;
        if (getNumber() != null ? !getNumber().equals(that.getNumber()) : that.getNumber() != null)
            return false;
        if (getVolume() != null ? !getVolume().equals(that.getVolume()) : that.getVolume() != null)
            return false;
        if (getYear() != null ? !getYear().equals(that.getYear()) : that.getYear() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        result = 31 * result + (getVolume() != null ? getVolume().hashCode() : 0);
        result = 31 * result + (getYear() != null ? getYear().hashCode() : 0);
        return result;
    }
}
