package com.coffee.usecase;

import com.coffee.request.ReorderRequest;
import com.coffee.response.BaseResponse;

/**
 * @author Aman Kumar Seth
 * @version 1.0
 * @since 2025-11-28
 */

public interface ReOrderUseCaseService {

    BaseResponse<String> reorder(ReorderRequest request);
}
