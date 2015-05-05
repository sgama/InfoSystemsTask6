package ch.ethz.globis.isk.domain.jpa;

import ch.ethz.globis.isk.domain.Article;
import ch.ethz.globis.isk.domain.Journal;
import ch.ethz.globis.isk.domain.JournalEdition;

import javax.persistence.*;

import org.hibernate.annotations.Index;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "JournalEdition")
@Table(name = "journal_edition")
public class JpaJournalEdition implements JournalEdition {

    @Id
    @Index(name="index_id_journal_edition")
    private String id;

    private String number;

    private String volume;

    private Integer year;

    @ManyToOne(targetEntity = JpaJournal.class)
    @JoinColumn(name = "journal_id")
    private Journal journal;

    @OneToMany(mappedBy = "journalEdition", targetEntity = JpaArticle.class)
    private Set<Article> publications;

    public JpaJournalEdition() {
        publications = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addArticle(Article publication) {
        publications.add(publication);
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

    @Override
    public String toString() {
        return "JournalEdition{" + "year=" + getYear() + ", volume='" + getYear() + '\'' + ", number='" + getYear() + '\'' + ", id='" + getYear() + '\'' + '}';
    }
}
