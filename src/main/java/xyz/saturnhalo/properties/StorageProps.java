package xyz.saturnhalo.properties;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 存储路径配置项
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "shulkersync.storage")
public class StorageProps {

    @NotBlank(message = "必须显式配置 shulkersync.storage.base-dir 存储路径")
    private String baseDir;

    private String tempDir;
    private String assetsDir;
    private String configDir;

    @PostConstruct
    public void initDirectories() {
        // 计算对应文件路径
        this.tempDir = baseDir + File.separator + "temp";
        this.assetsDir = baseDir + File.separator + "assets";
        this.configDir = baseDir + File.separator + "config";
    }
}