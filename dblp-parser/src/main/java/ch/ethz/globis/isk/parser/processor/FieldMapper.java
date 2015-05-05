package ch.ethz.globis.isk.parser.processor;

public class FieldMapper {

    public static final String JOURNAL_ID_PREFIX = "jour-";
    public static final String JOURNAL_EDITION_ID_PREFIX = "je-";
    public static final String CONFERENCE_ID_PREFIX = "conf-";
    public static final String CONFERENCE_EDITION_ID_PREFIX = "ce-";
    public static final String PERSON_ID_PREFIX = "pers-";
    public static final String SERIES_ID_PREFIX = "ser-";
    public static final String SCHOOl_ID_PREFIX = "sch-";
    public static final String PUBLISHER_ID_PREFIX = "pub-";

    public static String personId(String personName) {
        return personName.toLowerCase().replaceAll(" ", "-");
    }

    public static String journalId(String key) {
        String tokens[] = key.split("-");
        if (tokens.length == 3 && "journals".equals(tokens[0])) {
            return tokens[1];
        }
        return null;
    }

    /**
     * Publication names can have the following forms
     *
     * PhdThesis - either
     *  'books/daglib/<integer>' or 'phd/<language>/<string identifier>'
     *
     * MastersThesis - '<phd/ms>/<string identifier>'
     *
     * Book - either
     *  conf/<string>/</string> , reference/<string>/</string>, series/<string>/</string>, books/<string>/</string>
     *
     * @param xmlKey
     * @return
     */
    public static String publicationKey(String xmlKey) {
        return xmlKey.replaceAll("/", "-");
    }

    public static String publisherId(String publisherName) {
        return lowerCaseAndDash(publisherName);
    }

    public static String schoolId(String schoolName) {
        return lowerCaseAndDash(schoolName);
    }

    public static String seriesId(String seriesName) {
        return lowerCaseAndDash(seriesName);
    }

    public static Integer extractYear(String yearString) {
        return extractInt(yearString);
    }

    public static Integer extractMonth(String yearString) {
        return extractInt(yearString);
    }

    public static String journalEditionId(String journalId, String volume, Integer year, String number) {
        String newJournalId = journalId;
        if (volume != null) {
            newJournalId += volume;
        }
        if (year != null) {
            newJournalId += year;
        }
        if (number != null) {
            newJournalId += number;
        }
        if (newJournalId.equals(journalId)) {
            newJournalId += "1";
        }
        return newJournalId;
    }

    private static String lowerCaseAndDash(String string) {
        string = string.trim().toLowerCase().replaceAll("( |/)+", "-");
        return string;
    }

    public static String conferenceId(String proceedingsKey) {
        String tokens[] = proceedingsKey.split("/");
        if ("conf".equals(tokens[0])) {
            return tokens[1];
        } else {
            return null;
        }
    }

    public static String conferenceEditionId(String proceedingsKey) {
        String tokens[] = proceedingsKey.split("conf/");
        if (tokens.length > 0) {
            return lowerCaseAndDash(tokens[1]);
        }
        return null;
    }

    public static Integer extractInt(String value) {
        Integer result;
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            result = null;
        }
        return result;
    }
}