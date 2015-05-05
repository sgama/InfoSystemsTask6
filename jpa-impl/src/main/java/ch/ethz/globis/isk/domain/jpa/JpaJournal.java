package ch.ethz.globis.isk.domain.jpa;

import ch.ethz.globis.isk.domain.Journal;
import ch.ethz.globis.isk.domain.JournalEdition;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Journal")
@Table(name = "journal")
public class JpaJournal implements Journal {

    @Id
    @Index(name="index_id_journal")
    private String id;

    private String name;

    @OneToMany(mappedBy = "journal", targetEntity = JpaJournalEdition.class)
    private Set<JournalEdition> editions;

    public JpaJournal() {
        this.editions = new HashSet<>();
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

    public Set<JournalEdition> getEditions() {
        return editions;
    }

    public void addEdition(JournalEdition edition) {
        editions.add(edition);
    }

    public void setEditions(Set<JournalEdition> editions) {
        this.editions = editions;
    }
}
