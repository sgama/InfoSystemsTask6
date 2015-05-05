package ch.ethz.globis.isk.domain.jpa;

import ch.ethz.globis.isk.domain.Conference;
import ch.ethz.globis.isk.domain.ConferenceEdition;
import ch.ethz.globis.isk.domain.Proceedings;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;

import javax.persistence.*;

@Entity(name = "ConferenceEdition")
@Table(name = "conference_edition")
public class JpaConferenceEdition implements ConferenceEdition {

    @Id
    @Index(name="index_id_conference_edition")
    private String id;

    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = JpaConference.class)
    @JoinColumn(name = "conference_id")
    private Conference conference;

    @OneToOne(mappedBy = "conferenceEdition", targetEntity = JpaProceedings.class)
    @Fetch(FetchMode.SELECT)
    private Proceedings proceedings;

    public JpaConferenceEdition() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public Proceedings getProceedings() {
        return proceedings;
    }

    public void setProceedings(Proceedings proceedings) {
        this.proceedings = proceedings;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "ConferenceEdition{" + "id='" + getId() + '\'' + ", year=" + getYear() + '}';
    }
}
