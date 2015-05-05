package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Conference;
import ch.ethz.globis.isk.domain.ConferenceEdition;
import ch.ethz.globis.isk.util.OrderFilter;

import java.util.List;

public interface ConferenceService extends BaseService<String, Conference> {

    public Conference findOneByName(String name);

    public Iterable<Conference> findByName(String name, List<OrderFilter> orderConditions, int start, int size);

    public long countByName(String name);

    public ConferenceEdition findByEdition(String conferenceId, String editionId);

    public Long countAuthorsForConference(String conferenceId);

    public Long countPublicationsForConference(String conferenceId);
}