package com.coffee.entityMapper;

import com.coffee.dto.OrderMenuItemVariantDto;
import com.coffee.entity.OrderMenuItemVariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMenuItemVariantEntityMapper {

    @Mapping(target = "id", ignore = true)
    OrderMenuItemVariantEntity buildEntity(OrderMenuItemVariantDto dto);

    OrderMenuItemVariantDto buildDto(OrderMenuItemVariantEntity entity);

    @Mapping(target = "id", ignore = true)
    List<OrderMenuItemVariantEntity> buildEntities(List<OrderMenuItemVariantDto> dtos);

    List<OrderMenuItemVariantDto> buildDtos(List<OrderMenuItemVariantEntity> entities);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(OrderMenuItemVariantDto dto, @MappingTarget OrderMenuItemVariantEntity entity);
}
