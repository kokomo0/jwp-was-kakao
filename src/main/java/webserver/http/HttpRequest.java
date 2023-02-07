package webserver.http;

import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String uri;
    private final String httpVersion;
    private final Map<String, String> headers;
    private final String body;

    public HttpRequest(String method, String uri, String httpVersion, Map<String, String> headers, String body) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = body;
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

    public String getBody() {
        return body;
    }
}
