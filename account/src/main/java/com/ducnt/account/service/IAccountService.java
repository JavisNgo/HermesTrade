package com.ducnt.account.service;

import com.ducnt.account.dto.request.auth.LoginRequest;
import com.ducnt.account.dto.request.auth.UserRegistrationRequest;
import com.ducnt.account.dto.response.auth.AccountProfileResponse;
import com.ducnt.account.dto.response.auth.UserCreationResponse;
import com.ducnt.account.dto.response.auth.ValidationAccountResponse;

public interface IAccountService {
    UserCreationResponse activateUser(UserRegistrationRequest request);
    ValidationAccountResponse validateAccount(LoginRequest request);
    AccountProfileResponse getAccountProfile(String clientId);
    void getAdvertisements();
    void reserveAccountBalance(String clientId, String balance);
}
