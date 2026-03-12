package xyz.saturnhalo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;

/**
 * 存储类型枚举
 */
@AllArgsConstructor
public enum StorageType implements BaseEnum {

    // 本地存储
    LOCAL_TOMCAT(10, "本地 Tomcat 服务器存储"),
    LOCAL_NGINX(11, "本地 NGINX 服务器存储"),

    // GitHub 仓库
    GITHUB_REPO(21, "Github 仓库存储"),

    // 自搭建存储方案
    MINIO(31, "自搭建 Minio 存储"),
    RUSTFS(32, "自搭建 RustFS 存储"),

    // 服务商存储方案
    CLOUDFLARE_R2(41, "CloudFlare R2 存储服务"),
    ALIYUN_OSS(42, "阿里云 OSS 存储服务");

    @EnumValue
    private final Integer code;
    private final String displayName;

    @Override
    public Integer getCode() {
        return code;
    }
}