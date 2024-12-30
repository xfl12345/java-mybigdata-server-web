package cc.xfl12345.mybigdata.server.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Controller
public class RedirectController {
    protected Environment environment;

    public Environment getEnvironment() {
        return environment;
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    protected String redirectPath;

    @PostConstruct
    public void init() throws URISyntaxException {
        String webuiPath = environment.getProperty("app.webui.servlet-path", "webui/");
        if ("".equals(webuiPath)) {
            redirectPath = "./index.html";
        } else {
            redirectPath = new URI("./" + webuiPath).resolve("./index.html").toString();
        }
    }

    @GetMapping(path = {"", "index"})
    public void redirectIndexPage(HttpServletResponse response) throws IOException {
        response.sendRedirect(redirectPath);
    }
}
