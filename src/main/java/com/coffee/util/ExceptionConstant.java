package com.coffee.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionConstant {

    public final int requestFailedCode = 908;
    public final String requestFailedMessage = "Request Failed";
    public final int badRequestCode = 906;
    public final String badRequestMessage = "Mandatory params not found";
    public final int creditLimitExceed=75104;
    public final String creditLimitHeadingMessage="Credit Limit Exceeded";
    public final String creditLimitMessage="You have exceeded your credit limit.Order could not be created.";
    public final int otherError=400;
    public final String otherErrorHeadingMessage="Error 400";
    public final String otherErrorMessage="Something went wrong.Order could not be created.";
    public final String paymentFailedHeadingMessage="Payment Failed";
    public final String paymentFailedMessage="Your payment could not be processed.Order could not be created.";
    public final int entityNotFoundCode = 111;
    public final String notFound = "Not Found";
    public final int invalidCredentialCode = 112;
    public final String invalidCredentialMessage = "Invalid Credentials";
    public final int unAuthorizedCode = 401;
    public final String unAuthorizedMessage = "Unauthorized Access";
    public final int authExceptionCode = 800;
    public final int serviceNotFoundCode = 901;
    public final int OtpExceptionCode = 902;
    public final int inActiveVehicleCode = 903;
    public final int inActiveVehicleVariantCode = 904;
    public final int spareNotFoundCode = 905;
    public final String spareNotFoundMessage = "Spares Not Available";
    public final int emptyResponseCode = 907;
    public final String emptyResponseMessage = "No Record Found";
    public final int noPaymentMethodCode = 1001;
    public final int invoiceCode = 1002;
    public final int notBoardedCode = 1003;
    public final int newUserRequestCode = 1004;
    public final int emailAlreadyExistsCode = 1005;
    public final String emailAlreadyExistsMessage = "Email already exists.";
    public final int userAlreadyExistsCode = 1006;
    public final int walletCode = 1007;
    public final int zohoExceptionCode = 1101;
    public final String zohoExecptionMessage="Autoverse Zoho API Exception Occurred";
    public final String zohoErrorExecptionMessage=" Zoho API Exception Occurred";
    public final int fastlaneExceptionCode = 1102;
    public final int b2bExceptionCode = 1103;
    public final int autoverseExceptionCode = 1302;
    public final int onBoarded = 1003;
    public final String onBoardedMessage = "Garage Not OnBoarded";

}
