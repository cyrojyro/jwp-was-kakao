package controller;

import model.HttpRequest;
import model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class ViewController extends Controller {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    {
        setBasePath("");
        putHandler("/js/.*", "GET", this::handleFile);
        putHandler("/css/.*", "GET", this::handleFile);
        putHandler("/.*", "GET", this::handleView);
    }

    public void handleFile(HttpRequest request, OutputStream out) throws URISyntaxException, IOException {
        HttpResponse.of(out).forward("./static", request.getPath());
    }

    public void handleView(HttpRequest request, OutputStream out) throws URISyntaxException, IOException {
        HttpResponse.of(out).forward("./templates",
                (request.getPath().equals("/") ? "/index.html" : request.getPath()));
    }
}
