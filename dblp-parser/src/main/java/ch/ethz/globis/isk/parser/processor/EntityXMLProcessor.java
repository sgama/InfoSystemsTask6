package ch.ethz.globis.isk.parser.processor;


import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processor class for a certain type of Publication.
 * @param <T>               The type of Publication
 */
public abstract class EntityXMLProcessor<T extends Publication> {

    protected static final Logger LOG = LoggerFactory.getLogger(EntityXMLProcessor.class);

    /** Service corresonding to this type of publication. */
    protected BaseService<String, T> service;

    /** The Publication instance that is currently processed.*/
    protected T instance;

    /** The Entity cache. */
    protected EntityCache cache;

    private static int counter = 0;

    protected EntityXMLProcessor(BaseService<String, T> service, EntityCache cache) {
        this.service = service;
        this.cache = cache;
        this.instance = service.createEntity();
    }

    /**
     * Set the value with the corresponding key on the currently processed
     * publication instance.
     * @param key                           The name of the attribute the value corresponds for.
     * @param value                         The value corresponding to the attribute.
     */
    public abstract void setData(String key, String value);

    /**
     * Return the currently processed instance. This instance is also added to the cache.
     * @return                              The currently processed instance.
     */
    public T build() {
        cache.put(instance.getId(), instance);

        counter++;
        if (counter % 100000 == 0) {
            LOG.info("Created {} publications so far.", counter);
        }
        return instance;
    }

    /**
     * Clears the currently processed Publication instance and replaces it by a new one.
     */
    public void clear() {
        LOG.debug("Clearing entity processor {}", getClass());
        instance = service.createEntity();
    }
}