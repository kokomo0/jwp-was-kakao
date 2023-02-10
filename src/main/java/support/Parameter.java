package support;

import java.util.HashMap;
import java.util.Map;

import static webserver.http.Cookie.SESSION_ID;

public class Parameter {
    private Map<String, String> parameter;

    public Parameter() {
        parameter = new HashMap<>();
    }

    public Parameter(Map<String, String> parameter) {
        this.parameter = parameter;
    }

    public String get(String key) {
        return parameter.get(key);
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
