package ch.ethz.globis.isk.benchmark;

import ch.ethz.globis.isk.service.ConferenceService;
import ch.ethz.globis.isk.service.PersonService;
import ch.ethz.globis.isk.service.PublicationService;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *  A suite object that can be used to run a set of the benchmark tasks defined
 *  in the Benchmarks class.
 */
public class BenchmarkSuite {

    private static final int WARMUP_TIME_IN_SECONDS = 1;

    /* Each operation will be run repeatedly until the overall
     * execution time is greater than this value.
     * This value needs to be greater than WARMUP_TIME_IN_SECONDS.
     */
    private static final int MIN_RUNTIME_IN_SECONDS = 10;

    private PersonService personService;

    private ConferenceService conferenceService;

    private PublicationService publicationService;

    /**
     * Run all the benchmark tasks and return the results in a map.
     * @return                          A map containing the benchmark task names as key and
     *                                  the average execution times as objects.
     */
    public Map<String, String> runBenchmarks() {
        List<Benchmark<?>> benchmarks = Arrays.asList(new Benchmarks.FindWithFilterBenchmark(publicationService), new Benchmarks.FindOrderedByBenchmark(publicationService), new Benchmarks.FindPublicationById(publicationService), new Benchmarks.CoauthorsBenchmark(personService), new Benchmarks.ShortestPathBenchmark(personService), new Benchmarks.CountAllAuthorsForConferenceBenchmark(conferenceService), new Benchmarks.CountAllPublicationsForConferenceBenchmark(conferenceService), new Benchmarks.CountPublicationsForYearIntervalBenchmark(publicationService), new Benchmarks.AverageAuthorsForPublicationBenchmark(publicationService));
        assert benchmarks.size() > 0;
        Map<String, String> results = new LinkedHashMap<>();
        for (Benchmark benchmark : benchmarks) {
            results.put(benchmark.name(), runAndPrintResult(benchmark));
        }
        return results;
    }

    private String runAndPrintResult(Benchmark benchmark) {
        return runBenchmark(benchmark);
    }

    private String runBenchmark(Benchmark benchmark) {
        double averageExecutionTime = benchmark.run(MIN_RUNTIME_IN_SECONDS, WARMUP_TIME_IN_SECONDS);
        averageExecutionTime = TimeUnit.MICROSECONDS.toMillis((long) averageExecutionTime);
        return Double.toString(averageExecutionTime) + '\t';
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public void setConferenceService(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    public void setPublicationService(PublicationService publicationService) {
        this.publicationService = publicationService;
    }
}
