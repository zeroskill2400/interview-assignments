package org.zeroskill.common.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.zeroskill.common.dto.UserDto;
import org.zeroskill.common.dto.request.UserUpdateRequest;
import org.zeroskill.common.entity.User;
import org.zeroskill.common.exception.ErrorType;
import org.zeroskill.common.exception.InterviewException;
import org.zeroskill.common.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;

    public Optional<String> validateUser(String username, String password) {
        return userRepository.findActiveUserByUsername(username)
                .filter(user -> BCrypt.checkpw(password, user.getPassword()))  // 해시화된 비밀번호 비교
                .map(User::getUsername);
    }

    public UserDto getUser(@PathVariable Long id) {
        return User.toDto(userRepository.findActiveUserById(id).orElseThrow(() -> new InterviewException(ErrorType.DATA_NOT_FOUND, logger::error)));
    }

    public UserDto addUser(UserDto userDto) {
        User user = User.toEntity(userDto);
        return User.toDto(userRepository.save(user));
    }

    public UserDto updateUser(UserUpdateRequest request, Long id) {
        User user = userRepository.findActiveUserById(id)
                .orElseThrow(() -> new InterviewException(ErrorType.DATA_NOT_FOUND, logger::error));

        Optional.ofNullable(request.username()).ifPresent(user::updateUsername);
        Optional.ofNullable(request.password()).ifPresent(user::updatePassword);
        Optional.ofNullable(request.email()).ifPresent(user::updateEmail);
        Optional.ofNullable(request.age()).ifPresent(user::updateAge);
        Optional.ofNullable(request.phone()).ifPresent(user::updatePhone);
        Optional.ofNullable(request.gender()).ifPresent(user::updateGender);
        // 업데이트된 정보를 저장
        return User.toDto(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findActiveUserById(id)
                .orElseThrow(() -> new InterviewException(ErrorType.DATA_NOT_FOUND, logger::error));
        user.markAsDeleted();
        userRepository.save(user);
    }
}
