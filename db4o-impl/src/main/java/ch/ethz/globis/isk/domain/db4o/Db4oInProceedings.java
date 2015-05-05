package ch.ethz.globis.isk.domain.db4o;

import ch.ethz.globis.isk.domain.InProceedings;
import ch.ethz.globis.isk.domain.Proceedings;
import com.db4o.activation.ActivationPurpose;
import com.db4o.ta.Activatable;

public class Db4oInProceedings extends Db4oPublication implements InProceedings, Activatable {

    private String note;

    private String pages;

    private Proceedings proceedings;

    public Db4oInProceedings() {
    }

    public String getNote() {
        activate(ActivationPurpose.READ);
        return note;
    }

    public void setNote(String note) {
        activate(ActivationPurpose.WRITE);
        this.note = note;
    }

    public String getPages() {
        activate(ActivationPurpose.READ);
        return pages;
    }

    public void setPages(String pages) {
        activate(ActivationPurpose.WRITE);
        this.pages = pages;
    }

    public Proceedings getProceedings() {
        activate(ActivationPurpose.READ);
        return proceedings;
    }

    @Override
    public void setProceedings(Proceedings proceedings) {
        activate(ActivationPurpose.WRITE);
        this.proceedings = proceedings;
    }
}
