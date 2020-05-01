package cn.joylau.code.service;

import cn.joylau.code.conf.Unsplash;
import cn.joylau.code.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Created by joylau on 2020/5/1.
 * cn.joylau.code.service
 * 2587038142.liu@gmail.com
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spider.unsplash", name = "enable", havingValue = "true")
public class UnsplashSpiderService {

    private final Unsplash unsplash;

    private final RestTemplate restTemplate;

    public UnsplashSpiderService(Unsplash unsplash, RestTemplate restTemplate) {
        this.unsplash = unsplash;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        String rootPath = Utils.rootPath(unsplash.getSavePath());
        Arrays.stream(unsplash.getSize()).forEach(size -> {
            File path = new File(rootPath + "/" + size + "/");
            Utils.init(path);
        });
        log.info("======== Unsplash spider service is Read. ========");
    }


    @Scheduled(fixedRateString = "#{@unsplash.fixedRate}")
    public void run() {
        String keywords = String.join(",", unsplash.getKeyword());
        String rootPath = Utils.rootPath(unsplash.getSavePath());
        Arrays.stream(unsplash.getSize()).forEach(size -> {
            String url = unsplash.getUrl() + "/" + size + "/?" + keywords;
            File out = new File(rootPath + "/" + size + "/" + Utils.fileName());
            try {
                ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
                byte[] bytes = response.getBody();
                assert bytes != null;
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
                ImageIO.write(bufferedImage, "jpeg", Files.newOutputStream(out.toPath()));
            } catch (Exception e) {
                log.error("ERROR: {}, {}", e.getMessage(), e);
            }
        });
    }
}
