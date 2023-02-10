package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsingUtils {
    /**
     * @throws IndexOutOfBoundsException: 헤더에 콜론이 포함되지 않을 경우 발생하는 예외
     */
    public static Map<String, String> parseHeader(List<String> rawHeader) throws IndexOutOfBoundsException {
        Map<String, String> headers = new HashMap<>();
        rawHeader.subList(1, rawHeader.size()).forEach(param -> {
            String[] kv = param.split(":", 2);
            headers.put(kv[0].trim(), kv[1].trim());
        });

        return headers;
    }
}
