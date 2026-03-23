package xyz.saturnhalo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.saturnhalo.domain.dto.ModpackCreateReq;
import xyz.saturnhalo.domain.entity.Modpack;
import xyz.saturnhalo.domain.entity.ModpackTagNodeMapping;
import xyz.saturnhalo.exception.BusinessException;
import xyz.saturnhalo.mapper.ModpackMapper;
import xyz.saturnhalo.repository.ModpackRepository;
import xyz.saturnhalo.service.ModpackService;
import xyz.saturnhalo.service.ModpackTagNodeService;
import xyz.saturnhalo.utils.IdUtils;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 整合包相关功能实现类
 */
@Slf4j
@Service
public class ModpackServiceImpl extends ServiceImpl<ModpackRepository, Modpack> implements ModpackService {

    @Autowired
    private ModpackMapper modpackMapper;

    @Autowired
    private ModpackRepository modpackRepository;

    @Autowired
    private ModpackTagNodeService modpackTagNodeService;

    /**
     * 创建整合包
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addModpack(ModpackCreateReq req) {
        log.info("开始处理整合包创建请求，整合包基本信息={}", req);

        // 1. 对象转换
        Modpack modpack = modpackMapper.toEntity(req);

        // 2. 手动获取整合包 Id
        Long modpackId = IdUtils.nextId();
        modpack.setId(modpackId);

        // 3. 持久化整合包信息
        try {
            modpackRepository.insert(modpack);
            log.info("整合包slug={}持久化成功", modpack.getSlug());
        } catch (DataIntegrityViolationException e) {
            log.warn("整合包slug={}持久化失败：已存在相同标识符的整合包信息", modpack.getSlug());
            throw new BusinessException("整合包持久化失败：已存在相同标识符的整合包信息");
        }

        // 4. 封装 Modpack-TagNode 映射集合
        Collection<ModpackTagNodeMapping> mappingCollection = req.getTagIds().stream().map((o) -> new ModpackTagNodeMapping(IdUtils.nextId(), modpackId, o)).collect(Collectors.toSet());

        // 5. 持久化整合包标签映射
        modpackTagNodeService.saveBatch(mappingCollection);

        log.info("整合包slug={}创建成功", modpack.getSlug());
    }
}