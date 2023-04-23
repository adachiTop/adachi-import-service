package com.base.core.upload;

import com.base.core.upload.properties.TencentUploadProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import org.apache.http.client.utils.DateUtils;
import org.springframework.util.IdGenerator;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 上传文件工具类
 *
 * @author glk
 * @date 2023-04-23
 */
public class UploadUtil {

    private TencentUploadProperties properties;

    public void setProperties(TencentUploadProperties properties) {
        this.properties = properties;
    }

    public String tencentUpload(MultipartFile file) {
        try {
            if (file == null) {
                throw new Exception("文件不能为空");
            }
            String oldFileName = file.getOriginalFilename();
            String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID() + eName;
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DATE);
            // 1 初始化用户身份信息(secretId, secretKey)
            COSCredentials cred = new BasicCOSCredentials(properties.getAccessKey(), properties.getSecretKey());
            // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            ClientConfig clientConfig = new ClientConfig(new Region(properties.getRegionName()));
            // 3 生成cos客户端
            COSClient cosclient = new COSClient(cred, clientConfig);
            // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            String bucketName = properties.getBucketName();

            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
            // 大文件上传请参照 API 文档高级 API 上传
            File localFile = null;
            try {
                localFile = File.createTempFile("temp", null);
                file.transferTo(localFile);
                // 指定要上传到 COS 上的路径
                String key = "/" + properties.getFolderPrefix() + "/" + year + "/" + month + "/" + day + "/" + newFileName;
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
                PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
                return properties.getBaseUrl() + putObjectRequest.getKey();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                // 关闭客户端(关闭后台线程)
                cosclient.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}