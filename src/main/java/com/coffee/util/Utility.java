package com.coffee.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;
import org.hashids.Hashids;

@UtilityClass
public class Utility {

    public String generateMd5Hash(String s) {
        return DigestUtils.md5Hex(s).toLowerCase();
    }

    public String createPaymentReferenceNumber(String mobileNumber) {
        Hashids hashids = new Hashids(mobileNumber);
        return "pay_" + hashids.encode(System.currentTimeMillis());
    }
}
