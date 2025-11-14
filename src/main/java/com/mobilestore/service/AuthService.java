package com.mobilestore.service;

import com.mobilestore.entity.User;
import com.mobilestore.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final UserRepository userRepository = new UserRepository();

    public User authenticate(String username, String passwordPlain) {
        User user = userRepository.findByUsername(username);
        if (user == null) return null;
        boolean matches = passwordPlain.equals(user.getPassword()) ;
        return matches ? user : null;
    }
}
