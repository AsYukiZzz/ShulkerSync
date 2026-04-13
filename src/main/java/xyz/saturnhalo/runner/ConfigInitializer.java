package xyz.saturnhalo.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import xyz.saturnhalo.properties.StorageProps;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * 启动初始化
 */
@Slf4j
@Component
public class ConfigInitializer implements ApplicationRunner {

    @Autowired
    private StorageProps storageProps;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 1. 文件夹初始化
        Path tempDir = Path.of(storageProps.getTempDir());
        Path assetsDir = Path.of(storageProps.getAssetsDir());
        Path configDir = Path.of(storageProps.getConfigDir());

        Files.createDirectories(tempDir);
        Files.createDirectories(assetsDir);
        Files.createDirectories(configDir);

        // 2. 检测配置文件是否存在
        Path configFile = configDir.resolve("shulkersync_rules.yaml");
        if (Files.notExists(configFile)) {
            // 2.1 配置文件不存在：复制样本配置文件到配置文件文件夹
            log.info("配置文件不存在，将复制样本配置文件并使用");

            ClassPathResource resource = new ClassPathResource("default-rules.yaml");
            try (InputStream in = resource.getInputStream()) {
                Files.copy(in, configFile, StandardCopyOption.REPLACE_EXISTING);
                log.info("默认配置文件生成成功: {}，请修改配置文件内容并重启", configFile.toAbsolutePath());
            }

            // 2.2 样板配置文件复制完成：关闭虚拟机，让用户编辑配置文件后重启
            System.exit(0);
        }
    }
}