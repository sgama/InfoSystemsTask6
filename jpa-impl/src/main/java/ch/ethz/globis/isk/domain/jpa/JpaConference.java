package ch.ethz.globis.isk.domain.jpa;

import ch.ethz.globis.isk.domain.Conference;
import ch.ethz.globis.isk.domain.ConferenceEdition;

import javax.persistence.*;

import org.hibernate.annotations.Index;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Conference")
@Table(name = "conference")
public class JpaConference implements Conference {

    @Id
    @Index(name="index_id_conference")
    private String id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conference", targetEntity = JpaConferenceEdition.class)
    Set<ConferenceEdition> editions;

    public JpaConference() {
        editions = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ConferenceEdition> getEditions() {
        return editions;
    }

    public void setEditions(Set<ConferenceEdition> editions) {
        this.editions = editions;
    }
}
