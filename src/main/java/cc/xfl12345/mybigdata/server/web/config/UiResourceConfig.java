package cc.xfl12345.mybigdata.server.web.config;

import cn.hutool.core.lang.Validator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.MalformedURLException;

@Configuration
@ConditionalOnProperty(prefix = "app.webui", name = "location")
@ConfigurationProperties(prefix = "app.webui")
public class UiResourceConfig implements WebMvcConfigurer {
    protected Resource resource;

    protected String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        if (location.startsWith("classpath")) {
            resource = new ClassPathResource(location);
        } else if (Validator.isUrl(location)) {
            try {
                resource = new UrlResource(location);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else {
            resource = new FileSystemResource(location);
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/ui/**").addResourceLocations(resource);
    }
}
