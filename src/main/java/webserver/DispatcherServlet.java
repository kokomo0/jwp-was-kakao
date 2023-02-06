package webserver;

import controller.Controller;
import webserver.http.Request;

import static utils.FileIoUtils.exists;

public class DispatcherServlet {
    private HandlerMapping handlerMapping = new HandlerMapping();

    public Controller mapController(Request request) {
        String uri = request.get("path");
        if(exists(uri)) {
            return handlerMapping.getController("static");
        }
        return handlerMapping.getController("user");
    }
}
