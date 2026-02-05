package com.coffee.entityMapper;

import com.coffee.dto.CreateOrderItemDto;
import com.coffee.entity.CreateOrderItemEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreateOrderItemEntityMapper {

    CreateOrderItemDto buildDto(CreateOrderItemEntity entity);

    @Mapping(target = "id", ignore = true)
    CreateOrderItemEntity buildEntity(CreateOrderItemDto dto);

    List<CreateOrderItemDto> buildDtos(List<CreateOrderItemEntity> entities);

    @Mapping(target = "id", ignore = true)
    List<CreateOrderItemEntity> buildEntities(List<CreateOrderItemDto> dtos);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CreateOrderItemDto dto, @MappingTarget CreateOrderItemEntity entity);
}
