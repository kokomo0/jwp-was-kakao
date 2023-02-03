package utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsingUtils {
    public static Map<String, String> parseHeader(List<String> rawHeader) {
        Map<String, String> headers = new HashMap<>();
        String[] startLine = rawHeader.get(0).split(" ");
        headers.put("method", startLine[0]);
        headers.put("path", startLine[1]);
        headers.put("protocol version", startLine[2]);

        rawHeader.subList(1,rawHeader.size()).stream().forEach(param -> {
            String[] kv = param.split(":", 2);
            headers.put(kv[0].trim(), kv[1].trim());
        });
        return headers;
    }

    public static Map<String, String> parseQueryString(String query) {
        Map<String, String> params = new HashMap<>();
        //String query = path.split("\\?")[1];

        Arrays.stream(query.split("&")).forEach(param -> {
            String[] kv = param.split("=");
            params.put(kv[0].trim(), kv[1].trim());
        });

        return params;
    }

}
