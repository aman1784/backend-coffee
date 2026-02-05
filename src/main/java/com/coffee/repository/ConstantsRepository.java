package com.coffee.repository;

import com.coffee.entity.ConstantsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstantsRepository extends JpaRepository<ConstantsEntity, Long> {

    ConstantsEntity findByKeyAndStatus(String key, int status);
}
