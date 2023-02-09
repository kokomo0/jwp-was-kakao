package webserver.http;

import java.util.stream.Collectors;

public class ResponseBuilder {
    private String httpVersion = "HTTP/1.1";
    private HttpStatus httpStatus;
    private String contentType;
    private String contentLength;
    private String connection;
    private String location;
    private Cookie cookie;

    private byte[] body;

    public ResponseBuilder() {
    }

    public ResponseBuilder httpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
        return this;
    }

    public ResponseBuilder httpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public ResponseBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public ResponseBuilder contentLength(String contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public ResponseBuilder connection(String connection) {
        this.connection = connection;
        return this;
    }

    public ResponseBuilder location(String location) {
        this.location = location;
        return this;
    }

    public ResponseBuilder cookie(Cookie cookie) {
        this.cookie = cookie;
        return this;
    }

    public ResponseBuilder body(byte[] body) {
        this.body = body;
        this.contentLength = String.valueOf(body.length);
        return this;
    }

    public ResponseBuilder redirect(String location) {
        this.httpStatus = HttpStatus.FOUND;
        this.location = location;
        return this;
    }

    public HttpResponse build() {

        String statusLine = httpVersion + " " + httpStatus.valueOf() + " " + httpStatus.getDescription();
        String headers = (contentType != null ? ("Content-Type: " + contentType + ";charset=utf-8" + " \r\n") : "")
                + (contentLength != null ? ("Content-Length: " + contentLength + " \r\n") : "")
                + (connection != null ? ("Connection: " + connection + " \r\n") : "")
                + (location != null ? ("Location: " + location + " \r\n") : "")
                + (cookie != null ? (String.join("\r\n",
                cookie.getAllCookies().entrySet().stream()
                        .map(e -> "Set-Cookie: " + e.getKey() + "=" + e.getValue())
                        .collect(Collectors.toList())) + "\r\n") : "");

        return new HttpResponse(statusLine, headers, body);
    }
}