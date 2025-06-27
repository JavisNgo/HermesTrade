package com.ducnt.account.model;

import com.ducnt.account.dto.request.auth.UserRegistrationRequest;
import com.ducnt.account.dto.response.binance.FilteredAdvertiser;
import com.ducnt.account.enums.AccountStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    UUID clientId;
    String email;
    String password;
    String fullName;
    LocalDate birthDate;
    String address;
    LocalDate createdDate;
    LocalDate updatedDate;
    AccountStatus status;

    public static Account fromUserRequest(UserRegistrationRequest request) {
        return Account.builder()
                .email(request.getEmail())
                .address(request.getAddress())
                .clientId(UUID.randomUUID())
                .fullName(request.getFullName())
                .birthDate(request.getBirthDate())
                .status(AccountStatus.ACTIVE)
                .createdDate(LocalDate.now())
                .build();
    }

    public static Account fromFilteredAdvertiser(FilteredAdvertiser advertiser) {
        return Account.builder()
                .email(normalizeNickName(advertiser.getNickName())+"@gmail.com")
                .clientId(UUID.randomUUID())
                .fullName(advertiser.getNickName())
                .status(AccountStatus.ACTIVE)
                .createdDate(LocalDate.now())
                .build();

    }

    public void onUpdatePassword(String password) {
        this.password = password;
    }

    public static String normalizeNickName(String nickName) {
        return nickName.toLowerCase().replaceAll("\\P{Alnum}", "");
    }
}
