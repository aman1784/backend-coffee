package com.coffee.responseMapper;

import com.coffee.dto.PaymentLinkDto;
import com.coffee.projection.GetPaymentTypeMethodProjection;
import com.coffee.request.CreatePaymentLinkRequest;
import com.coffee.response.CreatePaymentLinkResponse;
import com.coffee.response.GetPaymentMethodsResponse;
import com.coffee.util.Utility;
import com.razorpay.PaymentLink;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Component
public class PaymentResponseMapper {

    public List<GetPaymentMethodsResponse> toGetPaymentMethodsResponse(List<GetPaymentTypeMethodProjection> paymentTypeList) {
        return paymentTypeList.stream()
                .map(paymentTypeDto -> GetPaymentMethodsResponse
                        .builder()
                        .paymentMethod(paymentTypeDto.getPaymentType())
                        .build())
                .toList();
    }

    public JSONObject createPaymentLinkRequest(BigDecimal grandTotal, String orderId,
                                               String userId, String customerName, String customerPhoneNumber, String orderKey) {

        if (customerName.length() < 3) {
            int length = 3 - customerName.length(); // Calculate missing spaces
            customerName = customerName + " ".repeat(length); // Pad with spaces
        } else if (customerName.length() > 50) {
            customerName = customerName.substring(0, 50); // Trim to 50 characters
        } else {
            customerName = customerName;
        }

        String referenceNumber = Utility.createPaymentReferenceNumber(customerPhoneNumber);

        JSONObject paymentLinkRequest = new JSONObject();

        paymentLinkRequest.put("amount", grandTotal.multiply(BigDecimal.valueOf(100))); // currency always in paisa
        paymentLinkRequest.put("currency", "INR");
        paymentLinkRequest.put("accept_partial", false);
        paymentLinkRequest.put("reference_id", referenceNumber); // refrence id must be uniquely created combination of current time stamp and customer mobile.

        JSONObject customer = new JSONObject();
        customer.put("name", customerName); // name should be between 3 and 50 characters
        customer.put("contact", "+91" + customerPhoneNumber);
        customer.put("email", "");
        //customer.put("whatsapp_link", "true");
        paymentLinkRequest.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("sms", true);
        notify.put("email", false);
        //notify.put("whatsapp", true);
        paymentLinkRequest.put("notify", notify);
        paymentLinkRequest.put("reminder_enable", true);

        JSONObject notes = new JSONObject();
        notes.put("reference_id", referenceNumber);// this must be present so that we can check in db using this link.
        notes.put("order_id", orderId);
        notes.put("user_id", userId);
        notes.put("amount", grandTotal);
        notes.put("order_key", orderKey);
        paymentLinkRequest.put("notes", notes);
        return paymentLinkRequest;
    }

    public CreatePaymentLinkResponse createPaymentLinkResponse(PaymentLink payment) {

        return CreatePaymentLinkResponse.builder()
                .link(payment.get("short_url"))
                .build();
    }

    public PaymentLinkDto createPaymentLinkDto(PaymentLink payment, CreatePaymentLinkRequest request) {


        return PaymentLinkDto.builder()
                .id(null)
                .paymentId(payment.get("id"))
                .userKey(request.getUserId())
                .notes(payment.get("notes").toString())
                .description("") // .description(payment.get("description"))
                .referenceId(payment.get("reference_id").toString())
                .url(payment.get("short_url").toString())
                .totalAmount(request.getGrandTotal())
                .amountPaid(BigDecimal.ZERO)
                .currency(payment.get("currency").toString())
                .customerName(request.getCustomerName())
                .customerPhone(request.getCustomerPhoneNumber())
                .customerEmail("") // .customerEmail(payment.get("customer").get("email"))
                .status(1)
                .paymentStatus(payment.get("status"))
                .orderId(request.getOrderId())
                .orderKey(request.getOrderKey())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
}
