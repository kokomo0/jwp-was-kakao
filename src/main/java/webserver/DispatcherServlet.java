package webserver;

import controller.Controller;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import static utils.FileIoUtils.exists;

public class DispatcherServlet {
    private final HandlerMapping handlerMapping = new HandlerMapping();

    private Controller mapController(String uri) {
        if(exists(uri)) {
            return handlerMapping.getController("static");
        }
        return handlerMapping.getController("user");
    }

    public HttpResponse process(HttpRequest httpRequest) {
        String uri = httpRequest.getUri();
        Controller controller = mapController(uri);
        return controller.mapRoute(httpRequest);
    }
}
