package com.ducnt.account.repository;

import com.ducnt.account.model.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, UUID> {
}
