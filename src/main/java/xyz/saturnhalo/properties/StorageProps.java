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

    @PostConstruct
    public void initDirectories() {
        // 计算对应文件路径
        this.tempDir = baseDir + File.separator + "temp";
        this.assetsDir = baseDir + File.separator + "assets";

        // 启动时创建对应文件夹，确保目录存在
        new File(this.tempDir).mkdirs();
        new File(this.assetsDir).mkdirs();
    }
}