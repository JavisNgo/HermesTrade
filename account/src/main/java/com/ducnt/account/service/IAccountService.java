package com.ducnt.account.service;

import com.ducnt.account.dto.request.LoginRequest;
import com.ducnt.account.dto.request.UserRegistrationRequest;
import com.ducnt.account.dto.response.UserCreationResponse;
import com.ducnt.account.dto.response.ValidationAccountResponse;

public interface IAccountService {
    UserCreationResponse activateUser(UserRegistrationRequest request);
    ValidationAccountResponse validateAccount(LoginRequest request);
}
