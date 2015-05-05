package ch.ethz.globis.isk.web.model;

import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Publication;

import java.util.List;

public class SearchResponse {

    private String searchString;
    private List<DTO<Person>> persons;
    private List<DTO<Publication>> publications;

    public SearchResponse() { }

    public SearchResponse(List<DTO<Person>> persons, List<DTO<Publication>> publications, String searchString) {
        this.persons = persons;
        this.publications = publications;
        this.searchString = searchString;
    }

    public void setPersons(List<DTO<Person>> persons) {
        this.persons = persons;
    }

    public void setPublications(List<DTO<Publication>> publications) {
        this.publications = publications;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public List<DTO<Person>> getPersons() {
        return persons;
    }

    public List<DTO<Publication>> getPublications() {
        return publications;
    }
}
