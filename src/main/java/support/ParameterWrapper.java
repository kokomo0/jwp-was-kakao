package support;

import webserver.http.HttpRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ParameterWrapper {
    public static final String QUERY_PATTERN = "[^=&\\s]+=[^=&\\s]+(&[^=&\\s]+=[^=&\\s]+)*";
    public static final String PATH_QUERY_DELIMITER = "?";
    public static final String QUERIES_DELIMITER = "&";
    public static final String KEY_VALUE_DELIMITER = "=";

    /**
     * @throws IllegalArgumentException: 쿼리스트링이 표현식을 만족하지 않을 경우 예외를 던진다.
     */
    public static Parameter wrap(HttpRequest request) throws IllegalArgumentException {
        String query = getQuery(request);
        if (query == null || query.isEmpty()) return new Parameter();
        if (!isQuery(query)) throw new IllegalArgumentException();
        return new Parameter(parseQuery(query));
    }

    private static String getQuery(HttpRequest request) {
        if (request.getUri().contains(PATH_QUERY_DELIMITER))
            return request.getUri().split("\\?")[1];
        if (!request.getBody().isEmpty() && request.getBody() != null)
            return request.getBody();
        return null;
    }

    private static boolean isQuery(String query) {
        return Pattern.matches(QUERY_PATTERN, query);
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> parameter = new HashMap<>();
        Arrays.stream(query.split(QUERIES_DELIMITER)).forEach(param -> {
            String[] kv = param.split(KEY_VALUE_DELIMITER);
            parameter.put(kv[0].trim(), kv[1].trim());
        });
        return parameter;
    }
}
