package ch.ethz.globis.isk.test.benchmark;

import ch.ethz.globis.isk.benchmark.Benchmark;
import ch.ethz.globis.isk.benchmark.BenchmarkInputs;
import ch.ethz.globis.isk.benchmark.BenchmarkResult;
import ch.ethz.globis.isk.domain.Article;
import ch.ethz.globis.isk.domain.Conference;
import ch.ethz.globis.isk.domain.InProceedings;
import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.service.ConferenceService;
import ch.ethz.globis.isk.service.PersonService;
import ch.ethz.globis.isk.service.PublicationService;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static ch.ethz.globis.isk.benchmark.Benchmarks.*;
import static org.junit.Assert.*;

public class PerformanceOperationsTest {

    private ConferenceService conferenceService;
    private PersonService personService;
    private PublicationService publicationService;

    @Before
    public void setUp() {
        publicationService.setUseCache(false);
    }

    @Test
    public void testFindWithFilterBenchmark() {
        Benchmark benchmark = newFindWithFilterBenchmark(
                inputCases(
                        inputCase("towards", "0", "15")
                )
        );
        List<BenchmarkResult> results = benchmark.run();
        assertNotNull(results);
        assertEquals("Wrong result size", 1, results.size());
        BenchmarkResult benchmarkResult = results.get(0);
        assertTrue(benchmarkResult.getResult() instanceof Collection);
        List result = (List) benchmarkResult.getResult();
        assertEquals(15, result.size());
    }

    @Test
    public void testFindOrderedByBenchmark() {
        Benchmark benchmark = newFindOrderedByBenchmark(
                inputCases(
                        inputCase("0", "15")
                )
        );
        List<BenchmarkResult> results = benchmark.run();
        assertNotNull(results);
        assertEquals("Wrong result size", 1, results.size());
        BenchmarkResult benchmarkResult = results.get(0);
        assertTrue(benchmarkResult.getResult() instanceof Collection);
        List result = (List) benchmarkResult.getResult();
        assertEquals(15, result.size());
    }

    @Test
    public void testFindPublicationById() {
        Benchmark benchmark = newFindPublicationById(
                inputCases(
                        inputCase("journals-jsyml-Doss45"),
                        inputCase("conf-dac-RowanL70")
                )
        );
        List<BenchmarkResult> results = benchmark.run();
        assertNotNull(results);
        assertEquals("Wrong result size", 2, results.size());
        BenchmarkResult benchmarkResult = results.get(0);
        Article article = (Article) benchmarkResult.getResult();
        assertEquals("Note on Two Theorems of Mostowski.", article.getTitle());
        benchmarkResult = results.get(1);
        InProceedings inProceedings = (InProceedings) benchmarkResult.getResult();
        assertEquals("Interactive on-line computing in structural design.", inProceedings.getTitle());
    }

    @Test
    public void testCoauthorsBenchmark() {
        Person first = personService.findOneByName("Moira C. Norrie");
        Person second = personService.findOneByName("M. G. Hartley");

        Benchmark benchmark = newCoauthorsBenchmark(
                inputCases(
                        inputCase(first.getId()),
                        inputCase(second.getId())
                )
        );

        List<BenchmarkResult> results = benchmark.run();
        BenchmarkResult benchmarkResult = results.get(0);
        Set<Person> coauthors = (Set<Person>) benchmarkResult.getResult();
        assertEquals(18, coauthors.size());

        benchmarkResult = results.get(1);
        coauthors = (Set<Person>) benchmarkResult.getResult();
        assertEquals(1, coauthors.size());
    }

    @Test
    public void testShortestPathBenchmark() {
    	checkDistance("Moira C. Norrie", "Moira C. Norrie", 0);
    	checkDistance("Moira C. Norrie", "P. Hepp", 1);
    	checkDistance("Moira C. Norrie", "Norman W. Paton", 2);
    	checkDistance("Moira C. Norrie", "Bill Kalsow", 3);
    }
    
    private void checkDistance(String p1, String p2, int expectedDistance) {
        Person first = personService.findOneByName(p1);
        Person second = personService.findOneByName(p2);
        Benchmark benchmark = newShortestPathBenchmark(
                inputCases(
                        inputCase(first.getId(), second.getId())
                )
        );
        List<BenchmarkResult> results = benchmark.run();
        assertNotNull(results);
        assertEquals("Wrong result size", 1, results.size());;
        BenchmarkResult benchmarkResult = results.get(0);
        long distance = (Long) benchmarkResult.getResult();
        assertEquals((long) expectedDistance, distance);
    }

