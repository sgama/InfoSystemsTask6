package ch.ethz.globis.isk.domain.mongo;

import ch.ethz.globis.isk.domain.PhdThesis;
import ch.ethz.globis.isk.domain.Publisher;
import ch.ethz.globis.isk.domain.School;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "publication")
public class MongoPhdThesis extends MongoPublication implements PhdThesis {

    private Integer month;

    private String note;

    private Integer number;

    private String isbn;

    @DBRef(lazy = true)
    private Publisher publisher;

    @DBRef(lazy = true)
    private School school;

    public MongoPhdThesis() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PhdThesis))
            return false;
        if (!super.equals(o))
            return false;
        PhdThesis that = (PhdThesis) o;
        if (getIsbn() != null ? !getIsbn().equals(that.getIsbn()) : that.getIsbn() != null)
            return false;
        if (getMonth() != null ? !getMonth().equals(that.getMonth()) : that.getMonth() != null)
            return false;
        if (getNote() != null ? !getNote().equals(that.getNote()) : that.getNote() != null)
            return false;
        if (getNumber() != null ? !getNumber().equals(that.getNumber()) : that.getNumber() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getMonth() != null ? getMonth().hashCode() : 0);
        result = 31 * result + (getNote() != null ? getNote().hashCode() : 0);
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        result = 31 * result + (getIsbn() != null ? getIsbn().hashCode() : 0);
        return result;
    }
}
