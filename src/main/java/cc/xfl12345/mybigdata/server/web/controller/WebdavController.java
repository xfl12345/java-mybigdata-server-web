package cc.xfl12345.mybigdata.server.web.controller;

import cc.xfl12345.mybigdata.server.web.pojo.RequestAnalyser;
import cc.xfl12345.mybigdata.server.web.service.VfsWebDavService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ConditionalOnProperty(prefix = "app.service.vfs.webdav", name = "enable", havingValue = "true", matchIfMissing = true)
@Controller
@RequestMapping(WebdavController.VIRTUAL_SERVLET_PATH)
@Slf4j
public class WebdavController {
    public final static String VIRTUAL_SERVLET_PATH = "webdav";

    @Getter
    protected VfsWebDavService vfsWebDavService;

    @Autowired
    public void setVfsWebDavService(VfsWebDavService vfsWebDavService) {
        this.vfsWebDavService = vfsWebDavService;
    }

    @Getter
    protected RequestAnalyser requestAnalyser;

    @Autowired
    public void setRequestAnalyser(RequestAnalyser requestAnalyser) {
        this.requestAnalyser = requestAnalyser;
    }

    @RequestMapping("/**")
    public void vfsWebdav(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request) {
            @Override
            public String getServletPath() {
                return '/' + VIRTUAL_SERVLET_PATH;
            }
        };
        String range = request.getHeader("Content-Range");
        if (StringUtils.isEmpty(range)) {
            range = request.getHeader("Range");
        }
        if (!StringUtils.isEmpty(range)) {
            log.debug(requestAnalyser.getIpAddress(request) + " request for range=" + range);
        }

        vfsWebDavService.service(wrapper, response);
    }
}
