package ch.ethz.globis.isk.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Utility class for encoding/decoding strings that should appear in URL's.
 */
public class EncodingUtils {

    private static final String ENCODING = "UTF-8";

    private static final Logger LOG = LoggerFactory.getLogger(EncodingUtils.class);

    /**
     * Encodes an string to an URL-friendly representation.
     * @param unencodedString               The un-encoded String.
     * @return                              The encoded String.
     */
    public static String encode(String unencodedString) {
        String encodedString = unencodedString;
        try {
            encodedString = URLEncoder.encode(unencodedString, ENCODING);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Encoding {} not found. Returning the unencoded string");
        }
        return encodedString;
    }

    /**
     * Decodes an URL-encoded string
     * @param encodedString                 The encoded String.
     * @return                              The un-encoded String.
     */
    public static String decode(String encodedString) {
        String decodedString = encodedString;
        try {
            decodedString = URLDecoder.decode(encodedString, ENCODING);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Encoding {} not found. Returning the encoded string");
        }
        return decodedString;
    }
}
