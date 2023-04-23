package com.base.core.upload.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author glk
 * @date 2023/4/23
 */
@Data
@ConfigurationProperties(prefix = TencentUploadProperties.PREFIX)
public class TencentUploadProperties {

    public static final String PREFIX = "tencent-cos";

    /**
     * url
     */
    private String baseUrl;

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 密码密钥
     */
    private String secretKey;

    /**
     * 位于区域名称
     */
    private String regionName;

    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 文件名前缀
     */
    private String folderPrefix;

}
