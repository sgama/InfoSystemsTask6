package ch.ethz.globis.isk.parser.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProcessorLocator {

    @Autowired
    ApplicationContext applicationContext;

    private final Map<String, EntityXMLProcessor> processors;
    private static final String PROCESSOR = "xmlprocessor";

    public ProcessorLocator() {
        this.processors = new HashMap<>();
    }

    @PostConstruct
    private void loadProcessors() {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            String lowercaseBeanName = beanName.toLowerCase();
            if (lowercaseBeanName.contains(PROCESSOR)) {
                EntityXMLProcessor processor = (EntityXMLProcessor) applicationContext.getBean(beanName);
                String processorKey = extractKeyFromProcessorName(lowercaseBeanName);
                processors.put(processorKey, processor);
            }
        }
    }

    public EntityXMLProcessor getProcessor(String entityTagName) {
        if (processors.isEmpty()) {
            loadProcessors();
        }

        EntityXMLProcessor processor = processors.get(entityTagName);
        if (processor == null) {
            throw new IllegalArgumentException("No processor found for entity " + entityTagName);
        }
        return processor;
    }

    private String extractKeyFromProcessorName(String beanName) {
        String[] tokens = beanName.split(PROCESSOR);
        return tokens[0].toLowerCase();
    }
}