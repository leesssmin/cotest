package com.example.sm.service;

import com.example.sm.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.sm.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User signup(String username, String password, String email){
        User user = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
        return userRepository.save(user);
    }

    public boolean login(String username, String password){
        return userRepository.findByUsername(username)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
}
