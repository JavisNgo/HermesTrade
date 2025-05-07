package com.ducnt.activation.repository;

import com.ducnt.activation.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByEmail(String email);
}
