package org.zeroskill.postapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zeroskill.common.dto.PostDto;
import org.zeroskill.common.dto.request.AddPostRequest;
import org.zeroskill.common.dto.request.UpdatePostRequest;
import org.zeroskill.common.dto.response.ApiResponse;
import org.zeroskill.common.service.PostService;
import org.zeroskill.common.service.UserService;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping("/{userId}")
    public ApiResponse<PostDto> addPost(@PathVariable("userId") Long userId, @RequestBody AddPostRequest request) {
        return ApiResponse.of(postService.addPost(request, userId));
    }

    @GetMapping("/{id}")
    public ApiResponse<PostDto> getPost(@PathVariable("id") Long id) {
        return ApiResponse.of(postService.getPost(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<PostDto> updatePost(@PathVariable("id") Long id, @RequestBody UpdatePostRequest request) {
        return ApiResponse.of(postService.updatePost(request, id));
    }

    @DeleteMapping("/{id}/{userId}")
    public void deletePost(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        postService.deletePost(id, userId);
    }
}