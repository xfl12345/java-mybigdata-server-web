package cc.xfl12345.mybigdata.server.web.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Configuration
@ConditionalOnProperty(prefix = "app.webui", name = "location")
@ConfigurationProperties(prefix = "app.webui")
@Slf4j
public class UiResourceConfig implements WebMvcConfigurer {
    protected Resource resource;

    @Getter
    @Setter
    protected String pathPattern = "/**";

    protected String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) throws IOException {
        this.location = location;
        if (location.startsWith("classpath")) {
            resource = new ClassPathResource(location);
        } else {
            if (!"".equals(location)) {
                try {
                    resource = new UrlResource(new URL(location));
                } catch (MalformedURLException e) {
                    log.info("[" + location + "] is not a URL link. [java.net.URL]: " + e.getMessage());
                }
            }
            if (resource == null) {
                resource = new FileSystemResource(location);
            }
        }

        log.info("Mapping request: [" + pathPattern + "] <---> [" + resource.getURL() + "]");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(pathPattern).addResourceLocations(resource);
    }
}
