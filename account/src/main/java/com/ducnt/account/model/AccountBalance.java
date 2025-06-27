package com.ducnt.account.model;

import com.ducnt.account.dto.response.binance.FilteredAdvertisement;
import com.ducnt.account.enums.Currency;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
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
    BigDecimal availableBalance;
    BigDecimal actualBalance;
    String currency;
    BigDecimal reversedDebitAmount;
    LocalDate createdDate;
    LocalDate updatedDate;

    public static AccountBalance onCreateAccountBalanceDefault(UUID clientId) {
        return AccountBalance.builder()
                .clientId(clientId)
                .availableBalance(BigDecimal.valueOf(0))
                .actualBalance(BigDecimal.valueOf(0))
                .reversedDebitAmount(BigDecimal.valueOf(0))
                .currency(Currency.VND.name())
                .createdDate(LocalDate.now())
                .build();
    }

    public static AccountBalance onCreation(UUID clientId,FilteredAdvertisement adv) {
        String priceString = adv.getPrice();
        String tradableQuantityString = adv.getTradableQuantity();
        return AccountBalance.builder()
                .clientId(clientId)
                .availableBalance(calculateTradeValue(priceString, tradableQuantityString))
                .actualBalance(calculateTradeValue(priceString, tradableQuantityString))
                .reversedDebitAmount(BigDecimal.valueOf(0))
                .currency(Currency.VND.name())
                .createdDate(LocalDate.now())
                .build();
    }

    public static BigDecimal calculateTradeValue(String priceString, String tradableQuantityString) {
        if (priceString == null || priceString.trim().isEmpty() ||
                tradableQuantityString == null || tradableQuantityString.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            BigDecimal price = new BigDecimal(priceString);
            BigDecimal tradableQuantity = new BigDecimal(tradableQuantityString);
            return price.multiply(tradableQuantity);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
