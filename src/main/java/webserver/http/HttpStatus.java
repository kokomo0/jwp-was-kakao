package webserver.http;

public enum HttpStatus {
    OK(200, "OK"),
    FOUND(302, "Found"),
    NOT_FOUND(404, "NotFound")
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
