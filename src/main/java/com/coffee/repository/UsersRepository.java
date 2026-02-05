package com.coffee.repository;

import com.coffee.entity.UsersEntity;
import com.coffee.projection.CheckUserEmailOrMobileExistsProjection;
import com.coffee.projection.CheckUserIdProjection;
import com.coffee.projection.CheckUserProjection;
import com.coffee.projection.CheckUserNameProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

    // METHOD 1 -> USING PROJECTION
    @Query(value = "SELECT u.user_name FROM users u WHERE user_name = ?1 AND status = ?2", nativeQuery = true)
    CheckUserNameProjection findByUserNameAndStatus(@Param("userName") String userName, @Param("status") int status);

    // METHOD 2 -> USING DTO
    @Query(value = "SELECT u.user_name FROM users u WHERE user_name = ?1 AND status = 1", nativeQuery = true)
    UsersEntity findByUserName(String userName);

    @Query(value = "SELECT * FROM users WHERE (email_id = :emailId OR phone_number = :phone) AND status = 1", nativeQuery = true)
    List<UsersEntity> findIfAnyDataAlreadyExists(@Param("emailId") String emailId, @Param("phone") String phone);

    @Query(value = "SELECT * FROM users WHERE (email_id = ?1 OR phone_number = ?2) AND status = ?3", nativeQuery = true)
    UsersEntity findByEmailIdOrPhoneNumberAndStatus(String emailIdOrPhoneNumber, String emailIdOrPhoneNumber1, int status);

    UsersEntity findByUserIdAndFullNameAndPhoneNumberAndStatus(String userId, String customerName, String customerPhoneNumber, int status);

    @Query(value = "SELECT users.user_id FROM users WHERE user_id = ?1 AND status = ?2", nativeQuery = true)
    CheckUserIdProjection findByUserIdAndStatus(String userId, int status);

    @Query(value = "SELECT email_id as emailId, phone_number as phoneNumber FROM users WHERE (email_id = ?1 OR phone_number = ?2) AND status = ?3", nativeQuery = true)
    CheckUserEmailOrMobileExistsProjection findProjectionByEmailIdOrPhoneNumberAndStatus(String emailId, String phoneNumber, int status);

    UsersEntity findByUserKeyAndStatus(String userKey, int status);

    Optional<UsersEntity> findByIdAndStatus(Long id, int status);

    @Query(value = "SELECT user_name FROM users WHERE status = 1", nativeQuery = true)
    List<String> findAllUsernames();

    @Query(value = "SELECT users.user_key, users.user_id, users.full_name FROM users WHERE user_key = ?1 AND status = ?2", nativeQuery = true)
    CheckUserProjection findProjectionByUserKeyAndStatus(String userKey, int status);
}
