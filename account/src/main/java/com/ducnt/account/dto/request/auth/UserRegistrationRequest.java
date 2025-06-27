package com.ducnt.account.dto.request.auth;

import com.ducnt.account.validation.AccountConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@AccountConstraint
public class UserRegistrationRequest {
    String email;
    String password;
    String fullName;
    LocalDate birthDate;
    String address;
}
