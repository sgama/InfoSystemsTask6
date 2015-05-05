package ch.ethz.globis.isk.domain.jpa;

import ch.ethz.globis.isk.domain.MasterThesis;
import ch.ethz.globis.isk.domain.School;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity(name = "MasterThesis")
public class JpaMasterThesis extends JpaPublication implements MasterThesis {

    @ManyToOne(targetEntity = JpaSchool.class)
    private School school;

    public JpaMasterThesis() {
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
