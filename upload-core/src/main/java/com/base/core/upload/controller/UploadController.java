package com.base.core.upload.controller;

import com.base.core.upload.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author glk
 * @date 2023/4/23
 */
@RestController
public class UploadController {

    @Autowired
    private UploadUtil uploadUtil;

    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        return uploadUtil.tencentUpload(file);
    }

}
