package com.coffee.service;

import com.coffee.dto.ConstantsDto;

public interface ConstantsService {

    ConstantsDto findByKey(String key);
}
