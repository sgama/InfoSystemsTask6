package ch.ethz.globis.isk.domain.db4o;

import ch.ethz.globis.isk.domain.Article;
import ch.ethz.globis.isk.domain.JournalEdition;
import com.db4o.activation.ActivationPurpose;
import com.db4o.ta.Activatable;

public class Db4oArticle extends Db4oPublication implements Article, Activatable {

    private String cdrom;

    private String pages;

    private JournalEdition journalEdition;

    public Db4oArticle() {
    }

    @Override
    public String getCdrom() {
        activate(ActivationPurpose.READ);
        return cdrom;
    }

    public void setCdrom(String cdrom) {
        activate(ActivationPurpose.WRITE);
        this.cdrom = cdrom;
    }

    public JournalEdition getJournalEdition() {
        activate(ActivationPurpose.READ);
        return journalEdition;
    }

    public void setJournalEdition(JournalEdition journalEdition) {
        activate(ActivationPurpose.WRITE);
        this.journalEdition = journalEdition;
    }

    public String getPages() {
        activate(ActivationPurpose.READ);
        return pages;
    }

    public void setPages(String pages) {
        activate(ActivationPurpose.WRITE);
        this.pages = pages;
    }
}
