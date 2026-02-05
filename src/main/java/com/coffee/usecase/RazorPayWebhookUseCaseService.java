package com.coffee.usecase;

import org.json.JSONObject;

public interface RazorPayWebhookUseCaseService {
    
    void paymentLinkPaid(JSONObject webhookData);

    void paymentLinkPartiallyPaid(JSONObject webhookData);

    void paymentLinkExpired(JSONObject webhookData);

    void paymentLinkCancelled(JSONObject webhookData);

    void paymentCaptured(JSONObject webhookData);

    void paymentFailed(JSONObject webhookData);
}
