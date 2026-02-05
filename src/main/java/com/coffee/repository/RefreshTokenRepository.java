package com.coffee.repository;

import com.coffee.dto.UsersPasswordDto;
import com.coffee.entity.RefreshToken;
import com.coffee.entity.UsersPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String requestRefreshToken);

    // Find by user to delete on login (if you want to allow only one session)
//    @Modifying
//    int deleteByUser(UsersPasswordEntity entity);
    void deleteByUser(String userKey);

//    void deleteByUser_UserKey(String userKey);
}

