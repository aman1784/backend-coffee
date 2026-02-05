package com.coffee.usecase.impl;

import com.coffee.dto.CreateOrderDto;
import com.coffee.dto.PaymentLinkDto;
import com.coffee.exception.GenericException;
import com.coffee.responseMapper.RazorPayWebhookMapper;
import com.coffee.service.CreateOrderService;
import com.coffee.service.PaymentLinkService;
import com.coffee.usecase.RazorPayWebhookUseCaseService;
import com.coffee.util.ExceptionConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class RazorPayWebhookUseCaseServiceImpl implements RazorPayWebhookUseCaseService {

    private final RazorPayWebhookMapper razorPayWebhookMapper;

    private final PaymentLinkService paymentLinkService;
    private final CreateOrderService createOrderService;
    private final CacheManager cacheManager;

    @Override
    // @CacheEvict(value = "getOrder", key = "#notes.order_id")
    public void paymentLinkPaid(JSONObject webhookData) {
        log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] : {}", webhookData);
        log.error("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] : {}", webhookData);
        // [RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] :
        // {"contains":["payment_link","order","payment"],"account_id":"acc_QcdOTYhzKAxWdn","payload":{"payment":{"entity":{"notes":{"amount":"236","reference_id":"pay_Mn7DWJ96q","user_id":"user_6588387389","order_id":"order_id174924"},"fee":614,"created_at":1760424579,"description":"#RTGAZv2Ufm1AzX","amount_refunded":0,"bank":"BARB_R","error_reason":null,"acquirer_data":{"bank_transaction_id":"9480153"},"error_description":null,"captured":true,"contact":"+916386100205","base_amount":23600,"invoice_id":null,"currency":"INR","id":"pay_RTGB8l6O9eTmT3","international":false,"email":"void@razorpay.com","amount":23600,"amount_transferred":0,"refund_status":null,"wallet":null,"method":"netbanking","error_source":null,"error_step":null,"vpa":null,"tax":94,"card_id":null,"error_code":null,"fee_bearer":"platform","order_id":"order_RTGAedMQeW6ZeT","card":null,"entity":"payment","status":"captured"}},"payment_link":{"entity":{"cancelled_at":0,"reminders":{"status":"failed"},"amount":23600,"amount_paid":23600,"notes":{"amount":"236","reference_id":"pay_Mn7DWJ96q","user_id":"user_6588387389","order_id":"order_id174924"},"reference_id":"pay_Mn7DWJ96q","created_at":1760424546,"description":"","expired_at":0,"reminder_enable":true,"notify":{"whatsapp":false,"sms":true,"email":false},"short_url":"https://rzp.io/rzp/J7BH4dH","updated_at":1760424587,"user_id":"","upi_link":false,"accept_partial":false,"currency":"INR","id":"plink_RTGAZv2Ufm1AzX","order_id":"order_RTGAedMQeW6ZeT","expire_by":0,"customer":{"contact":"+916586108347","name":"Abhishek Gupta"},"first_min_partial_amount":0,"status":"paid","whatsapp_link":false}},"order":{"entity":{"amount":23600,"amount_paid":23600,"notes":{"amount":"236","reference_id":"pay_Mn7DWJ96q","user_id":"user_6588387389","order_id":"order_id174924"},"created_at":1760424550,"description":null,"offer_id":null,"amount_due":0,"currency":"INR","receipt":"pay_Mn7DWJ96q","id":"order_RTGAedMQeW6ZeT","checkout":null,"entity":"order","attempts":1,"status":"paid"}}},"created_at":1760424546,"event":"payment_link.paid","entity":"event"}

        try {
            String orderId = webhookData.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity").getJSONObject("notes").getString("order_id");
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] ✅ orderId: {}", orderId);
            Cache cache = cacheManager.getCache("getOrder");
            if (cache != null) {
                cache.evict(orderId);
                log.info("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] ✅ Cache evicted for orderId: {}", orderId);
            } else {
                log.warn("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] ⚠️ Cache 'getOrder' not found");
            }
            // Safely extract "event" from root
            String event = webhookData != null ? webhookData.optString("event", null) : null;

            // Navigate safely through payload -> payment -> entity
            JSONObject payload = webhookData != null ? webhookData.optJSONObject("payload") : null;
            JSONObject payment = payload != null ? payload.optJSONObject("payment") : null;
            JSONObject entity = payment != null ? payment.optJSONObject("entity") : null;

            // Extract values with null-safe checks
            String errorReason = entity != null ? entity.optString("error_reason", null) : null;
            String errorDescription = entity != null ? entity.optString("error_description", null) : null;
            String errorSource = entity != null ? entity.optString("error_source", null) : null;
            String errorStep = entity != null ? entity.optString("error_step", null) : null;
            String status = entity != null ? entity.optString("status", null) : null; // "paid"

            // Navigate safely into entity.notes
            JSONObject notes = entity != null ? entity.optJSONObject("notes") : null;
            String referenceId = notes != null ? notes.optString("reference_id", null) : null;


            // Extract "amount" and "method" in a null-safe way
            BigDecimal amountInRupees = BigDecimal.ZERO;
            if (entity != null && entity.has("amount")) {
                // Get the amount in paise and convert to rupees
                int amountPaise = entity.optInt("amount", 0);
                amountInRupees = new BigDecimal(amountPaise).divide(new BigDecimal("100"));
            }

            String method = entity != null ? entity.optString("method", null) : null; // "netbanking"


            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid]Event: {}", event);
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid]Error Reason: {}", errorReason);
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid]Error Description: {}", errorDescription);
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid]Error Source: {}", errorSource);
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid]Error Step: {}", errorStep);
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid]Status: {}", status);
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid]Reference ID: {}", referenceId);
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid]Amount (INR): {}", amountInRupees);
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid]Method: {}", method);

            PaymentLinkDto paymentLinkDto = paymentLinkService.findByReferenceId(referenceId);
            if (paymentLinkDto == null) {
                log.error("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] PaymentLink not found for referenceId: {}", referenceId);
                log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] PaymentLink not found for referenceId: {}", referenceId);
                throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "PaymentLink not found for referenceId: " + referenceId);
            }
            paymentLinkDto = razorPayWebhookMapper.buildPaymentLinkDtoForPaymentLinkPaid(paymentLinkDto, event, status, amountInRupees);
            log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] PaymentLinkDto: {}", paymentLinkDto);
            paymentLinkDto = paymentLinkService.update(paymentLinkDto);
            log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] Updated PaymentLinkDto: {}", paymentLinkDto);

            CreateOrderDto createOrderDto = createOrderService.findByOrderIdAndOrderKey(paymentLinkDto.getOrderId(), paymentLinkDto.getOrderKey());
            log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] CreateOrderDto: {}", createOrderDto);
            if (createOrderDto == null) {
                log.error("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] CreateOrder not found for orderId: {}", paymentLinkDto.getOrderId());
                log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] CreateOrder not found for orderId: {}", paymentLinkDto.getOrderId());
                throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "CreateOrder not found for orderId: " + paymentLinkDto.getOrderId());
            }
            createOrderDto = razorPayWebhookMapper.buildUpdateCreateOrderDtoForPaymentLinkPaid(createOrderDto, paymentLinkDto);
            log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] CreateOrderDto: {}", createOrderDto);
            createOrderDto = createOrderService.update(createOrderDto);
            log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] Updated CreateOrderDto: {}", createOrderDto);

        } catch (Exception e) {
            log.error("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] : {}", e.getMessage());
            log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPaid] : {}", e.getMessage());
            e.getStackTrace();
        }
    }

    @Override
    public void paymentLinkPartiallyPaid(JSONObject webhookData) {
        log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPartiallyPaid] : {}", webhookData);
        log.error("[RazorPayWebhookUseCaseServiceImpl][paymentLinkPartiallyPaid] : {}", webhookData);
        
    }

    @Override
    public void paymentLinkExpired(JSONObject webhookData) {
        log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentLinkExpired] : {}", webhookData);
        log.error("[RazorPayWebhookUseCaseServiceImpl][paymentLinkExpired] : {}", webhookData);
        
    }

    @Override
    public void paymentLinkCancelled(JSONObject webhookData) {
        log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentLinkCancelled] : {}", webhookData);
        log.error("[RazorPayWebhookUseCaseServiceImpl][paymentLinkCancelled] : {}", webhookData);
        
    }

    @Override
    public void paymentCaptured(JSONObject webhookData) {
        log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentCaptured] : {}", webhookData);
        log.error("[RazorPayWebhookUseCaseServiceImpl][paymentCaptured] : {}", webhookData);
        // [RazorPayWebhookUseCaseServiceImpl][paymentCaptured] :
        // {"contains":["payment"],"account_id":"acc_QcdOTYhzKAxWdn","payload":{"payment":{"entity":{"notes":{"amount":"295","reference_id":"pay_EpbJdEWMz","user_id":"user_6588387389","order_id":"order_id738884"},"fee":765,"description":"#RTFsjqi5cJlT1j","created_at":1760423911,"amount_refunded":0,"bank":"BARB_R","error_reason":null,"error_description":null,"acquirer_data":{"bank_transaction_id":"8888098"},"captured":true,"contact":"+916386100205","invoice_id":null,"base_amount":29500,"currency":"INR","id":"pay_RTFzOa6J3Ns2LD","international":false,"email":"void@razorpay.com","reward":null,"amount":29500,"refund_status":null,"wallet":null,"method":"netbanking","vpa":null,"error_source":null,"error_step":null,"tax":116,"card_id":null,"error_code":null,"order_id":"order_RTFt82w0wFsm2f","entity":"payment","status":"captured"}}},"created_at":1760423965,"event":"payment.captured","entity":"event"}

    }

    @Override
    public void paymentFailed(JSONObject webhookData) {
        log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentFailed] : {}", webhookData);
        log.error("[RazorPayWebhookUseCaseServiceImpl][paymentFailed] : {}", webhookData);
        // [RazorPayWebhookUseCaseServiceImpl][paymentFailed] :
        // {"contains":["payment"],"account_id":"acc_QcdOTYhzKAxWdn","payload":{"payment":{"entity":{"notes":{"amount":"295","reference_id":"pay_EpbJdEWMz","user_id":"user_6588387389","order_id":"order_id738884"},"fee":null,"description":"#RTFsjqi5cJlT1j","created_at":1760423573,"amount_refunded":0,"bank":"UTBI","error_reason":"payment_cancelled","error_description":"Your payment has been cancelled. Try again or complete the payment later.","acquirer_data":{"bank_transaction_id":null},"captured":false,"contact":"+916386100205","invoice_id":null,"currency":"INR","id":"pay_RTFtQxRt7ZsLCk","international":false,"email":"void@razorpay.com","amount":29500,"refund_status":null,"wallet":null,"method":"netbanking","vpa":null,"error_source":"customer","error_step":"payment_authentication","tax":null,"card_id":null,"error_code":"BAD_REQUEST_ERROR","order_id":"order_RTFt82w0wFsm2f","entity":"payment","status":"failed"}}},"created_at":1760423629,"event":"payment.failed","entity":"event"}

        try {
            // Safely extract "event" from root
            String event = webhookData != null ? webhookData.optString("event", null) : null;

            // Navigate safely through payload -> payment -> entity
            JSONObject payload = webhookData != null ? webhookData.optJSONObject("payload") : null;
            JSONObject payment = payload != null ? payload.optJSONObject("payment") : null;
            JSONObject entity = payment != null ? payment.optJSONObject("entity") : null;

            // Extract values with null-safe checks
            String errorReason = entity != null ? entity.optString("error_reason", null) : null;
            String errorDescription = entity != null ? entity.optString("error_description", null) : null;
            String errorSource = entity != null ? entity.optString("error_source", null) : null;
            String errorStep = entity != null ? entity.optString("error_step", null) : null;
            String status = entity != null ? entity.optString("status", null) : null; // "failed"

            // Navigate safely into entity.notes
            JSONObject notes = entity != null ? entity.optJSONObject("notes") : null;
            String referenceId = notes != null ? notes.optString("reference_id", null) : null;

            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentFailed]Event: {}", event);
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentFailed]Error Reason: {}", errorReason);
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentFailed]Error Description: {}", errorDescription);
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentFailed]Error Source: {}", errorSource);
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentFailed]Error Step: {}", errorStep);
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentFailed]Status: {}", status);
            log.info("[RazorPayWebhookUseCaseServiceImpl][paymentFailed]Reference ID: {}", referenceId);

            PaymentLinkDto paymentLinkDto = paymentLinkService.findByReferenceId(referenceId);
            if (paymentLinkDto == null) {
                log.error("[RazorPayWebhookUseCaseServiceImpl][paymentFailed] PaymentLink not found for referenceId: {}", referenceId);
                log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentFailed] PaymentLink not found for referenceId: {}", referenceId);
                throw new GenericException(ExceptionConstant.entityNotFoundCode, "Not Found", ExceptionConstant.notFound, "PaymentLink not found for referenceId: " + referenceId);
            }
            paymentLinkDto = razorPayWebhookMapper.buildPaymentLinkDto(paymentLinkDto, event, errorReason, errorDescription, errorSource, errorStep, status);
            log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentFailed] PaymentLinkDto: {}", paymentLinkDto);
            paymentLinkDto = paymentLinkService.update(paymentLinkDto);
            log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentFailed] Updated PaymentLinkDto: {}", paymentLinkDto);

        } catch (Exception e) {
            log.error("[RazorPayWebhookUseCaseServiceImpl][paymentFailed] Exception while parsing webhook data", e);
            log.debug("[RazorPayWebhookUseCaseServiceImpl][paymentFailed] Exception while parsing webhook data", e);
            e.getStackTrace();
        }
        
    }
}
