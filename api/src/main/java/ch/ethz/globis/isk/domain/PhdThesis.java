package ch.ethz.globis.isk.domain;

/**
 * Represents a PHD Thesis.
 */
public interface PhdThesis extends Publication {

    public String getIsbn();

    public void setIsbn(String isbn);

    public Integer getMonth();

    public void setMonth(Integer month);

    public String getNote();

    public void setNote(String note);

    public Integer getNumber();

    public void setNumber(Integer number);

    public Publisher getPublisher();

    public void setPublisher(Publisher publisher);

    public School getSchool();

    public void setSchool(School school);

}