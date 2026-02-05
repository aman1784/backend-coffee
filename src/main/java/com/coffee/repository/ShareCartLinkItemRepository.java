package com.coffee.repository;

import com.coffee.entity.ShareCartLinkItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aman Kumar Seth
 * @since 27-01-2026
 */

@Repository
public interface ShareCartLinkItemRepository extends JpaRepository<ShareCartLinkItemEntity, Long> {

    List<ShareCartLinkItemEntity> findAllByShareCartLinkIdAndStatus(Long userId, int status);
}
