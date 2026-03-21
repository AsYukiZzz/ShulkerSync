package xyz.saturnhalo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.saturnhalo.domain.dto.ChildTagCreateReq;
import xyz.saturnhalo.domain.dto.RootTagCreateReq;
import xyz.saturnhalo.domain.dto.TagDeleteReq;
import xyz.saturnhalo.domain.dto.TagInfoUpdateReq;
import xyz.saturnhalo.domain.entity.TagNode;

/**
 * 标签相关功能接口
 */
public interface TagService extends IService<TagNode> {

    /**
     * 添加根标签
     */
    void addRootTag(RootTagCreateReq req);

    /**
     * 添加子标签
     */
    void addChildTag(ChildTagCreateReq req);

    /**
     * 删除标签
     */
    void removeTag(TagDeleteReq req);

    /**
     * 修改标签(自身)
     */
    void updateTagInfo(TagInfoUpdateReq req);
}