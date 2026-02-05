package com.coffee.entityMapper;

import com.coffee.dto.ShareCartLinkDto;
import com.coffee.entity.ShareCartLinkEntity;
import org.mapstruct.*;

import java.util.List;

/**
 * @author Aman Kumar Seth
 * @since 26-01-2026
 */

@Mapper(componentModel = "spring")
public interface ShareCartLinkEntityMapper {

    ShareCartLinkDto buildDto(ShareCartLinkEntity entity);

    @Mapping(source = "id", target = "id", ignore = true)
    ShareCartLinkEntity buildEntity(ShareCartLinkDto dto);

    List<ShareCartLinkDto> buildDtos(List<ShareCartLinkEntity> entities);

    @Mapping(source = "id", target = "id", ignore = true)
    List<ShareCartLinkEntity> buildEntities(List<ShareCartLinkDto> dtos);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ShareCartLinkDto dto, @MappingTarget ShareCartLinkEntity entity);
}
