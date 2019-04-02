package cqjtu.ds.yun.service.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Configuration
/**
 * 配置类（获取配置文件里的信息）
 */
public class ConstantConfig {
    @Value("${lximage.endpoint}")
    private String LXIMAGE_END_POINT;
    @Value("${lximage.keyid}")
    private String LXIMAGE_ACCESS_KEY_ID;
    @Value("${lximage.keysecret}")
    private String LXIMAGE_ACCESS_KEY_SECRET;
    @Value("${lximage.filehost}")
    private String LXIMAGE_FILE_HOST;
    @Value("${lximage.bucketname1}")
    private String LXIMAGE_BUCKET_NAME1;
}
