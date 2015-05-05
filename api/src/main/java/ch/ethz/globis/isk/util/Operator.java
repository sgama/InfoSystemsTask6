package ch.ethz.globis.isk.util;

/**
 * Types of operators for the filters.
 *
 * The equal operator will trigger filter equality, i.e. when
 * searching for all publications in a certain year.
 * The string match operator will trigger a string matching. For example,
 * this means if the object of a filter is a substring of a fields string,
 * the object passes the filter.
 */
public enum Operator {
    EQUAL, STRING_MATCH;
}
