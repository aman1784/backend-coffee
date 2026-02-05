package com.coffee.service;

import com.coffee.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String userKey);

    Optional<RefreshToken> findByToken(String requestRefreshToken);

    RefreshToken verifyExpiration(RefreshToken token);

    void deleteByToken(String requestRefreshToken);
}
