package ch.ethz.globis.isk.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;

@Component
public class JpaTransactionManager extends CacheAwareTransactionManager {

    @Autowired
    @Qualifier("entityManager")
    private EntityManager em;

    @Override
    public void commit() {
        em.getTransaction().commit();
    }

    @Override
    public void rollback() {
        em.getTransaction().rollback();
    }

    @Override
    public void begin() {
        em.getTransaction().begin();
    }
}
