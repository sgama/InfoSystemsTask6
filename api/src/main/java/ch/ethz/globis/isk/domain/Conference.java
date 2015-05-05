package ch.ethz.globis.isk.domain;

import java.util.Set;

/**
 * Represents a certain conference. A conference has more conference editions,
 * usually one edition per year.
 */
public interface Conference extends DomainObject{

    public String getName();

    public void setName(String name);

    public Set<ConferenceEdition> getEditions();

    public void setEditions(Set<ConferenceEdition> editions);
}