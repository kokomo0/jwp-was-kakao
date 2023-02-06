package webserver.http;

public class ResponseBuilder {
    private String httpStatus;
    private String contentType;
    private String contentLength;
    private String connection;
    private String location;

    private byte[] body;

    public ResponseBuilder() {
    }

    public ResponseBuilder httpStatus(String status) {
        this.httpStatus = status;
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

    public ResponseBuilder body(byte[] body) {
        this.body = body;
        return this;
    }

    public Response build() {
        String statusLine = "HTTP/1.1 " + httpStatus;
        String headers = (contentType != null ? ("Content-Type: " + contentType + ";charset=utf-8" + " \r\n") : "")
                + (contentLength != null ? ("Content-Length: " + contentLength + " \r\n") : "")
                + (connection != null ? ("Connection: " + connection + " \r\n") : "")
                + (location != null ? ("Location: " + location + " \r\n") : "");

        return new Response(statusLine, headers, body);
    }
}