package ch.ethz.globis.isk.web.model;


import ch.ethz.globis.isk.domain.*;

@Deprecated
public class PublicationDtoUtil {

    public static PublicationDto fromPublication(Publication publication) {
        if (publication instanceof Article) {
            return new PublicationDto((Article) publication);
        } else if (publication instanceof Book) {
            return new PublicationDto((Book) publication);
        } else if (publication instanceof InCollection) {
            return new PublicationDto((InCollection) publication);
        } else if (publication instanceof InProceedings) {
            return new PublicationDto((InProceedings) publication);
        } else if (publication instanceof MasterThesis) {
            return new PublicationDto((MasterThesis) publication);
        } else if (publication instanceof PhdThesis) {
            return new PublicationDto((PhdThesis) publication);
        } else if (publication instanceof Proceedings) {
            return new PublicationDto((Proceedings) publication);
        } else {
            return null;
        }
    }
}