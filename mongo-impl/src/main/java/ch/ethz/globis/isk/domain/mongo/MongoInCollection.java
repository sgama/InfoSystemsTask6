package ch.ethz.globis.isk.domain.mongo;

import ch.ethz.globis.isk.domain.Book;
import ch.ethz.globis.isk.domain.InCollection;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "publication")
public class MongoInCollection extends MongoPublication implements InCollection {

    private String note;

    private String pages;

    @DBRef(lazy = true)
    private Book parentPublication;

    public MongoInCollection() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public Book getParentPublication() {
        return parentPublication;
    }

    public void setParentPublication(Book parentPublication) {
        this.parentPublication = parentPublication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof InCollection))
            return false;
        if (!super.equals(o))
            return false;
        InCollection that = (InCollection) o;
        if (getNote() != null ? !getNote().equals(that.getNote()) : that.getNote() != null)
            return false;
        if (getPages() != null ? !getPages().equals(that.getPages()) : that.getPages() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getNote() != null ? getNote().hashCode() : 0);
        result = 31 * result + (getPages() != null ? getPages().hashCode() : 0);
        return result;
    }
}
