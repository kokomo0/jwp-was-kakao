package webserver.http;

public enum HttpStatus {
    OK(200, "OK"),
    FOUND(302, "Found"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    INTERNET_SERVER_ERROR(500, "Internal Server Error"),
    UNKNOWN_ERROR(520, "Unknown Error")
    ;
    private final int value;
    private final String description;

    HttpStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int valueOf() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
