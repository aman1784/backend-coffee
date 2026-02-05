package com.coffee.service;

import com.coffee.dto.PersistCartDto;

import java.util.List;

public interface PersistCartService {

    List<PersistCartDto> findByUserId(String userId);

    List<PersistCartDto> saveAll(List<PersistCartDto> existingPersistCartDtoList);

    PersistCartDto save(PersistCartDto dto);
}
