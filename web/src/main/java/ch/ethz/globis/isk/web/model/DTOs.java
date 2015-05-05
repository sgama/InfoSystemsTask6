package ch.ethz.globis.isk.web.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Utility class for converting domain objects to and from DTOs.
 */
public class DTOs {

    private static final Logger LOG = LoggerFactory.getLogger(DTOs.class);

    public static DTO one(Object entity,  Class<? extends DTO> clazz) {

        DTO current = null;
        try {
            current = clazz.newInstance().convert(entity);
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("Failed to instantiate object of class {}.", clazz.getName());
        } catch (NullPointerException ex) {
            LOG.warn("NullPointerException encountered during object conversion. Returning null.",
                    clazz.getName());
        }
        return current;
    }

    public static <T> List<DTO<T>> create(Collection<T> entities, Class<? extends DTO> clazz) {
        List<DTO<T>> set = new ArrayList<>();
        try {
            DTO<T> current;
            for (T entiy : entities) {
                current = clazz.newInstance().convert(entiy);
                set.add(current);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("Failed to instantiate object of class {}.", clazz.getName());
        } catch (NullPointerException npe) {
            LOG.warn("NullPointerException encountered during object conversion. Returning null.",
                    clazz.getName());
        }
        return set;
    }
}