package ch.ethz.globis.isk.parser;

import ch.ethz.globis.isk.parser.processor.EntityCache;
import ch.ethz.globis.isk.parser.processor.EntityXMLProcessor;
import ch.ethz.globis.isk.parser.processor.ProcessorLocator;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * A parser for the dblp.xml file. The reasons this is called non-xml-parser is because it does not
 * use any of the Java XML API's for parsing the document.
 */
@Component("non-xml-parser")
public class NonXMLDBLPParser implements DBLPParser {

    private static final Logger LOG = LoggerFactory.getLogger(NonXMLDBLPParser.class);
    private String tag;
    private String value;
    private EntityXMLProcessor processor = null;
    private boolean unescapeHTML;

    @Autowired
    private EntityCache entityCache;

    @Autowired
    private ProcessorLocator locator;

    public NonXMLDBLPParser() {
        this.unescapeHTML = false;
    }

    public NonXMLDBLPParser(boolean unescapeHTML) {
        this.unescapeHTML = unescapeHTML;
    }

    @Override
    public void process(String filename) {
        BufferedReader scanner = null;
        int lines = 0;
        try {
            scanner = new BufferedReader(new FileReader(filename));
            LOG.info("Processing file: {}", filename);
            String line;
            while ((line = scanner.readLine()) != null) {
                if (processLine(line.trim())) {
                    processor.setData(tag, value);
                }
                lines+=1;
            }
            LOG.info("Processed {} lines.", lines);
        } catch (FileNotFoundException e) {
            LOG.error("File {} not found.", filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (scanner != null) {
                try {
                    scanner.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            entityCache.flush();
        }
    }

    public boolean processLine(String line) {
        tag = value = null;
        int index = 0;
        int size = line.length();
        if (size == 0 || line.charAt(0) != '<') {
            return false;
        }
        //go to start of tag
        while ('<' != line.charAt(index) && index < size) {
            index++;
        }
        int start = index;
        if (line.charAt(index + 1) == '/') {
            //end tag for publication
            if (processor != null) {
                processor.build();
                processor.clear();
                processor = null;
                return false;
            }
        }
        while (' ' != line.charAt(index) && '>' != line.charAt(index) && index < size) {
            index++;
        }
        int end = index;
        tag = line.substring(start + 1, end);
        value = "";
        switch (tag) {
            case DBLPTag.ARTICLE:
            case DBLPTag.BOOK:
            case DBLPTag.INCOLLECTION:
            case DBLPTag.PROCEEDINGS:
            case DBLPTag.INPROCEEDINGS:
            case DBLPTag.MASTER_THESIS:
            case DBLPTag.PHD_THESIS:
            case DBLPTag.WEBSITE:
                //get tag
                start = line.indexOf("key=", end) + 5;
                index = start;
                while ('\"' != line.charAt(index)) {
                    index++;
                }
                value = line.substring(start, index);
                processor = locator.getProcessor(tag);
                tag = "key";
                break;
            default:
                //get value
                start = end;
                end = line.indexOf("</" + tag, start+1);
                if (end < 0) {
                    return false;
                }
                value = line.substring(start + 1, end);
                start = value.indexOf("href");
                if (start > -1) {
                    value = value.substring(value.indexOf('>') + 1);
                }
                if (unescapeHTML && value.indexOf('&') > 0) {
                    value = StringEscapeUtils.unescapeHtml4(value);
                }
                break;
        }
        return true;
    }

    public String getTag() {
        return tag;
    }

    public String getValue() {
        return value;
    }
}
