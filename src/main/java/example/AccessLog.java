package example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccessLog {

    public static final String METHOD_GET = "GET";
    public static final String STATUS_OK = "200";
    
    public static final int GROUP_OF_METHOD = 5;
    public static final int GROUP_OF_URL = 6;
    public static final int GROUP_OF_STATUS = 8;
    
    private static final String REGEX = "^" +
            // group 1-3, e.g. 44.168.187.141 - fe5dbbcea5ce7e2988b8c69bcfdfde8904aabc1f
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

    private Matcher matcher;
    
    private AccessLog() {};
    
    public static AccessLog parse(String line) {
        AccessLog log = new AccessLog();
        log.matcher = Pattern.compile(REGEX).matcher(line);
        if (!log.matcher.matches()) {
            throw new IllegalArgumentException(line);
        }
        return log;
    }
    
    public boolean isGetMethod() {
        return METHOD_GET.equals(matcher.group(GROUP_OF_METHOD));
    }

    public boolean isOkStatus() {
        return STATUS_OK.equals(matcher.group(GROUP_OF_STATUS));
    }
    
    public String getUrl() {
        return matcher.group(GROUP_OF_URL);
    }
}
