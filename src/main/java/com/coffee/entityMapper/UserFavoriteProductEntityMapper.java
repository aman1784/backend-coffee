package com.coffee.entityMapper;

import com.coffee.dto.UserFavoriteProductDto;
import com.coffee.entity.UserFavoriteProductEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserFavoriteProductEntityMapper {

    UserFavoriteProductDto buildDto(UserFavoriteProductEntity entity);

    @Mapping(source = "id", target = "id", ignore = true)
    UserFavoriteProductEntity buildEntity(UserFavoriteProductDto dto);

    List<UserFavoriteProductDto> buildDtos(List<UserFavoriteProductEntity> entities);

    @Mapping(source = "id", target = "id", ignore = true)
    List<UserFavoriteProductEntity> buildEntities(List<UserFavoriteProductDto> dtos);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserFavoriteProductDto dto, @MappingTarget UserFavoriteProductEntity entity);
}
