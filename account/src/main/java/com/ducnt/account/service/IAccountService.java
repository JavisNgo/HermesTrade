package com.ducnt.account.service;

import com.ducnt.account.dto.request.UserRegistrationRequest;
import com.ducnt.account.dto.response.UserCreationResponse;

public interface IAccountService {
    UserCreationResponse activateUser(UserRegistrationRequest request);
}
