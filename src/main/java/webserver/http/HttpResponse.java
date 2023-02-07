package webserver.http;

public class HttpResponse {
    private final String statusLine;
    private final String headers;
    private final byte[] body;

    public HttpResponse(String statusLine, String headers, byte[] body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public String getStatusLine() {
        return statusLine;
    }

    public String getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
