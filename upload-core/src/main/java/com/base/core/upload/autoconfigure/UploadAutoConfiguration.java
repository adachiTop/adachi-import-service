package com.base.core.upload.autoconfigure;

import com.base.core.upload.UploadUtil;
import com.base.core.upload.properties.TencentUploadProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 卫士通服务
 * @author: yaochaochen
 * @since: 2023/4/18
 */

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(TencentUploadProperties.class)
@ConditionalOnClass(UploadUtil.class)
//判断指定的属性是否具备指定的值
@ConditionalOnProperty(prefix = TencentUploadProperties.PREFIX, value = {"enabled"}, matchIfMissing = true)
public class UploadAutoConfiguration {

    private final TencentUploadProperties properties;

    public UploadAutoConfiguration(TencentUploadProperties secretProperties) {
        this.properties = secretProperties;
    }


    @Bean
    @ConditionalOnMissingBean(UploadUtil.class)
    public UploadUtil westSecretUtil() {
        UploadUtil uploadUtil = new UploadUtil();
        uploadUtil.setProperties(properties);
        return uploadUtil;
    }


}
