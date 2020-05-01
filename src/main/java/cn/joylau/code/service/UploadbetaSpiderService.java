package cn.joylau.code.service;

import cn.joylau.code.conf.Uploadbeta;
import cn.joylau.code.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;

/**
 * Created by joylau on 2020/5/1.
 * cn.joylau.code.service
 * 2587038142.liu@gmail.com
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spider.uploadbeta", name = "enable", havingValue = "true")
public class UploadbetaSpiderService {

    private final Uploadbeta uploadbeta;

    private final RestTemplate restTemplate;

    public UploadbetaSpiderService(Uploadbeta uploadbeta, RestTemplate restTemplate) {
        this.uploadbeta = uploadbeta;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        String rootPath = Utils.rootPath(uploadbeta.getSavePath());
        File path = new File(rootPath + "/1920x1080/");
        Utils.init(path);
        log.info("======== Uploadbeta spider service is Read. ========");
    }


    @Scheduled(fixedRateString = "#{@uploadbeta.fixedRate}")
    public void run() {
        String rootPath = Utils.rootPath(uploadbeta.getSavePath());
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("authority", "uploadbeta.com");
            headers.set("pragma", "no-cache");
            headers.set("cache-control", "no-cache");
            headers.set("upgrade-insecure-requests", "1");
            headers.set("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36");
            headers.set("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            headers.set("sec-fetch-site", "none");
            headers.set("sec-fetch-mode", "navigate");
            headers.set("sec-fetch-user", "?1");
            headers.set("sec-fetch-dest", "document");
            headers.set("accept-language", "zh-CN,zh;q=0.9,en;q=0.8");
            ResponseEntity<byte[]> response = restTemplate.exchange(uploadbeta.getUrl(), HttpMethod.GET, new HttpEntity<>(headers), byte[].class);
            byte[] bytes = response.getBody();
            File out = new File(rootPath + "/1920x1080/" + Utils.fileName());
            assert bytes != null;
            FileCopyUtils.copy(bytes, Files.newOutputStream(out.toPath()));
            if (log.isDebugEnabled()) {
                log.debug("======== success! file name is : {} ========", out.getPath());
            }
        } catch (Exception e) {
            log.error("ERROR: {}, {}", e.getMessage(), e);
        }
    }
}
