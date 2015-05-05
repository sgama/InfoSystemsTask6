package ch.ethz.globis.isk.domain.db4o;

import ch.ethz.globis.isk.domain.MasterThesis;
import ch.ethz.globis.isk.domain.School;
import com.db4o.activation.ActivationPurpose;
import com.db4o.ta.Activatable;

public class Db4oMasterThesis extends Db4oPublication implements MasterThesis, Activatable {

    private School school;

    public Db4oMasterThesis() {
    }

    public School getSchool() {
        activate(ActivationPurpose.READ);
        return school;
    }

    public void setSchool(School school) {
        activate(ActivationPurpose.WRITE);
        this.school = school;
    }
}
