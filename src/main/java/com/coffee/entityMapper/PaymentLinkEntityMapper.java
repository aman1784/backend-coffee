package com.coffee.entityMapper;

import com.coffee.dto.PaymentLinkDto;
import com.coffee.entity.PaymentLinkEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentLinkEntityMapper {

    @Mapping(source = "id", target = "id", ignore = true)
    PaymentLinkEntity buildEntity(PaymentLinkDto dto);

    PaymentLinkDto buildDto(PaymentLinkEntity entity);

    @Mapping(source = "id", target = "id", ignore = true)
    List<PaymentLinkEntity> buildEntities(List<PaymentLinkDto> dtos);

    List<PaymentLinkDto> buildDtos(List<PaymentLinkEntity> entities);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PaymentLinkDto dto, @MappingTarget PaymentLinkEntity entity);
}
