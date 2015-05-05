package ch.ethz.globis.isk.domain.jpa;

import ch.ethz.globis.isk.domain.PhdThesis;
import ch.ethz.globis.isk.domain.Publisher;
import ch.ethz.globis.isk.domain.School;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity(name = "PhdThesis")
public class JpaPhdThesis extends JpaPublication implements PhdThesis {

    private Integer month;

    private String note;

    private Integer number;

    private String isbn;

    @ManyToOne(targetEntity = JpaPublisher.class)
    private Publisher publisher;

    @ManyToOne(targetEntity = JpaSchool.class)
    private School school;

    public JpaPhdThesis() {
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
