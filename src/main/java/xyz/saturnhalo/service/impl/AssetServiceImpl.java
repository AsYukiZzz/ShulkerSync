package xyz.saturnhalo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.saturnhalo.domain.entity.Asset;
import xyz.saturnhalo.repository.AssetRepository;
import xyz.saturnhalo.service.AssetService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AssetServiceImpl extends ServiceImpl<AssetRepository, Asset> implements AssetService {

    // 分片大小
    private static final int BATCH_SIZE = 1000;

    @Autowired
    private AssetRepository assetRepository;

    /**
     * 批量查询 Hash 是否存在
     */
    @Override
    public Set<String> getExistingHashList(List<String> hashList) {

        // 1. 分片全量哈希列表
        List<List<String>> partition = Lists.partition(hashList, BATCH_SIZE);

        // 2. 查询数据库中存在的哈希
        Set<String> existingHashList = new HashSet<>();
        for (List<String> list : partition) {
            List<String> assets = assetRepository.selectObjs(
                    new LambdaQueryWrapper<Asset>()
                            .in(Asset::getHash, list)
                            .select(Asset::getHash)
            );
            existingHashList.addAll(assets);
        }

        return existingHashList;
    }
}