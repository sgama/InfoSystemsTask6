package ch.ethz.globis.isk.benchmark;

import ch.ethz.globis.isk.domain.Conference;
import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.service.BaseService;
import ch.ethz.globis.isk.service.ConferenceService;
import ch.ethz.globis.isk.service.PersonService;
import ch.ethz.globis.isk.service.PublicationService;
import ch.ethz.globis.isk.util.Order;
import ch.ethz.globis.isk.util.OrderFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Contains all the benchmarks for the DBLP data.
 */
public class Benchmarks {

    public static class FindWithFilterBenchmark extends Benchmark<Iterable<Publication>> {

        public FindWithFilterBenchmark(BaseService service, BenchmarkInputs[] arguments) {
            super(service, arguments);
        }

        public FindWithFilterBenchmark(PublicationService publicationService) {
            super(publicationService);
        }

        @Override
        public String name() {
            return "Find publication page by title";
        }

        @Override
        public BenchmarkResult<Iterable<Publication>> internalRun(BenchmarkInputs arguments) {
            PublicationService service = (PublicationService) service();
            String title = (String) arguments.get(0);
            Long start = Long.valueOf((String) arguments.get(1));
            Long size = Long.valueOf((String) arguments.get(2));
            String message = constructMessage(arguments);
            Iterable<Publication> result = service.findByTitle(title, null, start.intValue(), size.intValue());
            return new BenchmarkResult<>(result, message, name());
        }

        @Override
        public void writeDefaultArguments() throws IOException {
            openWriter();

            PublicationService publicationService = (PublicationService) service();
            Iterable<Publication> publications = publicationService.find(0, 500);
            String newLineChar = System.getProperty("line.separator");
            for (Publication publication : publications) {
                String title = publication.getTitle();
                if (title != null) {
                    for (String word : title.split(" ")) {
                        writer.write(String.format(("%s %s %s" + newLineChar), word.replace(".", ""), 0, 15));
                    }
                }
            }

            closeWriter();
        }
    }

    public static class FindOrderedByBenchmark extends Benchmark<Iterable<Publication>> {

        public FindOrderedByBenchmark(BaseService service, BenchmarkInputs[] arguments) {
            super(service, arguments);
        }

        public FindOrderedByBenchmark(PublicationService publicationService) {
            super(publicationService);
        }

        @Override
        public String name() {
            return "Find publication page ordered by title";
        }

        @Override
        public BenchmarkResult<Iterable<Publication>> internalRun(BenchmarkInputs arguments) {
            PublicationService service = (PublicationService) service();
            Long start = Long.valueOf((String) arguments.get(0));
            Long size = Long.valueOf((String) arguments.get(1));
            OrderFilter orderFilter = new OrderFilter("title", Order.ASC);
            Iterable<Publication> result = service.find(Arrays.asList(orderFilter), start.intValue(), size.intValue());
            String message = constructMessage(arguments);
            return new BenchmarkResult<>(result, message, name());
        }

        @Override
        public void writeDefaultArguments() throws IOException {
            openWriter();

            PublicationService publicationService = (PublicationService) service();
            String newLineChar = System.getProperty("line.separator");
            long count = publicationService.count();
            for (int i = 15; i < count; i+=15) {
                writer.write(String.format("%s %s" + newLineChar, i - 15, i));
            }
            closeWriter();
        }
    }

    public static class FindPublicationById extends Benchmark<Publication> {

        public FindPublicationById(PublicationService publicationService) {
            super(publicationService);
        }

        @Override
        public String name() {
            return "Find publication by id";
        }

        @Override
        public BenchmarkResult<Publication> internalRun(BenchmarkInputs arguments) {
            PublicationService service = (PublicationService) service();
            String key = (String) arguments.get(0);
            String message = constructMessage(arguments);
            Publication result = service.findOne(key);
            return new BenchmarkResult<>(result, message, name());
        }

