package ch.ethz.globis.isk.domain;

import java.util.Set;

/**
 * Represents a person. The person can have the role of an author
 * or of an editor for some publications.
 */
public interface Person extends DomainObject{

    public String getName();

    public void setName(String name);

    public Set<Publication> getAuthoredPublications();

    public void setAuthoredPublications(Set<Publication> authoredPublications);

    public Set<Publication> getEditedPublications();

    public void setEditedPublications(Set<Publication> editedPublications);

}
