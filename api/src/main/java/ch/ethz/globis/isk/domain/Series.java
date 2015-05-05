package ch.ethz.globis.isk.domain;


import java.util.Set;

/**
 *  Represents a series of publications. Besides the name it also contains references to all
 *  of the publications published by in the same series.
 */
public interface Series extends DomainObject{

    public String getName();

    public void setName(String name);

    public Set<Publication> getPublications();

    public void setPublications(Set<Publication> publications);
}