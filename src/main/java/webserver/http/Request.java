package webserver.http;

import java.util.Map;

public class Request {
    private final String method;
    private final String uri;
    private final String httpVersion;
    private final Map<String, String> headers;
    private String body;

    public Request(String method, String uri, String httpVersion, Map<String, String> headers) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getHttpVersion() {
        return httpVersion;
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
