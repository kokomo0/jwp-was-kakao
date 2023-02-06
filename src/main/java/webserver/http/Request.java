package webserver.http;

import java.util.Map;

public class Request {
    private final Map<String, String> headers;

    private String body;
    public Request(Map<String, String> headers, String body) {
        this.headers = headers;
        this.body = body;
    }

    public String get(String key) {
        return headers.getOrDefault(key, "");
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}
