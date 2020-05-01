package cn.joylau.code.rest;

import cn.joylau.code.utils.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by joylau on 2020/5/1.
 * cn.joylau.code.rest
 * 2587038142.liu@gmail.com
 */
@RestController
@RequestMapping("/status")
public class StatusRest {

    @Value("${save.path}")
    private String savePath;

    @RequestMapping()
    public Map<String, Object> statistic() {
        HashMap<String, Object> result = new HashMap<>();
        String rootPath = Utils.rootPath(savePath);
        File root = new File(rootPath);
        result.put("freeSpace", root.getFreeSpace() / 1024 / 1024 / 1024 + "G");
        result.put("usableSpace", root.getUsableSpace() / 1024 / 1024 / 1024 + "G");
        List<Map<String, Object>> childes =
                Arrays.stream(Objects.requireNonNull(root.list()))
                        .filter(path -> new File(rootPath + "/" + path).isDirectory())
                        .map(path -> {
                            HashMap<String, Object> child = new HashMap<>();
                            child.put(path, Objects.requireNonNull(new File(rootPath + "/" + path).listFiles()).length);
                            return child;
                        }).collect(Collectors.toList());
        result.put("childes", childes);
        return result;

    }
}
