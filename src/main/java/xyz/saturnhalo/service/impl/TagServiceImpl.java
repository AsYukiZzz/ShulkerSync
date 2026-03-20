package xyz.saturnhalo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.saturnhalo.anno.DistributedLock;
import xyz.saturnhalo.domain.dto.ChildTagCreateReq;
import xyz.saturnhalo.domain.dto.RootTagCreateReq;
import xyz.saturnhalo.domain.entity.TagNode;
import xyz.saturnhalo.domain.entity.TagTree;
import xyz.saturnhalo.enums.LockType;
import xyz.saturnhalo.exception.BusinessException;
import xyz.saturnhalo.mapper.TagMapper;
import xyz.saturnhalo.repository.TagNodeRepository;
import xyz.saturnhalo.repository.TagTreeRepository;
import xyz.saturnhalo.service.TagService;
import xyz.saturnhalo.utils.IdUtils;
import xyz.saturnhalo.utils.TreePathUtils;

@Slf4j
@Service
public class TagServiceImpl extends ServiceImpl<TagNodeRepository, TagNode> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagNodeRepository tagNodeRepository;

    @Autowired
    private TagTreeRepository tagTreeRepository;

    /**
     * 创建根标签
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRootTag(RootTagCreateReq req) {
        log.info("开始处理根标签创建请求，标签基本信息={}", req);

        // 1. 创建 TagTree 并持久化
        TagTree tagTree = new TagTree();
        Long treeId = IdUtils.nextId();
        tagTree.setId(treeId);
        tagTree.setVersion(0L);
        tagTreeRepository.insert(tagTree);

        // 2. 初始化 TagNode
        TagNode tagNode = tagMapper.toTagNode(req);
        Long nodeId = IdUtils.nextId();
        tagNode.setId(nodeId);
        tagNode.setTreeId(treeId);
        tagNode.setParentId(-1L);
        tagNode.setPath(TreePathUtils.buildPath(tagNode.getId()));

        // 3. 持久化 TagNode
        try {
            tagNodeRepository.insert(tagNode);
            log.info("根标签{}已被持久化", tagNode);
        } catch (DataIntegrityViolationException e) {
            log.warn("顶级标签{}持久化失败，该层级下已存在同名标签", tagNode);
            throw new BusinessException("顶级标签持久化失败，该层级下已存在同名标签");
        }
    }

    /**
     * 添加子标签
     */
    @Override
    @DistributedLock(
            key = "'lock:tags:' + #req.treeId",
            type = LockType.WRITE
    )
    public void addChildTag(ChildTagCreateReq req) {
        log.info("开始处理子标签创建请求，标签基本信息={}", req);

        // 1. 验证并更新乐观锁版本
        int affectRows = tagTreeRepository.casUpdateTreeVersion(req.getTreeId(), req.getVersion());
        if (affectRows == 0) {
            log.warn("插入子标签={}失败：标签数据已过时", req);
            throw new BusinessException("插入子标签失败：标签数据已过时");
        }

        // 2. 获取父标签信息
        TagNode parentTagNode = tagNodeRepository.selectById(req.getParentId());
        if (parentTagNode == null) {
            log.warn("插入子标签失败：指定的父标签id={}不存在", req.getParentId());
            throw new BusinessException("插入子标签失败：指定的父标签不存在");
        }

        // 3. 健壮性跨树校验
        if (!parentTagNode.getTreeId().equals(req.getTreeId())) {
            log.warn("插入子标签失败：标签数据已过时，指定的父节点id={}不属于当前标签树id={}", parentTagNode.getId(), req.getTreeId());
            throw new BusinessException("插入子标签失败：标签数据已过时");
        }

        // 4. 对象转换
        TagNode tagNode = tagMapper.toTagNode(req);

        // 5. 填充属性
        Long id = IdUtils.nextId();
        tagNode.setId(id);
        tagNode.setPath(TreePathUtils.buildPath(id, parentTagNode.getPath()));

        // 3.2.5 持久化子标签
        try {
            tagNodeRepository.insert(tagNode);
            log.info("子标签{}已被持久化", tagNode);
        } catch (DataIntegrityViolationException e) {
            log.warn("子标签{}持久化失败，该层级下已存在同名标签", tagNode);
            throw new BusinessException("子标签持久化失败，该层级下已存在同名标签");
        }
    }
}