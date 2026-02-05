package com.coffee.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CreateOrderUtility {

    public static String createOrderId() {
        String part1 = "order_id";
        long timestamp = System.currentTimeMillis() % 10000; // Shortened timestamp
        int randomNum = (int) (Math.random() * 100); // Random number between 0 and 99
        return String.format("%s%02d%02d", part1, randomNum, timestamp);
    }

}
