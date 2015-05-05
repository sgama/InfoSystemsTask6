package ch.ethz.globis.isk.domain.db4o;

import ch.ethz.globis.isk.domain.PhdThesis;
import ch.ethz.globis.isk.domain.Publisher;
import ch.ethz.globis.isk.domain.School;
import com.db4o.activation.ActivationPurpose;
import com.db4o.ta.Activatable;

public class Db4oPhdThesis extends Db4oPublication implements PhdThesis, Activatable {

    private Integer month;

    private String note;

    private Integer number;

    private String isbn;

    private Publisher publisher;

    private School school;

    public Db4oPhdThesis() {
    }

    public String getIsbn() {
        activate(ActivationPurpose.READ);
        return isbn;
    }

    public void setIsbn(String isbn) {
        activate(ActivationPurpose.WRITE);
        if (this.getIsbn() != null) {
            if (this.getNote() == null) {
                this.setNote("");
            }
            this.setNote(this.getNote() + "\nISBN updated, old value was " + this.getIsbn());
        }
        this.isbn = isbn;
        this.isbn = isbn;
    }

    public Integer getMonth() {
        activate(ActivationPurpose.READ);
        return month;
    }

    public void setMonth(Integer month) {
        activate(ActivationPurpose.WRITE);
        this.month = month;
    }

    public String getNote() {
        activate(ActivationPurpose.READ);
        return note;
    }

    public void setNote(String note) {
        activate(ActivationPurpose.WRITE);
        this.note = note;
    }

    public Integer getNumber() {
        activate(ActivationPurpose.READ);
        return number;
    }

    public void setNumber(Integer number) {
        activate(ActivationPurpose.WRITE);
        this.number = number;
    }

    public Publisher getPublisher() {
        activate(ActivationPurpose.READ);
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        activate(ActivationPurpose.WRITE);
        this.publisher = publisher;
    }

    public School getSchool() {
        activate(ActivationPurpose.READ);
        return school;
    }

    public void setSchool(School school) {
        activate(ActivationPurpose.WRITE);
        this.school = school;
    }
}
