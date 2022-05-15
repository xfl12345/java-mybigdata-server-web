package cc.xfl12345.mybigdata.server.controller;

import cc.xfl12345.mybigdata.server.service.VfsWebDavService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(WebdavController.VIRTUAL_SERVLET_PATH)
@Slf4j
public class WebdavController {
    public final static String VIRTUAL_SERVLET_PATH = "webdav";

    @Autowired
    protected VfsWebDavService vfsWebDavService;

    @RequestMapping("/**")
    public void vfsWebdav(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request) {
            @Override
            public String getServletPath() {
                return '/' + VIRTUAL_SERVLET_PATH;
            }
        };
        String range = request.getHeader("Content-Range");
        if (range == null) {
            range = request.getHeader("Range");
        }
        if(!StringUtils.isEmpty(range)) {
            log.info(request.getRemoteAddr() + " request for range=" + range);
        }
        vfsWebDavService.service(wrapper, response);
    }
}
