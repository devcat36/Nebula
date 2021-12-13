package com.albireo.nebula.controller;

import com.albireo.nebula.model.User;
import com.albireo.nebula.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/account/login")
    public String login(@RequestBody User user) {
        return accountService.login(user.getUsername(), user.getPassword());
    }

    @PostMapping("/account")
    public String signUp(@RequestBody User user) {
        User signedUpUser = accountService.signUp(user);
        return accountService.login(signedUpUser.getUsername(), signedUpUser.getPassword());
    }

    @PutMapping("/account")
    public User modify(@RequestBody User user, @RequestHeader String sessionId) {
        return accountService.updateUser(sessionId, user);
    }
}
