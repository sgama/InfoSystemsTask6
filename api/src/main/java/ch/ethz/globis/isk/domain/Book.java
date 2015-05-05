package ch.ethz.globis.isk.domain;


import java.util.Set;

/**
 * Represents a book. The book, from the academical context, usually contains
 * a number of 'child' publications.
 */
public interface Book extends Publication {

    public String getCdrom();

    public void setCdrom(String cdrom);

    public String getIsbn();

    public void setIsbn(String isbn);

    public Integer getMonth();

    public void setMonth(Integer month);

    public Set<InCollection> getPublications();

    public void setPublications(Set<InCollection> publications);

    public Publisher getPublisher();

    public void setPublisher(Publisher publisher);

    public Series getSeries();

    public void setSeries(Series series);

    public String getVolume();

    public void setVolume(String volume);

}