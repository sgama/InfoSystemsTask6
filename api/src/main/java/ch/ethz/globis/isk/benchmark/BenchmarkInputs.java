package ch.ethz.globis.isk.benchmark;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Corresponds to a single set of arguments that are used by a benchmark task.
 */
public class BenchmarkInputs extends LinkedList<Object> {

    public BenchmarkInputs(Object[] inputs) {
        super(Arrays.asList(inputs));
    }
}