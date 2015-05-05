package ch.ethz.globis.isk.parser;

import ch.ethz.globis.isk.parser.processor.FieldMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FieldMapperTest {

    @Test
    public void publisherNameConversion() {
        String publisher = "Springer";
        assertEquals("springer", FieldMapper.publisherId(publisher));

        publisher = "IEEE Computer Society";
        assertEquals("ieee-computer-society", FieldMapper.publisherId(publisher));
    }
}