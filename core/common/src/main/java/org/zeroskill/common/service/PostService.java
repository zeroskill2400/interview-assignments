package org.zeroskill.common.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.zeroskill.common.dto.PostDto;
import org.zeroskill.common.dto.request.AddPostRequest;
import org.zeroskill.common.dto.request.UpdatePostRequest;
import org.zeroskill.common.entity.Post;
import org.zeroskill.common.entity.User;
import org.zeroskill.common.exception.ErrorType;
import org.zeroskill.common.exception.InterviewException;
import org.zeroskill.common.repository.PostRepository;
import org.zeroskill.common.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private static final Logger logger = LogManager.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostDto addPost(AddPostRequest request, Long userId) {
        User user = userRepository.findActiveUserById(userId).orElseThrow(() -> new InterviewException(ErrorType.DATA_NOT_FOUND, logger::error));
        Post post = new Post(request.title(), request.content(), user);
        return Post.toDto(postRepository.save(post));
    }

    public PostDto getPost(Long id) {
        return Post.toDto(postRepository.findActivePostById(id).orElseThrow(() -> new InterviewException(ErrorType.DATA_NOT_FOUND, logger::error)));
    }

    public PostDto updatePost(UpdatePostRequest request, Long id) {
        Post post = postRepository.findActivePostById(id).orElseThrow(() -> new InterviewException(ErrorType.DATA_NOT_FOUND, logger::error));
        Optional.ofNullable(request.title()).ifPresent(post::updateTitle);
        Optional.ofNullable(request.content()).ifPresent(post::updateContent);
        Post updatedPost = postRepository.save(post);
        return Post.toDto(updatedPost);
    }

    public void deletePost(Long id, Long userId) {
        Post post = postRepository.findActivePostById(id)
                .orElseThrow(() -> new InterviewException(ErrorType.DATA_NOT_FOUND, logger::error));
        post.delete(userId);
        postRepository.save(post);
    }
}
