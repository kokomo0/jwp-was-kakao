package webserver;

import controller.Controller;
import webserver.http.Request;
import webserver.http.Response;

import static utils.FileIoUtils.exists;

public class DispatcherServlet {
    private final HandlerMapping handlerMapping = new HandlerMapping();

    private Controller mapController(String uri) {
        if(exists(uri)) {
            return handlerMapping.getController("static");
        }
        return handlerMapping.getController("user");
    }

    public Response process(Request request) {
        String uri = request.getUri();
        Controller controller = mapController(uri);
        return controller.mapRoute(request);
    }
}
