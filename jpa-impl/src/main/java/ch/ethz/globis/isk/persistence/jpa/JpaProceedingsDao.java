package ch.ethz.globis.isk.persistence.jpa;

import ch.ethz.globis.isk.domain.Proceedings;
import ch.ethz.globis.isk.domain.jpa.JpaProceedings;
import ch.ethz.globis.isk.persistence.ProceedingsDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JpaProceedingsDao extends JpaDao<String, Proceedings> implements ProceedingsDao {

    @Override
    protected Class<JpaProceedings> getStoredClass() {
        return JpaProceedings.class;
    }

    @Override
    public Proceedings createEntity() {
        return new JpaProceedings();
    }

    @Override
    public Proceedings findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }
}
