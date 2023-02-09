package controller.support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Parameter {
    private Map<String, String> parameter = new HashMap<>();

    public Parameter(String query) {
        if (query.isEmpty() || query == null)
            return;
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
}
