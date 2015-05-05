package ch.ethz.globis.isk.config;

import ch.ethz.globis.isk.domain.mongo.*;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * The main configuration class for Spring.
 *
 * The @Configuration annotation marks it as a configuration class.
 * The @ComponentScan annotation marks the packages that will be scanned by Spring. Any Java classes
 * in these files that are annotated by @Component, @Service or @Repository will be instantiated automatically
 * by Spring. Moreover, any member attributes of objects corresponding to Spring managed classes and which are annotated
 * by @Autowired are automatically populated through dependency injection.
 * The @PropertySource annotation specifies a list of property files that Spring will scan for any properties.
 *
 * This class instantiates beans for the following profiles:
 *  - test - profile expected to be active when executing tests
 *  - import - profile expected to be active when importing data
 *  - production - profile expected to be active when accessing the production database
 *  - web - profile expected to be active when accessing the production database through a web application
 *
 *  Profiles can be activated in the following manner:
 *  - In JUnit tests by adding the annotation @ActiveProfiles(profiles = < String array of profile names> )\
 *  - In Spring Boot applications by adding the following line to application.properties:
 *          spring.profiles.active= comma separated list of profile names
 */
@Configuration
@ComponentScan(basePackages = { "ch.ethz.globis.isk" })
@PropertySource({ "classpath:persistence.properties" })
public class PersistenceConfig {

    /**
     * A reference to the Spring Environment. The Environment contains all the properties
     * in the property files listed as arguments to the @PropertySource annotation.
     *
     * Spring scans these files automatically once the annotation @PropertySource is set on a
     * class also marked with the @Configuration annotation.
     */
    @Autowired
    Environment environment;

    /**
     * A Boolean bean whose value determines if the database needs to be cleared on
     * startup.
     *
     * This is true in case of the profiles 'import' and 'test'.
     * @return                              True for the profiles 'import' and 'test'.
     */
    @Bean(name = "dropDatabase")
    @Profile({ "import", "test" })
    Boolean dropDatabase() {
        return true;
    }

    /**
     * A Boolean bean whose value determines if the database needs to be cleared on
     * startup.
     *
     * This is false in case one of the profiles 'production' or 'web' is active.
     * @return                              False for the profiles ''production' and 'web'
     */
    @Bean(name = "dropDatabase")
    @Profile({ "production", "web" })
    Boolean productionDropDatabase() {
        return false;
    }

    /**
     * A String bean representing the name of the database to be used.
     *
     * The name is only used if the profile 'test' is active.
     * @return                              The name of the database.
     */
    @Bean(name = "databaseName")
    @Profile("test")
    String testDatabaseName() {
        return "dblp-test";
    }

    /**
     * A String bean representing the name of the database to be used.
     *
     * The name is only used if one of the profiles 'production' ,'web' or 'import' is active.
     * @return                              The name of the database.
     */
    @Bean(name = "databaseName")
    @Profile({ "production", "import", "web" })
    String productionDatabaseName() {
        return "dblp";
    }

    /**
     * Create a MongoDB MongoTemplate corresponding to a database with the name databaseName.
     *
     * The MongoDB database server needs to be running on the default server port.
     *
     * If the value of dropDatabase is true, the database will be cleared on startup.
     *
     * @param databaseName                  The name of the database.
     * @param dropDatabase                  Weather to drop the database or not.
     * @return                              An MongoDB MongoTemplate corresponding
     *                                      to the database.
     */
    @Bean
    public MongoTemplate mongoTemplate(String databaseName, Boolean dropDatabase) throws Exception {
        MongoClient client = new MongoClient();
        MongoTemplate template = new MongoTemplate(client, databaseName);
        if (dropDatabase) {
            clearDatabase(template);
        }
        return template;
    }

    private void clearDatabase(MongoTemplate template) {
        Class[] clazzes = { MongoPublication.class, MongoPerson.class, MongoConference.class, MongoConferenceEdition.class, MongoJournal.class, MongoJournalEdition.class, MongoSeries.class, MongoPublisher.class, MongoSchool.class };
        for (Class clazz : clazzes) {
            template.dropCollection(clazz);
        }
    }
}
