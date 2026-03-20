package xyz.saturnhalo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.saturnhalo.domain.entity.TagNode;

/**
 * TagNode 持久层
 */
@Mapper
public interface TagNodeRepository extends BaseMapper<TagNode> {

    /**
     * 根据路径删除标签
     */
    @Delete("delete from tag_node where path like concat(#{path},'%')")
    int deleteTagsByPath(@Param("path") String path);
}