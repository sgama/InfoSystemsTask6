package ch.ethz.globis.isk.domain;

/**
 * Represents an article that is published in an academic journal.
 */
public interface Article extends Publication {

    public String getCdrom();

    public void setCdrom(String cdrom);

    public JournalEdition getJournalEdition();

    public void setJournalEdition(JournalEdition journalEdition);

    public String getPages();

    public void setPages(String pages);
}