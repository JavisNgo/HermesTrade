package com.ducnt.account.model;

import com.ducnt.account.enums.Currency;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AccountBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    UUID clientId;
    long availableBalance;
    long actualBalance;
    String currency;
    long reversedDebitAmount;
    LocalDate createdDate;
    LocalDate updatedDate;

    public static AccountBalance onCreateAccountBalanceDefault(UUID clientId) {
        return AccountBalance.builder()
                .clientId(clientId)
                .availableBalance(0)
                .actualBalance(0)
                .reversedDebitAmount(0)
                .currency(Currency.USD.name())
                .createdDate(LocalDate.now())
                .build();
    }
}
