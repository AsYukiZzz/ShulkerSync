package xyz.saturnhalo.mapper;

import org.mapstruct.Mapper;
import xyz.saturnhalo.domain.dto.ChildTagCreateReq;
import xyz.saturnhalo.domain.dto.RootTagCreateReq;
import xyz.saturnhalo.domain.entity.TagNode;

/**
 * 标签对象转换器
 */
@Mapper(componentModel = "spring")
public interface TagMapper {

    /**
     * RootTagCreateReq(DTO) -> TagNode(Entity)
     */
    TagNode toTagNode(RootTagCreateReq req);

    /**
     * ChildTagCreateReq(DTO) -> TagNode(Entity)
     */
    TagNode toTagNode(ChildTagCreateReq req);
}