package xyz.saturnhalo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.saturnhalo.domain.entity.TagNode;

/**
 * TagNode 持久层
 */
@Mapper
public interface TagNodeRepository extends BaseMapper<TagNode> {

}