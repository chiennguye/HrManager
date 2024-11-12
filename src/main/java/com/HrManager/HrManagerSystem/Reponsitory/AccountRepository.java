package com.HrManager.HrManagerSystem.Reponsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HrManager.HrManagerSystem.Model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    Optional<Account> findByEmail(String email);
}
