package com.ducnt.account.validation;

import com.ducnt.account.dto.request.UserRegistrationRequest;
import com.ducnt.account.exception.DomainException;
import com.ducnt.account.exception.ErrorResponse;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

public class AccountValidator implements ConstraintValidator<AccountConstraint, UserRegistrationRequest> {

    private static final int minOfAge = 18;
    private static final int maxLengthPassword = 8;
    private static final String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]$";

    @Override
    public void initialize(AccountConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRegistrationRequest userRegistrationRequest, ConstraintValidatorContext context) {
        if(Strings.isEmpty(userRegistrationRequest.getAddress()))
            throw new DomainException(ErrorResponse.ADDRESS_IS_REQUIRED);
        if(Strings.isEmpty(userRegistrationRequest.getFullName()))
            throw new DomainException(ErrorResponse.FULL_NAME_IS_REQUIRED);
        if(!validateDob(userRegistrationRequest.getBirthDate())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Address is required").addConstraintViolation();
            return false;
        }
        if(validatePasswordLength(userRegistrationRequest.getPassword()))
            throw new DomainException(ErrorResponse.PASSWORD_LENGTH_INVALID);
        if(validatePasswordPattern(userRegistrationRequest.getPassword()))
            throw new DomainException(ErrorResponse.WEAK_PASSWORD);
        return true;
    }

    private boolean validateDob(LocalDate dob) {
        long year = ChronoUnit.YEARS.between(dob, LocalDate.now());
        return year <= minOfAge;
    }


    private boolean validatePasswordLength(String password) {
        return password.length() < maxLengthPassword;
    }

    private boolean validatePasswordPattern(String password) {
        return Pattern.matches(pattern, password);
    }

}
