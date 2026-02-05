package com.coffee.entityMapper;

import com.coffee.dto.OrderItemRatingDto;
import com.coffee.entity.OrderItemRatingEntity;
import org.mapstruct.*;

import java.util.List;

/**
 * @author Aman Kumar Seth
 */

@Mapper(componentModel = "spring")
public interface OrderItemRatingEntityMapper {

    OrderItemRatingDto buildDto(OrderItemRatingEntity entity);

    @Mapping(source = "id", target = "id", ignore = true)
    OrderItemRatingEntity buildEntity(OrderItemRatingDto dto);

    List<OrderItemRatingDto> buildDtos(List<OrderItemRatingEntity> entities);

    @Mapping(target = "id", ignore = true)
    List<OrderItemRatingEntity> buildEntities(List<OrderItemRatingDto> dtos);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(OrderItemRatingDto dto, @MappingTarget OrderItemRatingEntity entity);
}
