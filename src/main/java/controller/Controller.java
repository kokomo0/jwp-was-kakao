package controller;

import webserver.http.Response;

import java.util.List;
import java.util.Map;

public interface Controller {
    Response mapRoute(String method, String path, Map<String, String> params) throws Exception;
}
