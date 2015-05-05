package ch.ethz.globis.isk.persistence.jpa;

import ch.ethz.globis.isk.domain.InProceedings;
import ch.ethz.globis.isk.domain.jpa.JpaInProceedings;
import ch.ethz.globis.isk.persistence.InProceedingsDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JpaInProceedingsDao extends JpaDao<String, InProceedings> implements InProceedingsDao {

    @Override
    public InProceedings findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public List<InProceedings> findByProceedingsIdOrderByYear(String proceedingsId) {
        return queryByReferenceIdOrderByYear("InProceedings", "proceedings", proceedingsId);
    }

    @Override
    protected Class<JpaInProceedings> getStoredClass() {
        return JpaInProceedings.class;
    }

    @Override
    public InProceedings createEntity() {
        return new JpaInProceedings();
    }
}
