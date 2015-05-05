package ch.ethz.globis.isk.util;

/**
 * Represents an ordering by a single field.
 */
public class OrderFilter {

    /** The order direction, can be ascending or descending. */
    private Order order;

    /** The name of the field. */
    private String field;

    public OrderFilter(String field, Order order) {
        this.field = field;
        this.order = order;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
