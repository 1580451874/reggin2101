package com.itheima.reggie.controller;

import com.itheima.reggie.common.OssTemplate;
import com.itheima.reggie.common.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
public class FileController {
    @Autowired
    private OssTemplate ossTemplate;

    @PostMapping("/common/upload")
    public ResultInfo uploadFile(MultipartFile file) throws IOException {
        String filePath = ossTemplate.upload(file.getOriginalFilename(), file.getInputStream());
        log.info("文件上传成功,访问地址是:{}", filePath);
        return ResultInfo.success(filePath);
    }
}
