package com.coffee.util;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@UtilityClass

public class UsersUtility {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String generateUserId(String phone) {
        String part1 = phone.substring(0, 3);
        String part2 = phone.substring(6, 8);
        long timestamp = System.currentTimeMillis() % 10000; // Shortened timestamp
        int randomNum = (int) (Math.random() * 100); // Random number between 0 and 99

        return String.format("user_%s%s%02d%02d", part1, part2, randomNum, timestamp);
    }

    public static String generateFullName(String firstName, String middleName, String lastName) {
        StringBuilder fullName = new StringBuilder();
        if (firstName != null && !firstName.isEmpty()) {
            fullName.append(firstName);
        }
        if (middleName != null && !middleName.isEmpty()) {
            fullName.append(" ");
            fullName.append(middleName);
        }
        else if (middleName != null) { // this is because middle name is optional field and can be empty as well
            fullName.append(middleName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            fullName.append(" ");
            fullName.append(lastName);
        }
        return fullName.toString();
    }

    public static String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean validatePassword(String userPassword, String savedPassword) {
        return passwordEncoder.matches(userPassword, savedPassword);
    }
}
