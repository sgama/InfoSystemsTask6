package ch.ethz.globis.isk.parser;

import ch.ethz.globis.isk.parser.config.ParserTestConfig;
import ch.ethz.globis.isk.parser.processor.EntityXMLProcessor;
import ch.ethz.globis.isk.parser.processor.ProcessorLocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ParserTestConfig.class })
@ActiveProfiles("test")
public class ProcessorLocatorTest {

    @Autowired
    private ProcessorLocator processorLocator;

    private static final String[] publicationNames = { "article", "book", "incollection", "proceedings", "inproceedings", "phdthesis", "mastersthesis" };

    @Test
    public void findProperProcessors() {
        for (String name : publicationNames) {
            EntityXMLProcessor processor = processorLocator.getProcessor(name);
            assertNotNull(processor);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidProcessorName() {
        String invalidName = "";
        processorLocator.getProcessor(invalidName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullProcessorName() {
        processorLocator.getProcessor(null);
    }
}
