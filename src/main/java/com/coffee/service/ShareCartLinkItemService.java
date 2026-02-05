package com.coffee.service;

import com.coffee.dto.ShareCartLinkItemDto;

import java.util.List;

/**
 * @author Aman Kumar Seth
 * @since 27-01-2026
 */

public interface ShareCartLinkItemService {

    List<ShareCartLinkItemDto> saveAll(List<ShareCartLinkItemDto> shareCartLinkItemDtoList);

    List<ShareCartLinkItemDto> findAllByShareCartLinkId(Long userId);
}
