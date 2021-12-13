package com.albireo.nebula.service;

import com.albireo.nebula.model.Session;
import com.albireo.nebula.model.User;
import com.albireo.nebula.repository.SessionRepository;
import com.albireo.nebula.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public String login(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new IllegalArgumentException("Login Failed"));
        return sessionRepository.save(new Session(user)).getSessionId();
    }

    public User signUp(User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already Exists");
        } else {
            User userToSave = new User(user);
            return userRepository.save(userToSave);
        }
    }

    public User updateUser(String sessionId, User user) {
        User userToUpdate = getUserFromSessionId(sessionId);
        userToUpdate.update(user);
        return userToUpdate;
    }

    public void deleteUser(String sessionId, String username) {
        authenticateAsAdmin(sessionId);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Not Found. id=" + username));
        userRepository.delete(user);
    }

    public User updateUserRole(String sessionId, String username, User.Role role) {
        authenticateAsAdmin(sessionId);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Not Found. id=" + username));
        user.setRole(role);
        return user;
    }

    public List<User> getAllUsers(String sessionId) {
        authenticateAsAdmin(sessionId);
        return userRepository.findAll();
    }

    public User getUserFromSessionId(String sessionId) {
        return sessionRepository
                .findBySessionIdAndExpireAfter(sessionId, new Date())
                .map(Session::getUser)
                .orElseThrow(() -> new IllegalArgumentException("Authentication Failed"));
    }

    public void authenticateAsAdmin(String sessionId) {
        User user = getUserFromSessionId(sessionId);
        if (user.getRole() != User.Role.ADMIN) {
            throw new IllegalArgumentException("Not Authorized");
        }
    }
}
