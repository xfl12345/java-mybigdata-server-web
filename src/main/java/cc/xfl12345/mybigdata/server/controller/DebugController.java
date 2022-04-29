package cc.xfl12345.mybigdata.server.controller;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Crypt;
import org.apache.commons.crypto.Crypto;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.teasoft.bee.osql.PreparedSql;
import org.teasoft.honey.osql.core.BeeFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Controller
@RequestMapping(value = "debug")
public class DebugController implements ApplicationContextAware {
    protected ApplicationContext applicationContext;

    @Autowired
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String debugView() {
        DruidDataSource dataSource = applicationContext.getBean(DruidDataSource.class);
        try {
            PreparedSql preparedSql = BeeFactory.getHoneyFactory().getPreparedSql();

            log.info(String.valueOf(preparedSql.select("select count(global_id) from xfl_mybigdata.global_data_record").get(0)));
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
        return JSON.toJSONString(jsonObject, true);
    }
}
