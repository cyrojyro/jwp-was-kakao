package controller.error;

import controller.handler.ErrorHandler;
import model.HttpRequest;
import model.HttpResponse;

public class RuntimeExceptionHandler implements ErrorHandler {
    @Override
    public HttpResponse handle(HttpRequest request) {
        return new HttpResponse().setStatus(400).setBody("INVALID REQUEST");
    }
}
