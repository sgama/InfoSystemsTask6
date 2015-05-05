package ch.ethz.globis.isk.parser.config;

import ch.ethz.globis.isk.config.PersistenceConfig;
import ch.ethz.globis.isk.persistence.ServiceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PersistenceConfig.class, ServiceConfig.class})
@ComponentScan(basePackages = { "ch.ethz.globis.isk.parser"})
public class ParserTestConfig {
}
