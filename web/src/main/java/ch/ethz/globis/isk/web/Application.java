package ch.ethz.globis.isk.web;

import ch.ethz.globis.isk.config.PersistenceConfig;
import ch.ethz.globis.isk.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan
@EnableWebMvc
@EnableAutoConfiguration(exclude = { HibernateJpaAutoConfiguration.class })
@Import({ PersistenceConfig.class })
public class Application {

    @Autowired
    private PublicationService publicationService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }
}
