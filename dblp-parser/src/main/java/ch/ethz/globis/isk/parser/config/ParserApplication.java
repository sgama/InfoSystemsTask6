package ch.ethz.globis.isk.parser.config;

import ch.ethz.globis.isk.config.PersistenceConfig;
import ch.ethz.globis.isk.parser.DBLPParser;
import ch.ethz.globis.isk.parser.NonXMLDBLPParser;
import ch.ethz.globis.isk.parser.processor.EntityCache;
import ch.ethz.globis.isk.persistence.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PersistenceConfig.class, ServiceConfig.class})
@EnableAutoConfiguration
@ComponentScan({"ch.ethz.globis.isk.parser"})
public class ParserApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ParserApplication.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public EntityCache entityCache() {
        return new EntityCache();
    }

    private DBLPParser getParser() {
        DBLPParser parser = applicationContext.getBean(NonXMLDBLPParser.class);
        return parser;
    }

    @Override
    public void run(String... args) throws Exception {
        String filename;

        DBLPParser parser;
        try {
            if (args.length == 0) {
                filename = "dblp_filtered.xml";
            } else {
                filename = args[0];
            }
            parser = getParser();
            parser.process(filename);
        } catch (ArrayIndexOutOfBoundsException aioobe){
            LOG.error("Error processing parser parameters.", aioobe);
            printUsage();
            System.exit(0);
        } catch (Exception ex){
            LOG.error("Error encountered during the parsing.", ex);
            printUsage();
            System.exit(0);
        }
    }

    public static void printUsage() {
        System.out.println("usage: java -jar parser <xmlfile>");
    }

    public static void main(String[] args) throws Exception{
        SpringApplication.run(ParserApplication.class, args);
    }
}