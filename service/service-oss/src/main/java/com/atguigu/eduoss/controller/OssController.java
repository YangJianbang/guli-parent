package com.atguigu.eduoss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.eduoss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/eduoss/oss")
public class OssController {
    @Autowired
    OssService ossService;
    @PostMapping("upload")
    public R uploadFile(@RequestParam("file") MultipartFile file){
        String url = ossService.uploadFileOss(file);
        return R.ok().data("url",url);
    }
}
