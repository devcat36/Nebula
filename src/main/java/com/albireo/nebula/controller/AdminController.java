package com.albireo.nebula.controller;

import com.albireo.nebula.model.Contribution;
import com.albireo.nebula.model.User;
import com.albireo.nebula.service.AccountService;
import com.albireo.nebula.service.ContributionService;
import com.albireo.nebula.service.MarketplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AdminController {
    private final AccountService accountService;
    private final MarketplaceService marketplaceService;
    private final ContributionService contributionService;

    @PutMapping("/admin/contribute/approve/{request_id}")
    public Contribution approveContribution(@PathVariable Long request_id, @RequestHeader String sessionId) {
        return contributionService.approveContribution(sessionId, request_id);
    }

    @PutMapping("/admin/contribute/deny/{request_id}")
    public Contribution denyContribution(@PathVariable Long request_id, @RequestHeader String sessionId) {
        return contributionService.denyContribution(sessionId, request_id);
    }

    @DeleteMapping("/admin/marketplace/item/{itemId}")
    public void deleteItem(@PathVariable Long itemId, @RequestHeader String sessionId) {
        marketplaceService.deleteItem(itemId, sessionId);
    }

    @DeleteMapping("/admin/account/{username}")
    public void deleteAccount(@PathVariable String username, @RequestHeader String sessionId) {
        accountService.deleteUser(sessionId, username);
    }

    @PutMapping("/admin/account/{username}")
    public User updateUserRole(@PathVariable String username, @RequestParam User.Role role, @RequestHeader String sessionId) {
        return accountService.updateUserRole(sessionId, username, role);
    }

    @GetMapping("/admin/accounts")
    public List<User> getUsers(@RequestHeader String sessionId) {
        return accountService.getAllUsers(sessionId);
    }

    @GetMapping("/admin/contributions")
    public List<Contribution> getAllContributions(@RequestHeader String sessionId) {
        return contributionService.getAllContributions(sessionId);
    }
}