        @Override
        public void writeDefaultArguments() throws IOException {
            openWriter();

            PublicationService publicationService = (PublicationService) service();
            Iterable<Publication> publications = publicationService.find(0, 500);
            String newLineChar = System.getProperty("line.separator");
            for (Publication publication : publications) {
                writer.write(publication.getId() + newLineChar);
            }
            closeWriter();
        }

        public FindPublicationById(BaseService service, BenchmarkInputs[] arguments) {
            super(service, arguments);
        }
    }

    public static class CoauthorsBenchmark extends Benchmark<Iterable<Person>> {

        public CoauthorsBenchmark(BaseService service, BenchmarkInputs[] arguments) {
            super(service, arguments);
        }

        public CoauthorsBenchmark(PersonService personService) {
            super(personService);
        }

        @Override
        public String name() {
            return "Find coauthors for person id";
        }

        @Override
        public BenchmarkResult<Iterable<Person>> internalRun(BenchmarkInputs arguments) {
            PersonService service = (PersonService) service();
            String first = (String) arguments.get(0);
            Iterable<Person> result  = service.getCoauthors(first);
            String message = constructMessage(arguments);
            return new BenchmarkResult<>(result, message, name());
        }

        @Override
        public void writeDefaultArguments() throws IOException {
            openWriter();

            PersonService personService = (PersonService) service();
            Iterable<Person> persons = personService.find(0, 500);
            String newLineChar = System.getProperty("line.separator");
            for (Person person : persons) {
                writer.write(person.getId() + newLineChar);
            }
            closeWriter();
        }
    }

    public static class ShortestPathBenchmark extends Benchmark<Long> {

        public ShortestPathBenchmark(BaseService service, BenchmarkInputs[] arguments) {
            super(service, arguments);
        }

        public ShortestPathBenchmark(PersonService personService) {
            super(personService);
        }

        @Override
        public String name() {
            return "Shortest path between two authors";
        }

        @Override
        public BenchmarkResult<Long> internalRun(BenchmarkInputs arguments) {
            PersonService service = (PersonService) service();
            String first = (String) arguments.get(0);
            String second = (String) arguments.get(1);
            Long result  = service.computeAuthorDistance(first, second);
            String message = constructMessage(arguments);
            return new BenchmarkResult<>(result, message, name());
        }

        @Override
        public void writeDefaultArguments() throws IOException {
            openWriter();

            PersonService personService = (PersonService) service();
            List<Person> persons = (List<Person>) personService.find(0, 500);
            String newLineChar = System.getProperty("line.separator");
            for (int i = 1; i < persons.size(); i++) {
                writer.write(persons.get(i -1).getId() + " " + persons.get(i).getId() + newLineChar);
            }
            closeWriter();
        }
    }

    public static class CountAllAuthorsForConferenceBenchmark extends Benchmark<Long> {

        public CountAllAuthorsForConferenceBenchmark(BaseService service, BenchmarkInputs[] arguments) {
            super(service, arguments);
        }

        public CountAllAuthorsForConferenceBenchmark(ConferenceService conferenceService) {
            super(conferenceService);
        }

        @Override
        public String name() {
            return "Count all authors featured in a conference";
        }

        @Override
        public BenchmarkResult<Long> internalRun(BenchmarkInputs arguments) {
            ConferenceService service = (ConferenceService) service();
            String conferenceId = (String) arguments.get(0);
            Long count = service.countAuthorsForConference(conferenceId);
            String message = constructMessage(arguments);
            return new BenchmarkResult<>(count, message, name());
        }

        @Override
        public void writeDefaultArguments() throws IOException {
            openWriter();

            ConferenceService conferenceService = (ConferenceService) service();
            Collection<Conference> conferences = (Collection<Conference>) conferenceService.findAll();
            String newLineChar = System.getProperty("line.separator");
            for (Conference conference : conferences) {
                writer.write(conference.getId() + " " + newLineChar);
            }
            closeWriter();
        }
    }

