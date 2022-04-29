package cc.xfl12345.mybigdata.server.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class FileTransformController {


    @RequestMapping(value = "thunderUpload", method = RequestMethod.POST)
    public void thunderUpload(
        @RequestParam("fileSize") Long givenFileSize,
        @RequestParam("md5Hex") String givenMD56Hex,
        @RequestParam("sha256Hex") String givenSHA256Hex,
        HttpServletRequest request,
        HttpServletResponse response) {

    }

    @RequestMapping(value = "upload")
    public void normalUpload(
        @RequestParam("file") MultipartFile uploadFile,
        HttpServletRequest request,
        HttpServletResponse response) {

    }

    @RequestMapping(value = "download/{directoryId}/{name:.*}", method = RequestMethod.GET)
    public void download(@PathVariable("directoryId") Long directoryId,
                         @PathVariable("name") String name,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception {

    }
}
