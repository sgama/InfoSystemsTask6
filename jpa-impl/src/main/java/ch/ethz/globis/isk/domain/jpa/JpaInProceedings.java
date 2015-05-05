package ch.ethz.globis.isk.domain.jpa;

import ch.ethz.globis.isk.domain.InProceedings;
import ch.ethz.globis.isk.domain.Proceedings;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "InProceedings")
public class JpaInProceedings extends JpaPublication implements InProceedings {

    private String note;

    private String pages;

    @ManyToOne(targetEntity = JpaProceedings.class)
    @JoinColumn(name = "proceedings_id")
    private Proceedings proceedings;

    public JpaInProceedings() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public Proceedings getProceedings() {
        return proceedings;
    }

    @Override
    public void setProceedings(Proceedings proceedings) {
        this.proceedings = proceedings;
    }
}
