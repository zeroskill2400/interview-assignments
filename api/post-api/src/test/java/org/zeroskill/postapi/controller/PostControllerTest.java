package org.zeroskill.postapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.zeroskill.common.dto.PostDto;
import org.zeroskill.common.dto.UserDto;
import org.zeroskill.common.dto.request.AddPostRequest;
import org.zeroskill.common.dto.request.UpdatePostRequest;
import org.zeroskill.common.entity.Gender;
import org.zeroskill.common.service.PostService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    void addPost_ShouldReturnPostDto() throws Exception {
        Long userId = 1L;
        AddPostRequest request = new AddPostRequest("Sample Title", "Sample Content");

        UserDto userDto = new UserDto(userId, "testUser", "test@example.com", "password", 25, "1234567890", Gender.MALE, LocalDateTime.now(), LocalDateTime.now());
        PostDto postDto = new PostDto(1L, request.title(), request.content(), userDto, LocalDateTime.now(), LocalDateTime.now());

        when(postService.addPost(any(AddPostRequest.class), eq(userId))).thenReturn(postDto);

        mockMvc.perform(post("/posts/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())  // JSON 응답 확인을 위해 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.id").value(postDto.id()))
                .andExpect(jsonPath("$.body.title").value(postDto.title()))
                .andExpect(jsonPath("$.body.content").value(postDto.content()));

        verify(postService).addPost(any(AddPostRequest.class), eq(userId));
    }

    @Test
    void getPost_ShouldReturnPostDto() throws Exception {
        Long postId = 1L;
        UserDto userDto = new UserDto(1L, "testUser", "test@example.com", "password", 25, "1234567890", Gender.MALE, LocalDateTime.now(), LocalDateTime.now());
        PostDto postDto = new PostDto(postId, "Sample Title", "Sample Content", userDto, LocalDateTime.now(), LocalDateTime.now());

        when(postService.getPost(postId)).thenReturn(postDto);

        mockMvc.perform(get("/posts/{id}", postId))
                .andDo(print())  // JSON 응답 확인을 위해 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.id").value(postDto.id()))
                .andExpect(jsonPath("$.body.title").value(postDto.title()))
                .andExpect(jsonPath("$.body.content").value(postDto.content()));

        verify(postService).getPost(postId);
    }

    @Test
    void updatePost_ShouldReturnUpdatedPostDto() throws Exception {
        Long postId = 1L;
        UpdatePostRequest request = new UpdatePostRequest("Updated Title", "Updated Content");

        UserDto userDto = new UserDto(1L, "testUser", "test@example.com", "password", 25, "1234567890", Gender.MALE, LocalDateTime.now(), LocalDateTime.now());
        PostDto updatedPostDto = new PostDto(postId, request.title(), request.content(), userDto, LocalDateTime.now(), LocalDateTime.now());

        when(postService.updatePost(any(UpdatePostRequest.class), eq(postId))).thenReturn(updatedPostDto);

        mockMvc.perform(put("/posts/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())  // JSON 응답 확인을 위해 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.id").value(updatedPostDto.id()))
                .andExpect(jsonPath("$.body.title").value(updatedPostDto.title()))
                .andExpect(jsonPath("$.body.content").value(updatedPostDto.content()));

        verify(postService).updatePost(any(UpdatePostRequest.class), eq(postId));
    }

    @Test
    void deletePost_ShouldCallServiceDeleteMethod() throws Exception {
        Long postId = 1L;
        Long userId = 1L;

        mockMvc.perform(delete("/posts/{id}/{userId}", postId, userId))
                .andDo(print())  // JSON 응답 확인을 위해 추가
                .andExpect(status().isOk());

        verify(postService).deletePost(postId, userId);
    }
}