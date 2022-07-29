package cc.xfl12345.mybigdata.server.controller.restful.vfs;

import cc.xfl12345.mybigdata.server.appconst.api.result.JsonApiResult;
import cc.xfl12345.mybigdata.server.model.api.response.JsonCommonApiResponseObject;
import cc.xfl12345.mybigdata.server.model.uri.JarFileURIRelativizeImpl;
import cc.xfl12345.mybigdata.server.model.uri.URIRelativize;
import cn.hutool.core.io.CharsetDetector;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.provider.ram.RamFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

@RestController
@Slf4j
@RequestMapping(RamFileSystemController.servletPath)
public class RamFileSystemController {
    public static final String servletPath = "/vfs/ramfs";

    protected String version = "1";

    @Getter
    protected RamFileSystem ramFileSystem;

    @Autowired
    public void setRamFileSystem(RamFileSystem ramFileSystem) {
        this.ramFileSystem = ramFileSystem;
    }

    protected URIRelativize uriRelativize = new JarFileURIRelativizeImpl();

    @GetMapping("files/**")
    public JsonCommonApiResponseObject list(HttpServletRequest request) {
        JsonCommonApiResponseObject responseObject = new JsonCommonApiResponseObject(
            version
        );
        // String[] requestPaths = StringUtils.split(request.getServletPath(), '/');
        String requestPath = request.getServletPath().substring((servletPath + "/files").length());

        try {
            FileObject ramfsRoot = ramFileSystem.getRoot();
            FileObject targetFile = ramFileSystem.resolveFile(requestPath);
            if (targetFile.isFile()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fileName", targetFile.getName().getBaseName());
                jsonObject.put(
                    "relativePath",
                    uriRelativize.getRelativizeURI(ramfsRoot.getURI(), targetFile.getURI())
                );
                jsonObject.put("isFile", targetFile.isFile());
                InputStream inputStream = targetFile.getContent().getInputStream();
                jsonObject.put(
                    "content",
                    targetFile.getContent().getString(CharsetDetector.detect(inputStream))
                );
                inputStream.close();
                responseObject.setData(jsonObject);
            } else {
                JSONArray jsonArray = new JSONArray();
                FileObject[] fileObjects = targetFile.getChildren();
                for (FileObject fileObject: fileObjects) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("fileName", fileObject.getName().getBaseName());
                    jsonObject.put(
                        "relativePath",
                        uriRelativize.getRelativizeURI(ramfsRoot.getURI(), fileObject.getURI())
                    );
                    jsonObject.put("isFile", fileObject.isFile());
                    if (fileObject.isFile()) {
                        jsonObject.put("fileSize", fileObject.getContent().getSize());
                    }
                    jsonArray.add(jsonObject);
                }
                responseObject.setData(jsonArray);
            }

            responseObject.setApiResult(JsonApiResult.SUCCEED);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            responseObject.setApiResult(JsonApiResult.OTHER_FAILED);
        } catch (URISyntaxException e) {
            responseObject.setApiResult(JsonApiResult.FAILED_NOT_FOUND);
        }

        return responseObject;
    }
}
