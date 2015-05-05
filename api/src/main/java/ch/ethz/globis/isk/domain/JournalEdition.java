package ch.ethz.globis.isk.domain;

import java.util.Set;

/**
 * Represents an issue of a certain Journal. Is uniquely identified
 * by the year, number and volume attributes.
 */
public interface JournalEdition extends DomainObject {

    public void addArticle(Article article);

    public Journal getJournal();

    public void setJournal(Journal journal);

    public String getNumber();

    public void setNumber(String number);

    public Set<Article> getPublications();

    public void setPublications(Set<Article> publications);

    public String getVolume();

    public void setVolume(String volume);

    public Integer getYear();

    public void setYear(Integer year);
}