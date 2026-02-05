package com.coffee.service;

import com.coffee.dto.PaymentLinkDto;

public interface PaymentLinkService {

    PaymentLinkDto save(PaymentLinkDto paymentLink);

    PaymentLinkDto findByReferenceId(String referenceId);

    PaymentLinkDto update(PaymentLinkDto paymentLinkDto);
}
