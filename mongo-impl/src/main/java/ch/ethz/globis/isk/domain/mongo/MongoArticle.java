package ch.ethz.globis.isk.domain.mongo;

import ch.ethz.globis.isk.domain.Article;
import ch.ethz.globis.isk.domain.JournalEdition;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "publication")
public class MongoArticle extends MongoPublication implements Article {

    private String cdrom;

    private String pages;

    @DBRef(lazy = true)
    private JournalEdition journalEdition;

    @PersistenceConstructor
    public MongoArticle() {
    }

    public String getCdrom() {
        return cdrom;
    }

    public void setCdrom(String cdrom) {
        this.cdrom = cdrom;
    }

    public JournalEdition getJournalEdition() {
        return journalEdition;
    }

    public void setJournalEdition(JournalEdition journalEdition) {
        this.journalEdition = journalEdition;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Article))
            return false;
        if (!super.equals(o))
            return false;
        Article that = (Article) o;
        if (getCdrom() != null ? !getCdrom().equals(that.getCdrom()) : that.getCdrom() != null)
            return false;
        if (getPages() != null ? !getPages().equals(that.getPages()) : that.getPages() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getCdrom() != null ? getCdrom().hashCode() : 0);
        result = 31 * result + (getPages() != null ? getPages().hashCode() : 0);
        return result;
    }
}
