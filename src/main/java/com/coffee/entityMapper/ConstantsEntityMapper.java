package com.coffee.entityMapper;

import com.coffee.dto.ConstantsDto;
import com.coffee.entity.ConstantsEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConstantsEntityMapper {

    ConstantsEntityMapper INSTANCE = Mappers.getMapper(ConstantsEntityMapper.class);

    ConstantsDto buildDto(ConstantsEntity entity);

    @Mapping(source = "id", target = "id", ignore = true)
    ConstantsEntity buildEntity(ConstantsDto dto);

    List<ConstantsDto> buildDtos(List<ConstantsEntity> entities);

    @Mapping(target = "id", ignore = true)
    List<ConstantsEntity> buildEntities(List<ConstantsDto> dtos);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ConstantsDto dto, @MappingTarget ConstantsEntity entity);
}
