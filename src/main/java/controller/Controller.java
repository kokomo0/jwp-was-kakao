package controller;

import java.util.Map;

public interface Controller {
    byte[] mapRoute(String path, Map<String, String> params);
}
