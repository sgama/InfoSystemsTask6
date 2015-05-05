package ch.ethz.globis.isk.benchmark;

import ch.ethz.globis.isk.service.BaseService;
import ch.ethz.globis.isk.service.TransactionManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

/**
 * Class used for running benchmarks on a domain entity.
 *
 * @param <T> The domain entity on which the benchmarks are ran.
 */
public abstract class Benchmark<T> {

    private String DIRECTORY = System.getProperty("user.home") +
                                File.separator + "ISK" + File.separator;

    /** A set of benchmark inputs*/
    private BenchmarkInputs[] arguments;

    /** The writer used for the results */
    protected BufferedWriter writer;

    /** The service used to query to domain entities*/
    protected BaseService service;

    /** The name of the benchmark task. */
    public abstract String name();

    /**
     * Executes the benchmark task internally, with the BenchmarkInputs arguments.
     *
     * @param arguments
     * @return
     */
    public abstract BenchmarkResult<T> internalRun(BenchmarkInputs arguments);

    /**
     * Writes the default arguments for this benchmark to a file. The benchmark task is run with all of these
     * arguments and execution time of the benchmark task is computed as an average overall all execution times
     * of the task with all arguments.
     *
     * @throws IOException                  If there was an error writing the arguments.
     */
    public abstract void writeDefaultArguments() throws IOException;

    protected Benchmark(BaseService service, BenchmarkInputs[] arguments) {
        this.arguments = arguments;
        this.service = service;
    }

    public Benchmark(BaseService service) {
       this.service = service;
    }

    public BaseService service() {
        return service;
    }

    /**
     * Read the default arguments for this benchmark task for a file on disk.
     *
     * The file is located in the location $HOME/ISK directory . The name of this file
     * is test_<lowercased-task-name> .
     * @return                      A list of BenchmarkInputs objects corresponding to
     *                              the set of default inputs for this task.
     */
    public List<BenchmarkInputs> readDefaultArguments() {
        List<BenchmarkInputs> benchmarkInputsList = new ArrayList<>();

        try {
            String fileName = "test_" + name().toLowerCase().replace(" ", "_");
            File directory = new File(DIRECTORY);
            if (!directory.exists()) {
                directory.mkdir();
            }
            fileName = DIRECTORY + fileName;
            File f = new File(fileName);
            if (!f.exists() || f.length() == 0) {
                writeDefaultArguments();
            }
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] stringArguments = line.split(" ");
                Object[] objectAtributes = Arrays.copyOf(stringArguments, stringArguments.length, Object[].class);
                BenchmarkInputs inputs = new BenchmarkInputs(objectAtributes);
                benchmarkInputsList.add(inputs);
            }
        } catch (IOException ie) {
            System.out.println("Failed to read input arguments.");
        }
        return benchmarkInputsList;
    }

    /**
     * Run and measure the timing of the input received as an argument.
     *
     * @param input
     * @return                  A BenchmarkResult object
     */
    public BenchmarkResult run(BenchmarkInputs input) {
        long start = System.nanoTime();
        BenchmarkResult result = internalRun(input);
        long end = System.nanoTime();
        long duration = TimeUnit.MICROSECONDS.convert(end - start, TimeUnit.NANOSECONDS);
        if (result != null) {
            result.setDuration(duration);
        }
        return result;
    }

    /**
     * Run the benchmark for a number of seconds. The arguments used will be
     * read from a file.
     * @param minRuntime            The minimum runtime of the benchmark.
     * @param warmupTime            Benchmark warmup time. Executions during this period
     *                              do not count towards the average measured execution
     *                              time.
     * @return                      The average measured execution time in microseconds.
     */
    public double run(int minRuntime, int warmupTime) {
        long targetMicros = TimeUnit.SECONDS.toMicros(minRuntime);
        long warmupMicros = TimeUnit.SECONDS.toMicros(warmupTime);
        long firstTimeAfterWarmup = 0;
        long executions = 0;
        boolean firstSecondPassed = false;

        List<BenchmarkInputs> benchmarkInputsList = readDefaultArguments();
        ListIterator<BenchmarkInputs> listIterator = benchmarkInputsList.listIterator();
        long globalStart = System.currentTimeMillis();
        long elapsedMicros;
        do {
            if (!listIterator.hasNext()) {
                listIterator = benchmarkInputsList.listIterator();
            }
            BenchmarkInputs inputs = listIterator.next();
            internalRun(inputs);
            elapsedMicros = (System.currentTimeMillis() - globalStart) * 1000;

            if (elapsedMicros < warmupMicros) {
                // we don't count the iterations in the first second.
                continue;
            }

            if (!firstSecondPassed) {
                firstTimeAfterWarmup = elapsedMicros;
                firstSecondPassed = true;
            }

            executions++;
        } while (elapsedMicros < targetMicros);

        long hotExecutionTime = elapsedMicros;
        if (firstTimeAfterWarmup < targetMicros) {
            hotExecutionTime -= firstTimeAfterWarmup;
        }
        double averageExecutionTime = (double) hotExecutionTime / (double)executions;
        return averageExecutionTime;
    }

    /**
     * Runs the benchmark task multiple times, once for each set of input set in the
     * variable arguments.
     *
     * @return                          A list of results of all the runs of the benchmark task.
     */
    public List<BenchmarkResult> run() {
        List<BenchmarkResult> results = new ArrayList<>();
        TransactionManager tm = service().getTM();
        for (BenchmarkInputs input : arguments) {
            tm.beginTransaction();
            BenchmarkResult<T> result = internalRun(input);
            tm.commitTransaction();
            results.add(result);
        }
        return results;
    }

    protected void openWriter() throws IOException {
        File directory = new File(DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
        String fileName = "test_" + name().toLowerCase().replace(" ", "_");
        writer = new BufferedWriter(new FileWriter(DIRECTORY + File.separator + fileName));
    }

    protected void closeWriter() throws IOException {
        writer.close();
    }
}