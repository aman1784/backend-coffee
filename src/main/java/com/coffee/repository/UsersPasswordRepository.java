package com.coffee.repository;

import com.coffee.entity.UsersPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersPasswordRepository extends JpaRepository<UsersPasswordEntity, Long> {

    @Query(value = "SELECT * FROM passwords WHERE (email_id = ?1 OR phone_number = ?2) AND status = ?3", nativeQuery = true)
    UsersPasswordEntity findByEmailIdOrPhoneNumberAndStatus(String emailIdOrPhoneNumber, String emailIdOrPhoneNumber1, int status);

    UsersPasswordEntity findByEmailIdAndStatus(String emailId, int status);

    UsersPasswordEntity findByPhoneNumberAndStatus(String phoneNumber, int status);

    UsersPasswordEntity findByUserKeyAndStatus(String userKey, int status);

    @Query(value = "SELECT * FROM passwords WHERE email_id = ?1 AND status = ?2", nativeQuery = true)
    Optional<UsersPasswordEntity> findByEmailAndStatus(String username, int status);
}
