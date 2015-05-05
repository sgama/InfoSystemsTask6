package ch.ethz.globis.isk.domain;

/**
 * A type of article that was published as part of a conference proceedings.
 */
public interface InProceedings extends Publication {

    public String getNote();

    public void setNote(String note);

    public String getPages();

    public void setPages(String pages);

    public Proceedings getProceedings();

    public void setProceedings(Proceedings proceedings);
}