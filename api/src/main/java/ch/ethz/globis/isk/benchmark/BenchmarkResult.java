package ch.ethz.globis.isk.benchmark;

import ch.ethz.globis.isk.domain.Publication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Corresponds to the result of a single execution of a benchmark.
 *
 * @param <T>               The type of the result.
 */
public class BenchmarkResult<T> {

    private long duration;
    private final String name;
    private final String message;
    private final T result;

    public BenchmarkResult(T result, String message, String name) {
        this.message = message;
        this.name = name;
        this.result = result;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public T getResult() {
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BenchmarkResult{");
        sb.append("\n");
        sb.append("Benchmark: ").append(name).append('\n');
        sb.append("Duration: ").append(duration).append("\n");
        sb.append("Message: ").append(message).append('\n');
        sb.append("Result: ").append(result).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BenchmarkResult)) return false;

        BenchmarkResult result1 = (BenchmarkResult) o;

        if (getName() != null ? !getName().equals(result1.getName()) : result1.getName() != null) return false;
        if (result1.getResult() instanceof Collection) {
            if (getResult() instanceof Collection) {
                return (((Collection) result1.getResult()).size() == ((Collection) getResult()).size());
            } else {
                return false;
            }
        } else {
            return (getResult().equals(result1.getResult()));
        }
    }

    @Override
    public int hashCode() {
        int result1 = (int) (duration ^ (duration >>> 32));
        result1 = 31 * result1 + (name != null ? name.hashCode() : 0);
        result1 = 31 * result1 + (message != null ? message.hashCode() : 0);
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        return result1;
    }

    private List<String> getTitles(Collection<Publication> collection) {
        List<String> names = new ArrayList<>();
        for (Publication publication : collection) {
            names.add(publication.getTitle());
        }
        Collections.sort(names);
        return names;
    }
}