    public static class CountAllPublicationsForConferenceBenchmark extends Benchmark<Long> {

        public CountAllPublicationsForConferenceBenchmark(BaseService service, BenchmarkInputs[] arguments) {
            super(service, arguments);
        }

        public CountAllPublicationsForConferenceBenchmark(ConferenceService conferenceService) {
            super(conferenceService);
        }

        @Override
        public String name() {
            return "Count all publications featured in a conference";
        }

        @Override
        public BenchmarkResult<Long> internalRun(BenchmarkInputs arguments) {
            ConferenceService service = (ConferenceService) service();
            String conferenceId = (String) arguments.get(0);
            Long count = service.countPublicationsForConference(conferenceId);
            String message = constructMessage(arguments);
            return new BenchmarkResult<>(count, message, name());
        }

        @Override
        public void writeDefaultArguments() throws IOException {
            openWriter();

            ConferenceService conferenceService = (ConferenceService) service();
            Collection<Conference> conferences = (Collection<Conference>) conferenceService.findAll();
            String newLineChar = System.getProperty("line.separator");
            for (Conference conference : conferences) {
                writer.write(conference.getId() + " " + newLineChar);
            }
            closeWriter();
        }
    }

    public static class CountPublicationsForYearIntervalBenchmark extends Benchmark<Map<Long, Long>> {

        public CountPublicationsForYearIntervalBenchmark(BaseService service, BenchmarkInputs[] arguments) {
            super(service, arguments);
        }

        public CountPublicationsForYearIntervalBenchmark(PublicationService publicationService) {
            super(publicationService);
        }

        @Override
        public String name() {
            return "Count number of publications per year interval";
        }

        @Override
        public BenchmarkResult<Map<Long, Long>> internalRun(BenchmarkInputs arguments) {
            PublicationService service = (PublicationService) service();
            Long startYear = Long.valueOf((String) arguments.get(0));
            Long endYear = Long.valueOf((String) arguments.get(1));
            Map<Long,Long> resultMap = service.countPerYears(startYear, endYear);
            String message = constructMessage(arguments);
            return new BenchmarkResult<>(resultMap, message, name());
        }

        @Override
        public void writeDefaultArguments() throws IOException {
            openWriter();
            String newLineChar = System.getProperty("line.separator");
            for (int i = 1945; i < 1990; i+=5) {
                writer.write((i - 5) + " " + i + newLineChar);
            }
            closeWriter();
        }
    }

    public static class AverageAuthorsForPublicationBenchmark extends Benchmark<Double> {

        public AverageAuthorsForPublicationBenchmark(BaseService service, BenchmarkInputs[] arguments) {
            super(service, arguments);
        }

        public AverageAuthorsForPublicationBenchmark(PublicationService publicationService) {
            super(publicationService);
        }

        @Override
        public String name() {
            return "Compute average number of authors for publication";
        }

        @Override
        public BenchmarkResult<Double> internalRun(BenchmarkInputs arguments) {
            PublicationService service = (PublicationService) service();
            String message = constructMessage(arguments);
            Double result = service.averageNumberOfAuthors();
            return new BenchmarkResult<>(result, message, name());
        }

        @Override
        public void writeDefaultArguments() throws IOException {
            openWriter();
            String newLineChar = System.getProperty("line.separator");
            for (int i = 0; i < 10; i++) {
                writer.write(i + newLineChar);
            }
            closeWriter();
        }
    }

    public static BenchmarkInputs[] inputCases(BenchmarkInputs... inputCases) {
        return inputCases;
    }

    public static BenchmarkInputs inputCase(Object... arguments) {
        return new BenchmarkInputs(arguments);
    }

    public static BenchmarkInputs[] noInputs() {
        return inputCases(new BenchmarkInputs( new Object[0]));
    }

    private static String constructMessage(BenchmarkInputs inputs) {
        return "Arguments: " + inputs;
    }
}