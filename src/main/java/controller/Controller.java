package controller;

import webserver.http.Request;
import webserver.http.Response;

public interface Controller {
    Response mapRoute(Request request);
}
