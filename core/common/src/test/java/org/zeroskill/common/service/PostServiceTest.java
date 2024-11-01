package org.zeroskill.common.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zeroskill.common.dto.PostDto;
import org.zeroskill.common.dto.request.AddPostRequest;
import org.zeroskill.common.dto.request.UpdatePostRequest;
import org.zeroskill.common.entity.Gender;
import org.zeroskill.common.entity.Post;
import org.zeroskill.common.entity.User;
import org.zeroskill.common.exception.InterviewException;
import org.zeroskill.common.repository.PostRepository;
import org.zeroskill.common.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPost_ShouldReturnPostDto() {
        Long userId = 1L;
        AddPostRequest request = new AddPostRequest("Sample Title", "Sample Content");
        User user = new User("testUser", "test@example.com", "hashedPassword", 25, "1234567890", Gender.MALE);
        Post post = new Post(request.title(), request.content(), user);
        Post savedPost = new Post(request.title(), request.content(), user);

        when(userRepository.findActiveUserById(userId)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        PostDto postDto = postService.addPost(request, userId);

        assertEquals(savedPost.getId(), postDto.id());
        assertEquals(savedPost.getTitle(), postDto.title());
        assertEquals(savedPost.getContent(), postDto.content());

        verify(userRepository).findActiveUserById(userId);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void addPost_ShouldThrowException_WhenUserNotFound() {
        Long userId = 1L;
        AddPostRequest request = new AddPostRequest("Sample Title", "Sample Content");

        when(userRepository.findActiveUserById(userId)).thenReturn(Optional.empty());

        assertThrows(InterviewException.class, () -> postService.addPost(request, userId));

        verify(userRepository).findActiveUserById(userId);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void getPost_ShouldReturnPostDto() {
        Long postId = 1L;
        User user = new User("testUser", "test@example.com", "hashedPassword", 25, "1234567890", Gender.MALE);
        Post post = new Post("Sample Title", "Sample Content", user);

        when(postRepository.findActivePostById(postId)).thenReturn(Optional.of(post));

        PostDto postDto = postService.getPost(postId);

        assertEquals(post.getId(), postDto.id());
        assertEquals(post.getTitle(), postDto.title());
        assertEquals(post.getContent(), postDto.content());

        verify(postRepository).findActivePostById(postId);
    }

    @Test
    void getPost_ShouldThrowException_WhenPostNotFound() {
        Long postId = 1L;

        when(postRepository.findActivePostById(postId)).thenReturn(Optional.empty());

        assertThrows(InterviewException.class, () -> postService.getPost(postId));

        verify(postRepository).findActivePostById(postId);
    }

    @Test
    void updatePost_ShouldReturnUpdatedPostDto() {
        Long postId = 1L;
        UpdatePostRequest request = new UpdatePostRequest("Updated Title", "Updated Content");
        User user = new User("testUser", "test@example.com", "hashedPassword", 25, "1234567890", Gender.MALE);
        Post post = new Post("Sample Title", "Sample Content", user);
        Post updatedPost = new Post("Updated Title", "Updated Content", user);

        when(postRepository.findActivePostById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);

        PostDto postDto = postService.updatePost(request, postId);

        assertEquals(updatedPost.getId(), postDto.id());
        assertEquals(updatedPost.getTitle(), postDto.title());
        assertEquals(updatedPost.getContent(), postDto.content());

        verify(postRepository).findActivePostById(postId);
        verify(postRepository).save(post);
    }

    @Test
    void updatePost_ShouldThrowException_WhenPostNotFound() {
        Long postId = 1L;
        UpdatePostRequest request = new UpdatePostRequest("Updated Title", "Updated Content");

        when(postRepository.findActivePostById(postId)).thenReturn(Optional.empty());

        assertThrows(InterviewException.class, () -> postService.updatePost(request, postId));

        verify(postRepository).findActivePostById(postId);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void deletePost_ShouldDeletePost() {
        Long postId = 1L;
        Long userId = 1L;
        User user = new User("testUser", "test@example.com", "hashedPassword", 25, "1234567890", Gender.MALE);
        Post post = new Post("Sample Title", "Sample Content", user);

        when(postRepository.findActivePostById(postId)).thenReturn(Optional.of(post));

        postService.deletePost(postId, userId);

        verify(postRepository).findActivePostById(postId);
        verify(postRepository).save(post);
    }

    @Test
    void deletePost_ShouldThrowException_WhenPostNotFound() {
        Long postId = 1L;
        Long userId = 1L;

        when(postRepository.findActivePostById(postId)).thenReturn(Optional.empty());

        assertThrows(InterviewException.class, () -> postService.deletePost(postId, userId));

        verify(postRepository).findActivePostById(postId);
        verify(postRepository, never()).save(any(Post.class));
    }
}