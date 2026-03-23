package xyz.saturnhalo.mapper;

import org.mapstruct.Mapper;
import xyz.saturnhalo.domain.dto.ModpackCreateReq;
import xyz.saturnhalo.domain.entity.Modpack;

/**
 * 整合包对象转换器
 */
@Mapper(componentModel = "spring")
public interface ModpackMapper {

    /**
     * ModpackCreateReq(DTO) -> Entity
     */
    Modpack toEntity(ModpackCreateReq req);
}