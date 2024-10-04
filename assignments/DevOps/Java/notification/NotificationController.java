package com.example.notification;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    @PostMapping
    public String sendNotification(@RequestBody NotificationRequest request) {
        // User 서비스로부터 사용자 정보 조회
        RestTemplate restTemplate = new RestTemplate();
        User user = restTemplate.getForObject("http://user-service:8081/users/" + request.getUserId(), User.class);

        System.out.println("Sending notification to " + user.getEmail() + ": New post titled \"" + request.getTitle() + "\"");
        return "Notification sent";
    }
}

