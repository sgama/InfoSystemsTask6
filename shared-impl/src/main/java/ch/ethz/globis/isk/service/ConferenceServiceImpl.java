package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.Conference;
import ch.ethz.globis.isk.domain.ConferenceEdition;
import ch.ethz.globis.isk.persistence.ConferenceDao;
import ch.ethz.globis.isk.persistence.Dao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import ch.ethz.globis.isk.util.OrderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConferenceServiceImpl extends BaseServiceImpl<String, Conference> implements ConferenceService {

    private ConferenceDao conferenceDao;

    @Autowired
    protected ConferenceServiceImpl(ConferenceDao conferenceDao) {
        this.conferenceDao = conferenceDao;
    }

    public Conference findOneByName(String name) {
        return conferenceDao.findOneByName(name);
    }

    @Override
    public Dao<String, Conference> dao() {
        return conferenceDao;
    }

    public Iterable<Conference> findByName(String name, List<OrderFilter> orderConditions, int start, int size) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.STRING_MATCH, name));

        return conferenceDao.findAllByFilter(filterMap, orderConditions, start, size);
    }

    public long countByName(String name) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("name", new Filter(Operator.STRING_MATCH, name));

        return conferenceDao.countAllByFilter(filterMap);
    }

    public ConferenceEdition findByEdition(String conferenceId, String editionId) {
        Conference conference = conferenceDao.findOne(conferenceId);
        for (ConferenceEdition edition: conference.getEditions()) {
            if (edition.getId().equals(editionId)) {
                return edition;
            }
        }
        return null;
    }

    public Long countAuthorsForConference(String conferenceId) {
        return conferenceDao.countAuthorsForConference(conferenceId);
    }

    public Long countPublicationsForConference(String conferenceId) {
        return conferenceDao.countPublicationsForConference(conferenceId);
    }
}