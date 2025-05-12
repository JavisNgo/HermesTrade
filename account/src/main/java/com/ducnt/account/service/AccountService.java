package com.ducnt.account.service;

import com.ducnt.account.dto.request.LoginRequest;
import com.ducnt.account.dto.request.UserRegistrationRequest;
import com.ducnt.account.dto.response.UserCreationResponse;
import com.ducnt.account.dto.response.ValidationAccountResponse;
import com.ducnt.account.exception.DomainException;
import com.ducnt.account.exception.DomainEnumException;
import com.ducnt.account.model.Account;
import com.ducnt.account.model.AccountBalance;
import com.ducnt.account.repository.AccountBalanceRepository;
import com.ducnt.account.repository.AccountRepository;
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

    AccountRepository accountRepository;
    AccountBalanceRepository accountBalanceRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public UserCreationResponse activateUser(UserRegistrationRequest request) {
        Optional<Account> user = accountRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            throw new DomainException(DomainEnumException.EMAIL_ALREADY_EXISTS);
        }
        Account account = Account.fromUserRequest(request);
        account.onUpdatePassword(passwordEncoder.encode(request.getPassword()));

        AccountBalance accountBalance = AccountBalance.onCreateAccountBalanceDefault(account.getClientId());

        accountBalanceRepository.save(accountBalance);
        accountRepository.save(account);

        return UserCreationResponse.fromAccountAndAccountBalance(account, accountBalance);
    }

    @Override
    public ValidationAccountResponse validateAccount(LoginRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new DomainException(DomainEnumException.EMAIL_INVALID));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new DomainException(DomainEnumException.PASSWORD_INCORRECT);
        }

        return ValidationAccountResponse.formAccount(account);
    }
}
