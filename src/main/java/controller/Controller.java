package controller;

import java.util.List;
import java.util.Map;

public interface Controller {
    byte[] mapRoute(String method, String path, Map<String, String> params);
}
