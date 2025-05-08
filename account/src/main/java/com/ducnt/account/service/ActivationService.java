package com.ducnt.account.service;

import com.ducnt.account.dto.request.UserRegistrationRequest;
import com.ducnt.account.exception.CustomException;
import com.ducnt.account.exception.ErrorResponse;
import com.ducnt.account.model.Account;
import com.ducnt.account.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActivationService implements IActivationService {

    UserRepository userRepository;
    ModelMapper modelMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public boolean activateUser(UserRegistrationRequest request) {
        try {
            Optional<Account> user = userRepository.findByEmail(request.getEmail());
            if (user.isPresent()) {
                throw new CustomException(ErrorResponse.USERNAME_EXISTS);
            }
            Account accountMapper = modelMapper.map(request, Account.class);
            accountMapper.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(accountMapper);
            return true;
        } catch (CustomException e) {
            throw e;
        } catch (Exception ex) {
            return false;
        }
    }
}
