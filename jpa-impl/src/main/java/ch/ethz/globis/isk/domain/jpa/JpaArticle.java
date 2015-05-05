package ch.ethz.globis.isk.domain.jpa;

import ch.ethz.globis.isk.domain.Article;
import ch.ethz.globis.isk.domain.JournalEdition;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "Article")
public class JpaArticle extends JpaPublication implements Article {

    private String cdrom;

    private String pages;

    @ManyToOne(targetEntity = JpaJournalEdition.class)
    @JoinColumn(name = "journal_edition_id")
    private JournalEdition journalEdition;

    public JpaArticle() {
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
}
