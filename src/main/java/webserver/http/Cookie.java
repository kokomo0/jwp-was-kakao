package webserver.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cookie {

    private final Map<String, String> cookies = new HashMap<>();

    public Cookie() {
        cookies.put("JSESSIONID", UUID.randomUUID().toString());
        cookies.put("Path", "/");
    }

    public Cookie(String cookieString) {
        parseAndSetCookie(cookieString);
    }

    private void parseAndSetCookie(String query) {
        Arrays.stream(query.split(";"))
                .map(e -> e.trim().split("="))
                .forEach(e -> cookies.put(e[0].trim(), e[1].trim()));
    }

    public Map<String,String> getCookies() {
        return cookies;
    }
}
