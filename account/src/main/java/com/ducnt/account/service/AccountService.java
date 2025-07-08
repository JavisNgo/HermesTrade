package com.ducnt.account.service;

import com.ducnt.account.clients.AdvertisementClient;
import com.ducnt.account.dto.request.auth.LoginRequest;
import com.ducnt.account.dto.request.auth.UserRegistrationRequest;
import com.ducnt.account.dto.request.binance.AdvertisementApiRequest;
import com.ducnt.account.dto.response.auth.AccountProfileResponse;
import com.ducnt.account.dto.response.auth.ErrorResponse;
import com.ducnt.account.dto.response.auth.UserCreationResponse;
import com.ducnt.account.dto.response.auth.ValidationAccountResponse;
import com.ducnt.account.dto.response.binance.AdvertisementApiResponse;
import com.ducnt.account.dto.response.binance.FilteredAdvertisementData;
import com.ducnt.account.exception.DomainException;
import com.ducnt.account.exception.DomainCode;
import com.ducnt.account.model.Account;
import com.ducnt.account.model.AccountBalance;
import com.ducnt.account.repository.AccountBalanceRepository;
import com.ducnt.account.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AccountService implements IAccountService {

    AccountRepository accountRepository;
    AccountBalanceRepository accountBalanceRepository;
    PasswordEncoder passwordEncoder;
    AdvertisementClient advertisementClient;
    ObjectMapper objectMapper;

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

    @Override
    public void getAdvertisements() {
        ResponseEntity<AdvertisementApiResponse> advertisement = null;
        try {
            advertisement = advertisementClient.getAdvertisement(new AdvertisementApiRequest());

        } catch (FeignException.FeignClientException e) {
            try {
                throw new DomainException(DomainCode.SERVICE_UNAVAILABLE, HttpStatus.BAD_REQUEST);
            } catch (Exception ignored) {
                throw new DomainException(DomainCode.UNEXPECTED_ERROR_CODE, HttpStatus.BAD_REQUEST);
            }
        }

        if(advertisement == null) throw new DomainException(DomainCode.SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
        AdvertisementApiResponse body = advertisement.getBody();
        if(body == null) throw new DomainException(DomainCode.SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
        List<Account> accounts = new ArrayList<>();
        List<AccountBalance> accountBalances = new ArrayList<>();
        for (FilteredAdvertisementData data : body.getData()) {
            Account account = Account.fromFilteredAdvertiser(data.getAdvertiser());
            AccountBalance accountBalance = AccountBalance.onCreation(account.getClientId(), data.getAdv());
            accountBalances.add(accountBalance);
            accounts.add(account);
        }
        accountRepository.saveAll(accounts);
        accountBalanceRepository.saveAll(accountBalances);
    }

    @Override
    public void reserveAccountBalance(String clientId, String balance) {
        AccountBalance accountBalance = accountBalanceRepository.findByClientId(UUID.fromString(clientId))
                .orElseThrow(() -> new DomainException(DomainCode.ACCOUNT_NOT_FOUND));
        BigDecimal updatedBalance = new BigDecimal(balance);
        accountBalance.setAvailableBalance(updatedBalance);
        accountBalanceRepository.save(accountBalance);
    }

}
