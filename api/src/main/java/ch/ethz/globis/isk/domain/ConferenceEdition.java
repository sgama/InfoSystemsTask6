package ch.ethz.globis.isk.domain;

/**
 * Represents one edition of a conference. It contains a reference
 * to the proceedings published after the conference edition.
 */
public interface ConferenceEdition extends DomainObject{

    public Conference getConference();

    public void setConference(Conference conference);

    public Integer getYear();

    public void setYear(Integer year);

    public Proceedings getProceedings();

    public void setProceedings(Proceedings proceedings);
}