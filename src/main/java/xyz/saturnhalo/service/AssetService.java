package xyz.saturnhalo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.saturnhalo.domain.entity.Asset;

import java.util.List;
import java.util.Set;

/**
 *
 */
public interface AssetService extends IService<Asset> {

    /**
     * 批量查询 Hash 是否存在
     */
    Set<String> getExistingHashList(List<String> hashList);
}