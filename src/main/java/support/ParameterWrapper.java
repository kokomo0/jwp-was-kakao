package support;

import webserver.http.HttpRequest;

import java.util.regex.Pattern;

public class ParameterWrapper {
    public static final String QUERY_PATTERN = "[^=&\\s]+=[^=&\\s]+(&[^=&\\s]+=[^=&\\s])*";

    /**
     * @throws IllegalArgumentException: 쿼리스트링이 표현식을 만족하지 않을 경우 예외를 던진다.
     */
    public static Parameter wrap(HttpRequest request) throws IllegalArgumentException {
        String query = getQuery(request);
        if (query == null || query.isEmpty()) return new Parameter();
        if (!isQuery(query)) throw new IllegalArgumentException();
        return new Parameter(query);
    }

    private static String getQuery(HttpRequest request) {
        if (request.getUri().contains("?"))
            return request.getUri().split("\\?")[1];
        if (!request.getBody().isEmpty() && request.getBody() != null)
            return request.getBody();
        return null;
    }

    private static boolean isQuery(String query) {
        return Pattern.matches(QUERY_PATTERN, query);
    }
}
