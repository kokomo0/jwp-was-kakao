package utils;

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
}
