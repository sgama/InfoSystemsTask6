package ch.ethz.globis.isk.domain;

import java.util.Set;

/**
 *  Represents a school. Besides the name it also contains references to all
 *  of the publications published by at the school.
 */
public interface School extends DomainObject{

    public String getName();

    public void setName(String name);

    public Set<Publication> getPublications();

    public void setPublications(Set<Publication> publications);

}