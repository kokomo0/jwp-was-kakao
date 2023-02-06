package webserver;

import controller.Controller;
import webserver.http.Request;

import static utils.FileIoUtils.exists;

public class DispatcherServlet {
    private final HandlerMapping handlerMapping = new HandlerMapping();

    public Controller mapController(Request request) {
        String uri = request.getUri();
        if(exists(uri)) {
            return handlerMapping.getController("static");
        }
        return handlerMapping.getController("user");
    }
}
