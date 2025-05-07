package com.ducnt.activation.dto.request;

import com.ducnt.activation.validation.DobConstraint;
import com.ducnt.activation.validation.PasswordConstraint;
import jakarta.validation.constraints.Email;
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
public class UserRegistrationRequest {
    @Email(message = "Email must be a well-formed email address")
    String email;
    @PasswordConstraint
    String password;
    String fullName;
    @DobConstraint(min = 18)
    LocalDate birthDate;
    String address;
}
