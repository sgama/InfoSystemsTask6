package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.ConferenceEdition;

import java.util.List;

public interface ConferenceEditionDao extends Dao<String, ConferenceEdition> {

    public List<ConferenceEdition> findByConferenceOrderedByYear(String conferenceId);
}
