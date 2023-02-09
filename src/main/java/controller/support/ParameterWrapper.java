package controller.support;

import webserver.http.HttpRequest;

public class ParameterWrapper {
    public static Parameter wrap(HttpRequest request) {
        String query = getQuery(request);
        if (query == null) return null;
        return new Parameter(query);
    }

    private static String getQuery(HttpRequest request) {
        if (request.getUri().contains("?"))
            return request.getUri().split("\\?")[1];
        if (!request.getBody().isEmpty() && request.getBody() != null)
            return request.getBody();
        return null;
    }
}
