package ch.ethz.globis.isk.web.model;

/**
 * Marker interface for a data transfer object.
 */
public abstract class DTO<T> {

    public abstract DTO<T> convert(T entity);
}
