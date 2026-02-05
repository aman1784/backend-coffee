package com.coffee.entityMapper;

import com.coffee.dto.CreateOrderDto;
import com.coffee.entity.CreateOrderEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreateOrderEntityMapper {

    CreateOrderDto buildDto(CreateOrderEntity createOrderEntity);

    @Mapping(source = "id", target = "id", ignore = true)
    CreateOrderEntity buildEntity(CreateOrderDto createOrderDto);

    List<CreateOrderDto> buildDtos(List<CreateOrderEntity> createOrderEntities);

    @Mapping(source = "id", target = "id", ignore = true)
    List<CreateOrderEntity> buildEntities(List<CreateOrderDto> createOrderDtos);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CreateOrderDto dto, @MappingTarget CreateOrderEntity entity);


}
