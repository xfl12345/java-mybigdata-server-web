package cc.xfl12345.mybigdata.server.web.controller;

import cc.xfl12345.mybigdata.server.common.data.source.StringTypeSource;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Slf4j
@Controller
@RequestMapping(value = "debug")
public class DebugController implements ApplicationContextAware {
    protected ApplicationContext applicationContext;

    @Autowired
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String debugView() {
        try {
            StringTypeSource stringTypeSource = applicationContext.getBean(
                StringTypeSource.class
            );
            log.debug(JSON.toJSONString(stringTypeSource.selectId("text")));

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return "debug";
    }

    @RequestMapping(value = "call-test", method = RequestMethod.GET)
    @ResponseBody
    public String callTest(HttpServletRequest request) {
        File file = new File(getClass().getClassLoader().getResource("/").getFile());
        File targetDir = file.getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile();
        File[] subFiles = targetDir.listFiles();
        JSONObject jsonObject = new JSONObject();
        for (File subFile : subFiles) {
            jsonObject.put(subFile.getName(), subFile.getPath());
        }
        return JSON.toJSONString(jsonObject, JSONWriter.Feature.PrettyFormat);
    }
}