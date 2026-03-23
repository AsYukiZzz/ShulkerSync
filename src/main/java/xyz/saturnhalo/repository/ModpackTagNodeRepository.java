package xyz.saturnhalo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.saturnhalo.domain.entity.ModpackTagNodeMapping;

/**
 * Modpack-TagNode 映射关系持久层
 */
@Mapper
public interface ModpackTagNodeRepository extends BaseMapper<ModpackTagNodeMapping> {

}