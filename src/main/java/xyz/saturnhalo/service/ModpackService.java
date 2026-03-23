package xyz.saturnhalo.service;

import xyz.saturnhalo.domain.dto.ModpackCreateReq;

/**
 * 整合包相关功能接口
 */
public interface ModpackService {

    /**
     * 创建整合包
     */
    void addModpack(ModpackCreateReq req);
}