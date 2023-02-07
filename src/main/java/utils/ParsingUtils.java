package utils;

import webserver.http.Request;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsingUtils {
    public static Map<String, String> parseHeader(List<String> rawHeader) {
        Map<String, String> headers = new HashMap<>();
        rawHeader.subList(1, rawHeader.size()).forEach(param -> {
            String[] kv = param.split(":", 2);
            headers.put(kv[0].trim(), kv[1].trim());
        });

        return headers;
    }

    public static Map<String, String> parseQueryString(String query) {
        Map<String, String> params = new HashMap<>();

        Arrays.stream(query.split("&")).forEach(param -> {
            String[] kv = param.split("=");
            params.put(kv[0].trim(), kv[1].trim());
        });
        return params;
    }

    public static Map<String, String> parseParameter(Request request) {
        if (request.get("Content-Type").equals("application/x-www-form-urlencoded"))
            return parseQueryString(request.getBody());
        return parseQueryString(request.getUri().split("\\?", 2)[1]);
    }

}
