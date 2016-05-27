package example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public static final int GROUP_OF_URL = 6;
    
    private static final String REGEX = "^" +
            // group 1-3, e.g. 44.168.187.141 - -
            "([\\d.]+) (\\S+) (\\S+) " +
            // group 4, e.g. [22/Mar/2015:06:48:13 +0900]
            "\\[([\\w:/]+ [\\+\\-]\\d{4})\\] " +
            // group 5-9, e.g. "GET /category/music?from=20 HTTP/1.1" 200 127
            "\"(\\S+) ([^?]+).* (\\S+)\" (\\d{3}) (\\d+) " +
            // group 10, e.g. "/category/music?from=10"
            "\"(\\S+)\" " +
            // group 11, e.g. "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:10.0.1) Gecko/20100101 Firefox/10.0.1"
            "\"([^\"]+)\"" +
            "$";

    public static String getUrl(String line) {
        Matcher m = Pattern.compile(REGEX).matcher(line);
        if (!m.matches()) {
            throw new IllegalArgumentException(line);
        }
        return m.group(GROUP_OF_URL);
    }
}
