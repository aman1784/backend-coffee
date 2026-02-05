package com.coffee.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResetPasswordResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // private String passwordResetMessage;
    transient String emailId;
    transient String phoneNumber;
    // private String passwordResetLink;
    // private String passwordResetToken;
    // private String passwordResetTokenExpiry;
    private String password;
}
