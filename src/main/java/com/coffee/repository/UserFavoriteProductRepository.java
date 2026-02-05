package com.coffee.repository;

import com.coffee.entity.UserFavoriteProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoriteProductRepository extends JpaRepository<UserFavoriteProductEntity, Long> {

    List<UserFavoriteProductEntity> findAllByUserIdAndStatus(String userId, int status);

    UserFavoriteProductEntity findByUserIdAndMenuItemKeyAndVariantKeyAndStatus(String userId, String menuItemKey, String variantKey, int status);
}
