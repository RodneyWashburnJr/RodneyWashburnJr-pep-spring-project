package com.example.service;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    public Account register(String username, String password){
        if (accountRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        Account account = new Account(username, password);
        return accountRepository.save(account);
    }
    public Optional<Account> login(String username, String password) {
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isPresent() && account.get().getPassword().equals(password)) {
            return account;
        }
        return Optional.empty();
    }

}