    @Test
    public void testCountAllAuthorsForConferenceBenchmark() {
        Conference conference = conferenceService.findOne("conf-ACMse");
        Benchmark benchmark = newCountAllAuthorsForConferenceBenchmark(
                inputCases( inputCase(conference.getId()))
        );
        List<BenchmarkResult> results = benchmark.run();
        assertNotNull(results);
        assertEquals("Wrong result size", 1, results.size());;
        BenchmarkResult benchmarkResult = results.get(0);
        Long distance = (Long) benchmarkResult.getResult();
        assertEquals((Long) 321L, distance);
    }

    @Test
    public void testCountAllPublicationsForConferenceBenchmark() {
        Conference conference = conferenceService.findOne("conf-acm");
        Benchmark benchmark = newCountAllPublicationsForConferenceBenchmark(
                inputCases( inputCase(conference.getId()))
        );
        List<BenchmarkResult> results = benchmark.run();
        assertNotNull(results);
        assertEquals("Wrong result size", 1, results.size());;
        BenchmarkResult benchmarkResult = results.get(0);
        Long distance = (Long) benchmarkResult.getResult();
        assertEquals((Long) 929L, distance);

    }

    @Test
    public void testCountPublicationsForYearIntervalBenchmark() {
        Benchmark benchmark = newCountPublicationsForYearIntervalBenchmark(
                inputCases(
                        inputCase("1950", "1975")
                )
        );
        List<BenchmarkResult> results = benchmark.run();
        assertNotNull(results);
        assertEquals("Wrong result size", 1, results.size());;
        BenchmarkResult benchmarkResult = results.get(0);
        TreeMap<Long, Long> map = (TreeMap<Long, Long>) benchmarkResult.getResult();
        assertEquals(yearCase(), map);
    }

    @Test
    public void testAverageAuthorsForPublicationBenchmark() {
        Benchmark benchmark = newAverageAuthorsForPublicationBenchmark(noInputs());
        List<BenchmarkResult> results = benchmark.run();
        assertNotNull(results);
        assertEquals("Wrong result size", 1, results.size());;
        BenchmarkResult benchmarkResult = results.get(0);
        Double distance = (Double) benchmarkResult.getResult();
        assertTrue(1.749 < distance && distance < 1.751); // expected value is 1.750803826
    }

    private Map<Long, Long> yearCase() {
        Map<Long, Long> map = new TreeMap<>();
        map.put(1950L,23L);
        map.put(1951L,17L);
        map.put(1952L,20L);
        map.put(1953L,72L);
        map.put(1954L,73L);
        map.put(1955L,68L);
        map.put(1956L,148L);
        map.put(1957L,130L);
        map.put(1958L,172L);
        map.put(1959L,344L);
        map.put(1960L,283L);
        map.put(1961L,465L);
        map.put(1962L,731L);
        map.put(1963L,585L);
        map.put(1964L,557L);
        map.put(1965L,652L);
        map.put(1966L,739L);
        map.put(1967L,1051L);
        map.put(1968L,1397L);
        map.put(1969L,1264L);
        map.put(1970L,1329L);
        map.put(1971L,1758L);
        map.put(1972L,1969L);
        map.put(1973L,2245L);
        map.put(1974L,2591L);
        map.put(1975L,3050L);
        return map;
    }


    public Benchmark newFindPublicationById(BenchmarkInputs[] arguments) {
        return new FindPublicationById(publicationService, arguments);
    }

    public Benchmark newFindWithFilterBenchmark(BenchmarkInputs[] arguments) {
        return new FindWithFilterBenchmark(publicationService, arguments);
    }

    public Benchmark newFindOrderedByBenchmark(BenchmarkInputs[] arguments) {
        return new FindOrderedByBenchmark(publicationService, arguments);
    }

    public Benchmark newCoauthorsBenchmark(BenchmarkInputs[] arguments) {
        return new CoauthorsBenchmark(personService, arguments);
    }

    public Benchmark newShortestPathBenchmark(BenchmarkInputs[] arguments) {
        return new ShortestPathBenchmark(personService, arguments);
    }

    public Benchmark newCountAllAuthorsForConferenceBenchmark(BenchmarkInputs[] arguments) {
        return new CountAllAuthorsForConferenceBenchmark(conferenceService, arguments);
    }

    public Benchmark newCountAllPublicationsForConferenceBenchmark(BenchmarkInputs[] arguments) {
        return new CountAllPublicationsForConferenceBenchmark(conferenceService, arguments);
    }

    public Benchmark newCountPublicationsForYearIntervalBenchmark(BenchmarkInputs[] arguments) {
        return new CountPublicationsForYearIntervalBenchmark(publicationService, arguments);
    }

    public Benchmark newAverageAuthorsForPublicationBenchmark(BenchmarkInputs[] arguments) {
        return new AverageAuthorsForPublicationBenchmark(publicationService, arguments);
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