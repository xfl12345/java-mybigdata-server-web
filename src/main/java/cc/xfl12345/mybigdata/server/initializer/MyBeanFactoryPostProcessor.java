package cc.xfl12345.mybigdata.server.initializer;


import cc.xfl12345.mybigdata.server.model.StaticSpringApp;
import cc.xfl12345.mybigdata.server.plugin.apache.vfs.SpringBootResourceFileProvider;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Objects;

@Component
@Slf4j
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
//    private MockClassLoader mockClassLoader;
//
//    public MockClassLoader getMockClassLoader() {
//        return mockClassLoader;
//    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {


        // 设置 alibaba fastjson 为 默认有序
        JSON.DEFAULT_PARSER_FEATURE = Feature.config(
            JSON.DEFAULT_PARSER_FEATURE,
            Feature.OrderedField,
            true
        );
        // 优先初始化一些 Bean
        configurableListableBeanFactory.createBean(StaticSpringApp.class);
//        configurableListableBeanFactory.createBean(ResourceCacheMapBean.class);
    }
}
