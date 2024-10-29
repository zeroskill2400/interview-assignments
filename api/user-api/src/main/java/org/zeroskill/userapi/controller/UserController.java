package org.zeroskill.userapi.controller;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.zeroskill.common.dto.UserDto;
import org.zeroskill.common.dto.request.UserCreateRequest;
import org.zeroskill.common.dto.request.UserUpdateRequest;
import org.zeroskill.common.dto.response.ApiResponse;
import org.zeroskill.common.service.UserService;

import static org.zeroskill.common.dto.request.UserCreateRequest.toUserDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping
    public ApiResponse<UserDto> addUser(@RequestBody UserCreateRequest request) {
        request.check();
        String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        UserDto userDto = UserDto.hashPassword(toUserDto(request), hashedPassword);
        UserDto savedUser = userService.addUser(userDto);
        return ApiResponse.of(savedUser);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDto> getUser(@PathVariable Long id) {
        return ApiResponse.of(userService.getUser(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        request.check();
        return ApiResponse.of(userService.updateUser(request, id));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
