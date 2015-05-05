package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.ConferenceEdition;

import java.util.List;

public interface ConferenceEditionService extends BaseService<String, ConferenceEdition> {

    public List<ConferenceEdition> findByConferenceOrderedByYear(String conferenceId);
}