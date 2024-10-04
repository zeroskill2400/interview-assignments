package com.example.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);

        // Notification 서비스 호출
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity("http://notification-service:8083/notify", post, String.class);

        return createdPost;
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }
}

