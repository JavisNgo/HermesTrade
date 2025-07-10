package com.ducnt.account.validation;

import com.ducnt.account.dto.request.auth.UserRegistrationRequest;
import com.ducnt.account.exception.DomainException;
import com.ducnt.account.exception.DomainCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

public class AccountValidator implements ConstraintValidator<AccountConstraint, UserRegistrationRequest> {

    private static final int minOfAge = 18;
    private static final int maxLengthPassword = 8;
    private static final String passPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
    private static final String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    public void initialize(AccountConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRegistrationRequest userRegistrationRequest, ConstraintValidatorContext context) {
        if(Strings.isEmpty(userRegistrationRequest.getAddress()))
            throw new DomainException(DomainCode.ADDRESS_IS_REQUIRED, HttpStatus.BAD_REQUEST);

        if(Strings.isEmpty(userRegistrationRequest.getFullName()))
            throw new DomainException(DomainCode.FULL_NAME_IS_REQUIRED, HttpStatus.BAD_REQUEST);

        if(validateDob(userRegistrationRequest.getBirthDate())) {
            throw new DomainException(DomainCode.AGE_MUST_BE_AT_LEAST, HttpStatus.BAD_REQUEST);
        }
        if(validatePasswordLength(userRegistrationRequest.getPassword()))
            throw new DomainException(DomainCode.PASSWORD_LENGTH_INVALID, HttpStatus.BAD_REQUEST);

        if(!validatePasswordPattern(userRegistrationRequest.getPassword()))
            throw new DomainException(DomainCode.WEAK_PASSWORD, HttpStatus.BAD_REQUEST);

        if(!validateEmail(userRegistrationRequest.getEmail()))
            throw new DomainException(DomainCode.EMAIL_INVALID, HttpStatus.BAD_REQUEST);

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
        return Pattern.matches(passPattern, password);
    }

    private boolean validateEmail(String email) {
        return Pattern.matches(emailPattern, email);
    }

}
