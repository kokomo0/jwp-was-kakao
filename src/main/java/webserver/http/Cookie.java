package webserver.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Cookie {

    public static final String SESSION_ID = "JSESSIONID";
    public static final String COOOKIES_DELIMITER = ";";
    public static final String KEY_VALUE_DELIMITER = "=";
    private final Map<String, String> cookies;

    private Cookie(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public static Cookie setCookie(String uuid) {
        Map<String, String> cookies = new HashMap<>();
        cookies.put(SESSION_ID, uuid);
        cookies.put("Path", "/");
        cookies.put("Max-Age", "60");
        return new Cookie(cookies);
    }

    public static Cookie parseCookie(String query) throws IndexOutOfBoundsException {
        Map<String, String> cookies = new HashMap<>();
        Arrays.stream(query.split(COOOKIES_DELIMITER))
                .map(e -> e.trim().split(KEY_VALUE_DELIMITER))
                .forEach(e -> cookies.put(e[0].trim(), e[1].trim()));
        return new Cookie(cookies);
    }

    public String get(String key) {
        return cookies.getOrDefault(key, "");
    }

    public Map<String, String> getAllCookies() {
        return cookies;
    }
}
