package com.coffee.repository;

import com.coffee.entity.OrderItemRatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aman Kumar Seth
 */

@Repository
public interface OrderItemRatingRepository extends JpaRepository<OrderItemRatingEntity, Long> {

    List<OrderItemRatingEntity> findAllByOrderIdAndStatus(String orderId, int status);

    List<OrderItemRatingEntity> findAllByMenuItemKeyInAndStatus(List<String> menuItemKeys, int status);

    List<OrderItemRatingEntity> findAllByMenuItemKeyAndStatus(String menuItemKey, int status);
}
