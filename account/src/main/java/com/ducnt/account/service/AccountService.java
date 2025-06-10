package com.ducnt.account.service;

import com.ducnt.account.dto.request.LoginRequest;
import com.ducnt.account.dto.request.UserRegistrationRequest;
import com.ducnt.account.dto.response.AccountProfileResponse;
import com.ducnt.account.dto.response.UserCreationResponse;
import com.ducnt.account.dto.response.ValidationAccountResponse;
import com.ducnt.account.exception.DomainException;
import com.ducnt.account.exception.DomainCode;
import com.ducnt.account.model.Account;
import com.ducnt.account.model.AccountBalance;
import com.ducnt.account.repository.AccountBalanceRepository;
import com.ducnt.account.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

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
            throw new DomainException(DomainCode.EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }
        Account account = Account.fromUserRequest(request);
        account.onUpdatePassword(passwordEncoder.encode(request.getPassword()));

        AccountBalance accountBalance = AccountBalance.onCreateAccountBalanceDefault(account.getClientId());

        accountBalanceRepository.save(accountBalance);
        accountRepository.save(account);

        return UserCreationResponse.onCreationSuccess(account, accountBalance);
    }

    @Override
    public ValidationAccountResponse validateAccount(LoginRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new DomainException(DomainCode.EMAIL_INVALID, HttpStatus.BAD_REQUEST));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new DomainException(DomainCode.PASSWORD_INCORRECT, HttpStatus.BAD_REQUEST);
        }

        return ValidationAccountResponse.formAccount(account);
    }

    @Override
    public AccountProfileResponse getAccountProfile(String clientId) {
        Account account = accountRepository.findByClientId(UUID.fromString(clientId))
                .orElseThrow(() -> new DomainException(DomainCode.ACCOUNT_NOT_FOUND));

        AccountBalance accountBalance = accountBalanceRepository.findByClientId(UUID.fromString(clientId))
                .orElseThrow(() -> new DomainException(DomainCode.ACCOUNT_NOT_FOUND));

        return AccountProfileResponse.onCreationSuccess(account, accountBalance);
    }


}
