package com.ducnt.account.service;

import com.ducnt.account.dto.request.UserRegistrationRequest;

public interface IActivationService {
    boolean activateUser(UserRegistrationRequest request);
}
