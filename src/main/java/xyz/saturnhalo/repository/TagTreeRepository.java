package xyz.saturnhalo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import xyz.saturnhalo.domain.entity.TagTree;

/**
 * TagTree 持久层
 */
@Mapper
public interface TagTreeRepository extends BaseMapper<TagTree> {

    /**
     * CAS 方式更新标签树版本号
     */
    @Update("update tag_tree set version = version + 1 where id = #{treeId} and version = #{version}")
    int casUpdateTreeVersion(@Param("treeId") Long treeId, @Param("version") Integer version);
}