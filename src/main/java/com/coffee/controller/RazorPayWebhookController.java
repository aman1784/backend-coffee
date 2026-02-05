package com.coffee.controller;

import com.coffee.usecase.RazorPayWebhookUseCaseService;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1.0/razorpay/webhook")
@RequiredArgsConstructor
@Slf4j
public class RazorPayWebhookController {

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    private final RazorPayWebhookUseCaseService service;

    @PostMapping(value = "/paymentLink")
    public void razorPayWebhookForPaymentLink(@RequestBody String payload, @RequestHeader("X-Razorpay-Signature") String signature) {
        log.debug("[RazorPayWebhookController][razorPayWebhookForPaymentLink] request: {}", payload);
        log.error("[RazorPayWebhookController][razorPayWebhookForPaymentLink] request: {}", payload);

        try {
            // webhook signature verification
            boolean isValidSignature = Utils.verifyWebhookSignature(payload, signature, webhookSecret);
            log.debug("[RazorPayWebhookController][razorPayWebhookForPaymentLink] isValidSignature: {}", isValidSignature);
            log.error("[RazorPayWebhookController][razorPayWebhookForPaymentLink] isValidSignature: {}", isValidSignature);

            if (isValidSignature) {

                JSONObject webhookData = new JSONObject(payload);
                log.debug("[RazorPayWebhookController][razorPayWebhookForPaymentLink] webhookData: {}", webhookData);
                log.error("[RazorPayWebhookController][razorPayWebhookForPaymentLink] webhookData: {}", webhookData);

                String event = webhookData.getString("event");// e.g "payment_link.captured"
                log.debug("[RazorPayWebhookController][razorPayWebhookForPaymentLink] event: {}", event);
                log.error("[RazorPayWebhookController][razorPayWebhookForPaymentLink] event: {}", event);

                switch (event) {
//                    case "payment.authorized":
//                        service.paymentAuthorized(webhookData);
//                        break;
                    case "payment_link.paid":
                        // [RazorPayWebhookController][razorPayWebhookForPaymentLink] webhookData:
                        // {"contains":["payment_link","order","payment"],"account_id":"acc_QcdOTYhzKAxWdn","payload":{"payment":{"entity":{"notes":{"amount":"236","reference_id":"pay_Mn7DWJ96q","user_id":"user_6588387389","order_id":"order_id174924"},"fee":614,"created_at":1760424579,"description":"#RTGAZv2Ufm1AzX","amount_refunded":0,"bank":"BARB_R","error_reason":null,"acquirer_data":{"bank_transaction_id":"9480153"},"error_description":null,"captured":true,"contact":"+916386100205","base_amount":23600,"invoice_id":null,"currency":"INR","id":"pay_RTGB8l6O9eTmT3","international":false,"email":"void@razorpay.com","amount":23600,"amount_transferred":0,"refund_status":null,"wallet":null,"method":"netbanking","error_source":null,"error_step":null,"vpa":null,"tax":94,"card_id":null,"error_code":null,"fee_bearer":"platform","order_id":"order_RTGAedMQeW6ZeT","card":null,"entity":"payment","status":"captured"}},"payment_link":{"entity":{"cancelled_at":0,"reminders":{"status":"failed"},"amount":23600,"amount_paid":23600,"notes":{"amount":"236","reference_id":"pay_Mn7DWJ96q","user_id":"user_6588387389","order_id":"order_id174924"},"reference_id":"pay_Mn7DWJ96q","created_at":1760424546,"description":"","expired_at":0,"reminder_enable":true,"notify":{"whatsapp":false,"sms":true,"email":false},"short_url":"https://rzp.io/rzp/J7BH4dH","updated_at":1760424587,"user_id":"","upi_link":false,"accept_partial":false,"currency":"INR","id":"plink_RTGAZv2Ufm1AzX","order_id":"order_RTGAedMQeW6ZeT","expire_by":0,"customer":{"contact":"+916586108347","name":"Abhishek Gupta"},"first_min_partial_amount":0,"status":"paid","whatsapp_link":false}},"order":{"entity":{"amount":23600,"amount_paid":23600,"notes":{"amount":"236","reference_id":"pay_Mn7DWJ96q","user_id":"user_6588387389","order_id":"order_id174924"},"created_at":1760424550,"description":null,"offer_id":null,"amount_due":0,"currency":"INR","receipt":"pay_Mn7DWJ96q","id":"order_RTGAedMQeW6ZeT","checkout":null,"entity":"order","attempts":1,"status":"paid"}}},"created_at":1760424546,"event":"payment_link.paid","entity":"event"}
                        service.paymentLinkPaid(webhookData);
                        break;
                    case "payment_link.partially_paid":
                        service.paymentLinkPartiallyPaid(webhookData);
                        break;
                    case "payment_link.expired":
                        service.paymentLinkExpired(webhookData);
                        break;
                    case "payment_link.cancelled":
                        service.paymentLinkCancelled(webhookData);
                        break;
                    case "payment.captured":
                        // [RazorPayWebhookController][razorPayWebhookForPaymentLink] webhookData:
                        // {"contains":["payment"],"account_id":"acc_QcdOTYhzKAxWdn","payload":{"payment":{"entity":{"notes":{"amount":"295","reference_id":"pay_EpbJdEWMz","user_id":"user_6588387389","order_id":"order_id738884"},"fee":765,"description":"#RTFsjqi5cJlT1j","created_at":1760423911,"amount_refunded":0,"bank":"BARB_R","error_reason":null,"error_description":null,"acquirer_data":{"bank_transaction_id":"8888098"},"captured":true,"contact":"+916386100205","invoice_id":null,"base_amount":29500,"currency":"INR","id":"pay_RTFzOa6J3Ns2LD","international":false,"email":"void@razorpay.com","reward":null,"amount":29500,"refund_status":null,"wallet":null,"method":"netbanking","vpa":null,"error_source":null,"error_step":null,"tax":116,"card_id":null,"error_code":null,"order_id":"order_RTFt82w0wFsm2f","entity":"payment","status":"captured"}}},"created_at":1760423965,"event":"payment.captured","entity":"event"}
                        service.paymentCaptured(webhookData);
                        break;
                    case "payment.failed":
                        // [RazorPayWebhookController][razorPayWebhookForPaymentLink] webhookData:
                        // {"contains":["payment"],"account_id":"acc_QcdOTYhzKAxWdn", "payload":{"payment":{"entity":{"notes":{"amount":"295","reference_id":"pay_EpbJdEWMz","user_id":"user_6588387389","order_id":"order_id738884"},"fee":null,"description":"#RTFsjqi5cJlT1j","created_at":1760423573,"amount_refunded":0,"bank":"UTBI","error_reason":"payment_cancelled","error_description":"Your payment has been cancelled. Try again or complete the payment later.","acquirer_data":{"bank_transaction_id":null},"captured":false,"contact":"+916386100205","invoice_id":null,"currency":"INR","id":"pay_RTFtQxRt7ZsLCk","international":false,"email":"void@razorpay.com","amount":29500,"refund_status":null,"wallet":null,"method":"netbanking","vpa":null,"error_source":"customer","error_step":"payment_authentication","tax":null,"card_id":null,"error_code":"BAD_REQUEST_ERROR","order_id":"order_RTFt82w0wFsm2f","entity":"payment","status":"failed"}}},"created_at":1760423629,"event":"payment.failed","entity":"event"}
                        service.paymentFailed(webhookData);
                        break;
                    default:
                        log.debug("[RazorPayWebhookController][razorPayWebhookForPaymentLink] Unhandled event: {}", event);
                        log.error("[RazorPayWebhookController][razorPayWebhookForPaymentLink] Unhandled event: {}", event);
                        break;
                }
            } else {
                log.debug("[RazorPayWebhookController][razorPayWebhookForPaymentLink] Invalid webhook signature");
                log.error("[RazorPayWebhookController][razorPayWebhookForPaymentLink] Invalid webhook signature");
            }
        } catch (Exception e) {
            log.debug("[RazorPayWebhookController][razorPayWebhookForPaymentLink] Error processing webhook", e);
            log.error("[RazorPayWebhookController][razorPayWebhookForPaymentLink] Error processing webhook", e);
        }
    }

}
