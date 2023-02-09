package support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static webserver.http.Cookie.SESSION_ID;

public class Parameter {
    private Map<String, String> parameter = new HashMap<>();

    public Parameter() {}

    public Parameter(String query) {
        Arrays.stream(query.split("&")).forEach(param -> {
            String[] kv = param.split("=");
            parameter.put(kv[0].trim(), kv[1].trim());
        });
    }

    public String get(String key) {
        return parameter.getOrDefault(key, "");
    }

    public void add(Map<String, String> extraParameter) {
        parameter.putAll(extraParameter);
    }
    public void add(String key, String value) {
        parameter.put(key, value);
    }

    public String sessionId() {
        return parameter.get(SESSION_ID);
    }
}
