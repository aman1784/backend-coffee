package com.coffee.entityMapper;

import com.coffee.dto.UsersPasswordDto;
import com.coffee.entity.UsersPasswordEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersPasswordEntityMapper {

    UsersPasswordDto buildDto(UsersPasswordEntity usersPasswordEntity);

    @Mapping(source = "id", target = "id", ignore = true)
    UsersPasswordEntity buildEntity(UsersPasswordDto usersPasswordDto);

    List<UsersPasswordDto> buildDtos(List<UsersPasswordEntity> usersPasswordEntities);

    @Mapping(source = "id", target = "id", ignore = true)
    List<UsersPasswordEntity> buildEntities(List<UsersPasswordDto> usersPasswordDtos);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UsersPasswordDto dto, @MappingTarget UsersPasswordEntity entity);
}
