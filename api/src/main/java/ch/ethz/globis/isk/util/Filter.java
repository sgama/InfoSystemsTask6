package ch.ethz.globis.isk.util;

/**
 * Represents a filter corresponding to a certain field.
 */
public class Filter {

    /** The operating used for the filter */
    private Operator operator;

    /** The value on which the filter constrains the objects. */
    private Object value;

    public Filter(Operator operator, Object value) {
        this.operator = operator;
        this.value = value;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
