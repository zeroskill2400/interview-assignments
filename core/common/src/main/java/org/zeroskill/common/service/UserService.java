package org.zeroskill.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.zeroskill.common.entity.User;
import org.zeroskill.common.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<String> validateUser(String username, String password) {
        return userRepository.findActiveUserByUsername(username)
                .filter(user -> user.getPassword().equals(password))  // 비밀번호 검증
                .map(User::getUsername);  // 유효한 경우 사용자 이름 반환
    }

    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow();
    }
}
