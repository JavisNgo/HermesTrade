package com.ducnt.activation.service;

import com.ducnt.activation.dto.request.UserRegistrationRequest;

public interface IActivationService {
    boolean activateUser(UserRegistrationRequest request);
}
