package cc.xfl12345.mybigdata.server.web.controller.restful.vfs;

import cc.xfl12345.mybigdata.server.common.appconst.api.result.JsonApiResult;
import cc.xfl12345.mybigdata.server.common.web.pojo.response.JsonApiResponseData;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import cc.xfl12345.mybigdata.server.web.model.uri.JarFileURIRelativizeImpl;
import cc.xfl12345.mybigdata.server.web.model.uri.URIRelativize;
import cn.hutool.core.io.CharsetDetector;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(ApiConst.BACKEND_PATH_BASE + '/' + RamFileSystemController.servletPath)
public class RamFileSystemController {
    public static final String servletPath = "/vfs/ramfs";

    @Getter
    protected RamFileSystem ramFileSystem;

    @Autowired
    public void setRamFileSystem(RamFileSystem ramFileSystem) {
        this.ramFileSystem = ramFileSystem;
    }

    protected URIRelativize uriRelativize = new JarFileURIRelativizeImpl();

    @GetMapping("files/**")
    public JsonApiResponseData list(HttpServletRequest request) {
        JsonApiResponseData responseObject = new JsonApiResponseData();
        // String[] requestPaths = StringUtils.split(request.getServletPath(), '/');
        String requestPath = request.getServletPath().substring((servletPath + "/files").length());

        try {
            FileObject ramfsRoot = ramFileSystem.getRoot();
            FileObject targetFile = ramFileSystem.resolveFile(requestPath);
            if (targetFile.isFile()) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("fileName", targetFile.getName().getBaseName());
                map.put(
                    "relativePath",
                    uriRelativize.getRelativizeURI(ramfsRoot.getURI(), targetFile.getURI())
                );
                map.put("isFile", targetFile.isFile());
                InputStream inputStream = targetFile.getContent().getInputStream();
                map.put(
                    "content",
                    targetFile.getContent().getString(CharsetDetector.detect(inputStream))
                );
                inputStream.close();
                responseObject.setData(map);
            } else {
                FileObject[] fileObjects = targetFile.getChildren();
                List<Object> jsonArray = new ArrayList<>(fileObjects.length);
                for (FileObject fileObject: fileObjects) {
                    HashMap<String, Object> jsonObject = new HashMap<>();
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
