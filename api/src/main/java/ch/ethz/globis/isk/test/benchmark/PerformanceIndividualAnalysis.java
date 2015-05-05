package ch.ethz.globis.isk.test.benchmark;

import ch.ethz.globis.isk.benchmark.Benchmark;
import ch.ethz.globis.isk.benchmark.Benchmarks;
import ch.ethz.globis.isk.service.ConferenceService;
import ch.ethz.globis.isk.service.PersonService;
import ch.ethz.globis.isk.service.PublicationService;
import org.junit.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PerformanceIndividualAnalysis {

	private static final int WARMUP_TIME_IN_SECONDS = 1;

	/* Each operation will be run repeatedly until the overall
	 * execution time is greater than this value.
	 * This value needs to be greater than WARMUP_TIME_IN_SECONDS.
	 */
	private static final int MIN_RUNTIME_IN_SECONDS = 10;

	/* Results will be written to this file.
	 * Running the tests again overwrites the file's contents. 
	 */
	private static final String OUTFILE = "results";
	private static final String OUTFILE_EXT = ".txt";

    private static final String DIRECTORY = System.getProperty("user.home") +
            File.separator + "ISK" + File.separator;

    private ConferenceService conferenceService;
    private PersonService personService;
    private PublicationService publicationService;

	private static BufferedWriter writer;

    @BeforeClass
    public static void openFile() throws IOException {
        Assert.assertTrue(WARMUP_TIME_IN_SECONDS < MIN_RUNTIME_IN_SECONDS);
    	File directory = new File(DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File file = new File(DIRECTORY + OUTFILE + System.currentTimeMillis() + OUTFILE_EXT);
        if (!file.exists()) {
            file.createNewFile();
        }
    	writer = new BufferedWriter(new FileWriter(file));
    	writer.write("Testname\t");
    	writer.newLine();
    }
    
    @AfterClass
    public static void closeFile() throws IOException {
    	writer.flush();
    	writer.close();
    }

    @Before
    public void setUp() {
        publicationService.setUseCache(false);
    }

    @Test
    public void testFindWithFilterBenchmark() throws IOException {
        Benchmark benchmark = new Benchmarks.FindWithFilterBenchmark(publicationService);
        writer.write("Find with filter" + '\t');
        runBenchmark(benchmark);
        writer.newLine();
    }

    @Test
    public void testFindOrderedByBenchmark() throws IOException {
        Benchmark benchmark = new Benchmarks.FindOrderedByBenchmark(publicationService);
        writer.write("Find ordered by" + '\t');
        runBenchmark(benchmark);
        writer.newLine();
    }

    @Test
    public void testFindPublicationById() throws IOException {
        Benchmark benchmark = new Benchmarks.FindPublicationById(publicationService);
        writer.write("Find publication by id" + '\t');
        runBenchmark(benchmark);
        writer.newLine();
    }

    @Test
    public void testCoauthorsBenchmark() throws IOException {
        Benchmark benchmark = new Benchmarks.CoauthorsBenchmark(personService);
        writer.write("Find co-authors" + '\t');
        runBenchmark(benchmark);
        writer.newLine();
    }

    @Test
    public void testShortestPathBenchmark() throws IOException {
        Benchmark benchmark = new Benchmarks.ShortestPathBenchmark(personService);
        writer.write("Find author distance" + '\t');
        runBenchmark(benchmark);
        writer.newLine();
    }

    @Test
    public void testCountAllAuthorsForConferenceBenchmark() throws IOException {
        Benchmark benchmark = new Benchmarks.CountAllAuthorsForConferenceBenchmark(conferenceService);
        writer.write("Count all authors for a conference" + '\t');
        runBenchmark(benchmark);
        writer.newLine();
    }

    @Test
    public void testCountAllPublicationsForConferenceBenchmark() throws IOException {
        Benchmark benchmark = new Benchmarks.CountAllPublicationsForConferenceBenchmark(conferenceService);
        writer.write("Count all publications for a conference" + '\t');
        runBenchmark(benchmark);
        writer.newLine();
    }

    @Test
    public void testCountPublicationsForYearIntervalBenchmark() throws IOException {
        Benchmark benchmark = new Benchmarks.CountPublicationsForYearIntervalBenchmark(publicationService);
        writer.write("Count publications by year interval" + '\t');
        runBenchmark(benchmark);
        writer.newLine();
    }

    @Test
    public void testAverageAuthorsForPublicationBenchmark() throws IOException {
        Benchmark benchmark = new Benchmarks.AverageAuthorsForPublicationBenchmark(publicationService);
        writer.write("Average authors for publication" + '\t');
        runBenchmark(benchmark);
        writer.newLine();
    }

    private void runBenchmark(Benchmark benchmark) throws IOException {
        double averageExecutionTime = benchmark.run(MIN_RUNTIME_IN_SECONDS, WARMUP_TIME_IN_SECONDS);
        averageExecutionTime  = averageExecutionTime / 1000.0;
        writer.write(Double.toString(averageExecutionTime) + '\t');
    }

    public void setConferenceService(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public void setPublicationService(PublicationService publicationService) {
        this.publicationService = publicationService;
    }
}