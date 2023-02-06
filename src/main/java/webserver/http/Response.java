package webserver.http;

public class Response {
    private String statusLine;
    private String headers;
    private byte[] body;

    public Response(String statusLine, String headers, byte[] body) {
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
