package com.coffee.entityMapper;

import com.coffee.dto.PersistCartDto;
import com.coffee.entity.PersistCartEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersistCartEntityMapper {

    PersistCartDto buildDto(PersistCartEntity entity);

    @Mapping(source = "id", target = "id", ignore = true)
    PersistCartEntity buildEntity(PersistCartDto dto);

    List<PersistCartDto> buildDtos(List<PersistCartEntity> entities);

    @Mapping(source = "id", target = "id", ignore = true)
    List<PersistCartEntity> buildEntities(List<PersistCartDto> dtos);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PersistCartDto dto, @MappingTarget PersistCartEntity entity);
}
