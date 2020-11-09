package com.atguigu.eduoss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.commonutils.AccessKeyUtil;
import com.atguigu.eduoss.service.OssService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileOss(MultipartFile file) {
        try {
            //        第一步 定义需要固定值：地域节点，id，秘钥
            // Endpoint以杭州为例，其它Region请按实际情况填写。
            String endpoint = AccessKeyUtil.ENDPOINT;
            String accessKeyId = AccessKeyUtil.ACCESSKEY_ID;
            String accessKeySecret = AccessKeyUtil.ACCESSKEY_SECRET;
            String bucketName = AccessKeyUtil.BUCKET_NAME;
//        第二步 创建OSS对象，传递三个固定值
            OSS ossClient = new OSSClientBuilder()
                    .build(endpoint, accessKeyId, accessKeySecret);
//        第三步 调用oss对象的方法实现具体操作
            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            //文件名称里面添加uuid值，保证每个文件名称不一样
            filename = UUID.randomUUID().toString()
                    .replaceAll("-", "") + filename;
            //根据当前日期创建文件夹，存储文件
            //   2020/10/26
            String filePath = new DateTime().toString("yyyy/MM/dd");
            //  /2020/10/26/1.jpg
            filename = filePath + "/" + filename;
            //三个参数
            //第一个参数 bucket名称 ，第二个参数 文件路径和名称  /2020/10/26/1.jpg  ,第三个参数 输入流
            ossClient.putObject(bucketName, filename, inputStream);
            ossClient.shutdown();

            String url = "https://" + bucketName + "." + endpoint + "/" + filename;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
