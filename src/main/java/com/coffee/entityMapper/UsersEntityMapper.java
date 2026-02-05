package com.coffee.entityMapper;

import com.coffee.dto.UsersDto;
import com.coffee.entity.UsersEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersEntityMapper {

    UsersDto buildDto(UsersEntity entity);

    @Mapping(source = "id", target = "id", ignore = true)
    UsersEntity buildEntity(UsersDto dto);

    List<UsersDto> buildDtos(List<UsersEntity> entities);

    @Mapping(target = "id", ignore = true)
    List<UsersEntity> buildEntities(List<UsersDto> dtos);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UsersDto dto, @MappingTarget UsersEntity entity);

}
