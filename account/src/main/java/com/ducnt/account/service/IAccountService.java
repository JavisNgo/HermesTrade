package com.ducnt.account.service;

import com.ducnt.account.dto.request.LoginRequest;
import com.ducnt.account.dto.request.UserRegistrationRequest;
import com.ducnt.account.dto.response.AccountProfileResponse;
import com.ducnt.account.dto.response.UserCreationResponse;
import com.ducnt.account.dto.response.ValidationAccountResponse;

import java.util.UUID;

public interface IAccountService {
    UserCreationResponse activateUser(UserRegistrationRequest request);
    ValidationAccountResponse validateAccount(LoginRequest request);
    AccountProfileResponse getAccountProfile(String clientId);
}
