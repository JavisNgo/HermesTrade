package com.ducnt.account.service;

import com.ducnt.account.dto.request.UserRegistrationRequest;

public interface IAccountService {
    void activateUser(UserRegistrationRequest request);
}
