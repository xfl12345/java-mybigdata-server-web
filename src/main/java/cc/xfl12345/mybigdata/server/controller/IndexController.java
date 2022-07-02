package cc.xfl12345.mybigdata.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Slf4j
public class IndexController {
    @RequestMapping(path = {"", "index"})
    public void indexView(HttpServletResponse response) throws IOException {
        response.sendRedirect("/index.html");
    }
}
