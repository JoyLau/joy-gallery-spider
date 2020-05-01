package cn.joylau.code.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by joylau on 2020/5/1.
 * cn.joylau.code.conf
 * 2587038142.liu@gmail.com
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spider.uploadbeta")
public class Uploadbeta {

    private Boolean enable;

    private String url;

    private String fixedRate;

    private String savePath;

}
