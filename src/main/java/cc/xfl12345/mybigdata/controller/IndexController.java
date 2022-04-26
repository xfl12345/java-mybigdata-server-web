package cc.xfl12345.mybigdata.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class IndexController {
    @RequestMapping(path = {"", "index", "index.html"})
    public String indexView(HttpServletRequest request) {
        return "index";
    }

    @RequestMapping("hello_springmvc")
    public ModelAndView helloSpringmvcView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hello_springmvc");
        modelAndView.addObject("title", "这是个可以在Controller里改的Title");
        modelAndView.addObject("msg", "Hello SpringMVC!");


        return modelAndView;
    }
}
