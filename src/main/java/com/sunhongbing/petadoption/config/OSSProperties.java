package com.sunhongbing.petadoption.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @className: OSSProperties
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-24 18:18
 */

@Component
@PropertySource("classpath:/aliyun.properties")
public class OSSProperties implements InitializingBean {

    @Value("${aliyun.endpoint}")
    private String aliyun_endpoint;

    @Value("${aliyun.access-id}")
    private String aliyun_access_id;

    @Value("${aliyun.access-key}")
    private String aliyun_access_key;

    @Value("${aliyun.bucket}")
    private String aliyun_bucket;

    @Value("${aliyun.dir}")
    private String aliyun_dir;

    // 声明静态变量
    public static String ALIYUN_ENDPOINT;

    public static String ALIYUN_ACCESS_ID;

    public static String ALIYUN_ACCESS_KEY;

    public static String ALIYUN_BUCKET;

    public static String ALIYUN_DIR;

    @Override
    public void afterPropertiesSet() {
        ALIYUN_ENDPOINT = aliyun_endpoint;
        ALIYUN_ACCESS_ID = aliyun_access_id;
        ALIYUN_ACCESS_KEY = aliyun_access_key;
        ALIYUN_BUCKET = aliyun_bucket;
        ALIYUN_DIR = aliyun_dir;
    }

}
