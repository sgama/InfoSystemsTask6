package ch.ethz.globis.isk.domain.db4o;

import ch.ethz.globis.isk.domain.Book;
import ch.ethz.globis.isk.domain.InCollection;
import com.db4o.activation.ActivationPurpose;
import com.db4o.ta.Activatable;

public class Db4oInCollection extends Db4oPublication implements InCollection, Activatable {

    private String note;

    private String pages;

    private Book parentPublication;

    public Db4oInCollection() {
    }

    public String getNote() {
        activate(ActivationPurpose.READ);
        return note;
    }

    public void setNote(String note) {
        activate(ActivationPurpose.WRITE);
        this.note = note;
    }

    public String getPages() {
        activate(ActivationPurpose.READ);
        return pages;
    }

    public void setPages(String pages) {
        activate(ActivationPurpose.WRITE);
        this.pages = pages;
    }

    public Book getParentPublication() {
        activate(ActivationPurpose.READ);
        return parentPublication;
    }

    public void setParentPublication(Book book) {
        activate(ActivationPurpose.WRITE);
        this.parentPublication = book;
    }
}
