package support;

import controller.*;
import controller.annotation.RequestMapping;
import utils.FileIoUtils;
import webserver.http.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static webserver.http.Cookie.SESSION_ID;

public class MethodControllerResolver {
    public HttpResponse process(Controller controller, HttpRequest httpRequest) {
        try {
            Method method = map(controller, httpRequest);
            return invoke(controller, method, httpRequest);
        } catch (Exception e) {
            return ExceptionHandler.handleException(e);
        }
    }

    /**
     * @throws NoSuchElementException: 매핑하려 Method가 존재하지 않을 경우 발생하는 예외 (orElseThrow()에서 던진다)
     */
    private Method map(Controller controller, HttpRequest httpRequest) throws NoSuchElementException {
        return Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .filter(method -> {
                    RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                    return support(requestMapping, httpRequest);
                })
                .findAny().orElseThrow();
    }

    private boolean support(RequestMapping requestMapping, HttpRequest httpRequest) {
        if (!requestMapping.method().equals(httpRequest.getMethod())) return false;
        if (isResourceRequest(httpRequest)) return true;
        return requestMapping.path().equals(httpRequest.getUri().split("\\?")[0]);
    }

    private boolean isResourceRequest(HttpRequest httpRequest) {
        return httpRequest.getMethod().equals("GET") && FileIoUtils.exists(httpRequest.getUri());
    }

    /**
     * @throws InvocationTargetException: method에서 예외가 발생할 경우 던지는 예외 (method.invoke()에서 던진다)
     * @throws IllegalAccessException:    method에 접근할 수 없을 때 발생하는 예외 (method.invoke()에서 던진다)
     */
    private HttpResponse invoke(Controller controller, Method method, HttpRequest httpRequest) throws InvocationTargetException, IllegalAccessException {
        if (controller.equals(HomeController.getInstance()))
            return (HttpResponse) method.invoke(controller);

        Parameter parameter = ParameterWrapper.wrap(httpRequest);
        parameter.add("uri", httpRequest.getUri());
        if (httpRequest.hasCookie())
            parameter.add(SESSION_ID, Cookie.parseCookie(httpRequest.get("Cookie")).get(SESSION_ID));

        return (HttpResponse) method.invoke(controller, parameter);
    }
}
