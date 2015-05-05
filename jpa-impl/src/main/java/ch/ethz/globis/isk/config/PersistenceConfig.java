package ch.ethz.globis.isk.config;

import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Properties;

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
 *
 */
@Configuration
@ComponentScan(basePackages = { "ch.ethz.globis.isk" })
@PropertySource({ "classpath:jpa-persistence.properties", "classpath:persistence.properties" })
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

    /*
    * The EntityManager is the type of object that should be created and used to interact with the database.
    *
    * Because the EntityManager is not a thread-safe class, using a singleton entityManager might not working so well
    * in a web environment. Therefore, for the web profile, an entityManager will be created for each web request.
    *
    * If EntityManager would be thread-safe, one could simply instantiate it in the following manner:
    *
    * @Bean
    * public EntityManager entityManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
    *       nerEntityManagerFactoryBean );
    *   }
    * */
    /**
     * Create the entityManager bean of type EntityManager. In case of the test, import and production profiles,
     * where work is done by a single thread, a singleton entityManager is sufficient.
     *
     * @param localContainerEntityManagerFactoryBean                    The entityManagerFactoryBean
     * @return                                                          An EntityManager bean.
     */
    @Bean(name = "entityManager")
    @Profile({ "test", "import", "production" })
    public EntityManager entityManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        return localContainerEntityManagerFactoryBean.getObject().createEntityManager();
    }

    /**
     * Create the entityManager bean of type EntityManager. Configure Spring to instantiate one EntityManager object
     * per request.
     * @param localContainerEntityManagerFactoryBean                    The entityManagerFactoryBean
     * @return                                                          An EntityManager bean.
     */
    @Bean(name = "entityManager")
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Profile("web")
    public EntityManager webEntityManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        return localContainerEntityManagerFactoryBean.getObject().createEntityManager();
    }

    /**
     * Create an H2 data source with the name received as an argument.
     *
     * This bean is needed to create an EntityManagerFactory object.
     *
     * Because this method is placed in a configuration file and that it is annotated
     * by @Bean, the databaseName String is a bean that Spring looks up in the application
     * context by the name 'databaseName'.
     *
     * @param databaseName                  The name of the database.
     *
     * @return                              A DataSource object configured to connected to
     *                                      the database with the name 'databaseName'.
     */
    @Bean
    public DataSource h2DataSource(String databaseName) {
        String databaseUrl = environment.getProperty("jdbc.url");
        String url = String.format(databaseUrl, databaseName);
        JdbcConnectionPool cp = JdbcConnectionPool.create(url, environment.getProperty("jdbc.user"), environment.getProperty("jdbc.pass"));
        return cp;
    }

    /**
     * Create a Properties object for the Hibernate properties.
     *
     * This bean is needed to create an EntityManagerFactory object.
     *
     * Because this method is placed in a configuration file and that it is annotated
     * by @Bean, the dropDatabase Boolean is a bean that Spring looks up in the application
     * context by the name 'dropDatabase'.
     *
     * @param dropDatabase                  Weather to drop the database or not.
     * @return                              A Properties bean that is used to configure Hibernate.
     */
    @Bean
    Properties hibernateProperties(Boolean dropDatabase) {
        final String hbm2ddAutoSetting = dropDatabase ? "create" : "update";
        return new Properties() {

            {
                setProperty("javax.persistence.validation.mode", "none");
                setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
                setProperty("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
                setProperty("hibernate.hbm2ddl.auto", hbm2ddAutoSetting);
                setProperty("hibernate.jdbc.batch_size", environment.getProperty("hibernate.jdbc.batch_size"));
                setProperty("hibernate.order_inserts", environment.getProperty("hibernate.order_inserts"));
                setProperty("hibernate.order_updates", environment.getProperty("hibernate.order_updates"));
                setProperty("hibernate.cache.use_second_level_cache", environment.getProperty("hibernate.cache.use_second_level_cache"));
            }
        };
    }

    /**
     * This bean is needed to create an EntityManagerFactory object.
     * @return                              A jpa vendor adapter.
     */
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setGenerateDdl(false);
        hibernateJpaVendorAdapter.setDatabase(Database.HSQL);
        return hibernateJpaVendorAdapter;
    }

    /**
     * The EntityManagerFactory bean. This is used to create EntityManager instances.
     *
     * @param dataSource                        The data source used.
     * @param jpaVendorAdapter                  The jpa vendor adapter.
     * @param hibernateProperties               The hibernate properties.
     * @return                                  An entityManagerFactory bean used to create EntityManager instances.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter, Properties hibernateProperties) {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.setJpaProperties(hibernateProperties);
        lef.setPackagesToScan("ch.ethz.globis.isk.domain");
        return lef;
    }
}
