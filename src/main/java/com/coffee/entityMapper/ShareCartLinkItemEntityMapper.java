package com.coffee.entityMapper;

import com.coffee.dto.ShareCartLinkItemDto;
import com.coffee.entity.ShareCartLinkItemEntity;
import org.mapstruct.*;

import java.util.List;

/**
 * @author Aman Kumar Seth
 * @since 27-01-2026
 */

@Mapper(componentModel = "spring")
public interface ShareCartLinkItemEntityMapper {

    ShareCartLinkItemDto buildDto(ShareCartLinkItemEntity entity);

    @Mapping(source = "id", target = "id", ignore = true)
    ShareCartLinkItemEntity buildEntity(ShareCartLinkItemDto dto);

    List<ShareCartLinkItemDto> buildDtos(List<ShareCartLinkItemEntity> entities);

    @Mapping(source = "id", target = "id", ignore = true)
    List<ShareCartLinkItemEntity> buildEntities(List<ShareCartLinkItemDto> dtos);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ShareCartLinkItemDto dto, @MappingTarget ShareCartLinkItemEntity entity);

}
