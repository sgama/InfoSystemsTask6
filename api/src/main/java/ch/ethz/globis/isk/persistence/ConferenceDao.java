package ch.ethz.globis.isk.persistence;

import ch.ethz.globis.isk.domain.Conference;
import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Publication;

import java.util.Set;

public interface ConferenceDao extends Dao<String, Conference> {

    public Conference findOneByName(String name);

    public Long countAuthorsForConference(String conferenceName);

    public Set<Person> findAuthorsForConference(String conferenceName);

    public Set<Publication> findPublicationsForConference(String conferenceName);

    public Long countPublicationsForConference(String conferenceName);
}
