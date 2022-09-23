package cc.xfl12345.mybigdata.server.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class RedirectController {
    @RequestMapping(path = {"", "index"})
    public void redirectBackend(HttpServletResponse response) throws IOException {
        response.sendRedirect("./index.html");
    }
}
