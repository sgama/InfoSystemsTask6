package ch.ethz.globis.isk.domain;

import java.util.Set;

/**
 * Defines the base state for a publication. Is inherited by all specialized
 * types of publications.
 */
public interface Publication extends DomainObject {

    public String getTitle();

    public void setTitle(String title);

    public Set<Person> getAuthors();

    public void setAuthors(Set<Person> authors);

    public Set<Person> getEditors();

    public void setEditors(Set<Person> editors);

    public Integer getYear();

    public void setYear(Integer year);

    public String getElectronicEdition();

    public void setElectronicEdition(String electronicEdition);
}