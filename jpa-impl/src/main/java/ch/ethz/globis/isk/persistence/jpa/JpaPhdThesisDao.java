package ch.ethz.globis.isk.persistence.jpa;

import ch.ethz.globis.isk.domain.PhdThesis;
import ch.ethz.globis.isk.domain.jpa.JpaPhdThesis;
import ch.ethz.globis.isk.persistence.PhdThesisDao;
import ch.ethz.globis.isk.util.Filter;
import ch.ethz.globis.isk.util.Operator;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JpaPhdThesisDao extends JpaDao<String, PhdThesis> implements PhdThesisDao {

    @Override
    protected Class<JpaPhdThesis> getStoredClass() {
        return JpaPhdThesis.class;
    }

    @Override
    public PhdThesis findOneByTitle(String title) {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("title", new Filter(Operator.EQUAL, title));
        return findOneByFilter(filterMap);
    }

    @Override
    public PhdThesis createEntity() {
        return new JpaPhdThesis();
    }
}
