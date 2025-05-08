package com.ducnt.account.service;

import com.ducnt.account.dto.request.UserRegistrationRequest;
import com.ducnt.account.exception.DomainException;
import com.ducnt.account.exception.ErrorResponse;
import com.ducnt.account.model.Account;
import com.ducnt.account.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService implements IAccountService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public void activateUser(UserRegistrationRequest request) {
        Optional<Account> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            throw new DomainException(ErrorResponse.ACCOUNT_INVALID);
        }
        Account account = Account.fromUserRequest(request);
        account.onUpdatePassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(account);
    }
}
