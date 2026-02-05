package com.coffee.repository;

import com.coffee.entity.PersistCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersistCartRepository extends JpaRepository<PersistCartEntity, Long> {

    List<PersistCartEntity> findByUserIdAndStatus(String userId, int status);

    Optional<PersistCartEntity> findByIdAndStatus(Long id, int status);
}
