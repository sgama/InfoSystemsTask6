package ch.ethz.globis.isk.config;

import ch.ethz.globis.isk.domain.db4o.*;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.constraints.UniqueFieldValueConstraint;
import com.db4o.io.CachingStorage;
import com.db4o.io.FileStorage;
import com.db4o.io.Storage;
import com.db4o.ta.TransparentPersistenceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import java.io.File;

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
     * Create a db4o ObjectContainer corresponding to a database with the name databaseName.
     *
     * The database is started in the embedded mode and is located in the user home directory
     * and will use the file: databaseName.db4o .
     *
     * If the value of dropDatabase is true, the database will be cleared on startup.
     *
     * @param databaseName                  The name of the database.
     * @param dropDatabase                  Weather to drop the database or not.
     * @return                              An embedded db4o ObjectContainer corresponding
     *                                      to the database.
     */
    @Bean
    ObjectContainer objectContainer(String databaseName, Boolean dropDatabase) {
        EmbeddedConfiguration configuration = defaultConfiguration();
        String currentDirectoryPath = System.getProperty("user.home");
        File currentDirectory = new File(currentDirectoryPath);
        File databaseFile = new File(currentDirectory, databaseName + ".db4o");
        if (dropDatabase && databaseFile.exists()) {
            databaseFile.delete();
        }
        ObjectContainer db = Db4oEmbedded.openFile(configuration, databaseFile.getAbsolutePath());
        return db;
    }

    public static EmbeddedConfiguration defaultConfiguration() {
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        configuration.common().add(new TransparentPersistenceSupport());
        setupStorage(configuration);
        addUniqueConstrains(configuration);
        return configuration;
    }

    /**
     * Sets up the storage information on the EmbeddedConfiguration object.
     * @param configuration                 The EmbeddedConfiguration object.
     */
    private static void setupStorage(EmbeddedConfiguration configuration) {
        // max size = block_size * 2 GB
        Storage fileStorage = new FileStorage();
        Storage cachingStorage = new CachingStorage(fileStorage, 1024, 4 * 1024);
        configuration.file().storage(cachingStorage);
        configuration.file().blockSize(8);
    }

    /**
     * Sets up the unique constraints on the The EmbeddedConfiguration object.
     * @param configuration                 The EmbeddedConfiguration object.
     */
    private static void addUniqueConstrains(EmbeddedConfiguration configuration) {
        Class[] classes = new Class[] { Db4oArticle.class, Db4oBook.class, Db4oConference.class, Db4oConferenceEdition.class, Db4oInCollection.class, Db4oInProceedings.class, Db4oJournal.class, Db4oJournalEdition.class, Db4oMasterThesis.class, Db4oPerson.class, Db4oPhdThesis.class, Db4oProceedings.class, Db4oPublication.class, Db4oPublisher.class, Db4oSchool.class, Db4oSeries.class };
        String ID_FIELD = "id";
        for (Class clazz : classes) {
            configuration.common().objectClass(clazz).objectField(ID_FIELD).indexed(true);
            configuration.common().add(new UniqueFieldValueConstraint(clazz, ID_FIELD));
        }
    }
}
