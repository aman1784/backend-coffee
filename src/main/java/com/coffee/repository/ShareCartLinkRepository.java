package com.coffee.repository;

import com.coffee.entity.ShareCartLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Aman Kumar Seth
 * @since 26-01-2026
 */

@Repository
public interface ShareCartLinkRepository extends JpaRepository<ShareCartLinkEntity, Long> {

    ShareCartLinkEntity findByShareCartLinkAndStatus(String shareLink, int status);
}
