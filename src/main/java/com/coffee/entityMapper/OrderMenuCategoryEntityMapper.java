package com.coffee.entityMapper;

import com.coffee.dto.OrderMenuCategoryDto;
import com.coffee.entity.OrderMenuCategoryEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMenuCategoryEntityMapper {

    OrderMenuCategoryDto buildDto(OrderMenuCategoryEntity entity);

    @Mapping(target = "id", ignore = true)
    OrderMenuCategoryEntity buildEntity(OrderMenuCategoryDto dto);

    List<OrderMenuCategoryDto> buildDtos(List<OrderMenuCategoryEntity> entities);

    @Mapping(target = "id", ignore = true)
    List<OrderMenuCategoryEntity> buildEntities(List<OrderMenuCategoryDto> dtos);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(OrderMenuCategoryDto dto, @MappingTarget OrderMenuCategoryEntity entity);
}