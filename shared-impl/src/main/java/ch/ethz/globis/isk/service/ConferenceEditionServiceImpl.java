package ch.ethz.globis.isk.service;

import ch.ethz.globis.isk.domain.ConferenceEdition;
import ch.ethz.globis.isk.persistence.ConferenceEditionDao;
import ch.ethz.globis.isk.persistence.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConferenceEditionServiceImpl extends BaseServiceImpl<String, ConferenceEdition> implements ConferenceEditionService {

    private ConferenceEditionDao conferenceEditionDao;

    @Autowired
    public ConferenceEditionServiceImpl(ConferenceEditionDao conferenceEditionDao) {
        this.conferenceEditionDao = conferenceEditionDao;
    }

    @Override
    public Dao<String, ConferenceEdition> dao() {
        return conferenceEditionDao;
    }


    @Override
    public List<ConferenceEdition> findByConferenceOrderedByYear(String conferenceId) {
        return conferenceEditionDao.findByConferenceOrderedByYear(conferenceId);
    }
}