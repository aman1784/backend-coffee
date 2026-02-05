package com.coffee.service;

import com.coffee.dto.ShareCartLinkDto;

/**
 * @author Aman Kumar Seth
 * @since 26-01-2026
 */

public interface ShareCartLinkService {

    ShareCartLinkDto save(ShareCartLinkDto shareCartLinkDto);

    ShareCartLinkDto findByShareCartLink(String shareLink);
}
