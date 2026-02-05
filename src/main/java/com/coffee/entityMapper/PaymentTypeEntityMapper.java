package com.coffee.entityMapper;

import com.coffee.dto.PaymentTypeDto;
import com.coffee.razorpay.PaymentTypeEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentTypeEntityMapper {

    @Mapping(source = "id", target = "id", ignore = true)
    PaymentTypeEntity buildEntity(PaymentTypeDto dto);

    PaymentTypeDto buildDto(PaymentTypeEntity entity);

    @Mapping(source = "id", target = "id", ignore = true)
    List<PaymentTypeEntity> buildEntities(List<PaymentTypeDto> dtos);

    List<PaymentTypeDto> buildDtos(List<PaymentTypeEntity> entities);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PaymentTypeDto dto, @MappingTarget PaymentTypeEntity entity);
}
