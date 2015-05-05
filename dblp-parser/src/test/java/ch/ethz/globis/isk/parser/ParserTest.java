package ch.ethz.globis.isk.parser;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test
    public void testTitleInlineTags() {
        NonXMLDBLPParser parser = new NonXMLDBLPParser();
        String title = "This is a <tt> transcript </tt> tag";
        String line = makeTag("title", title);
        parser.processLine(line);
        assertEquals("title", parser.getTag());
        assertEquals(title, parser.getValue());
    }

    @Test
    public void testHTMLEntities() {
        NonXMLDBLPParser parser = new NonXMLDBLPParser(false);
        String title = "This is an &uuml; special character";
        String line = makeTag("title", title);
        parser.processLine(line);
        assertEquals("title", parser.getTag());
        assertEquals(title, parser.getValue());
    }

    private static String makeTag(String tag, String value) {
        return "<" + tag + ">" + value + "</" + tag + ">";
    }

}
