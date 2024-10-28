package org.zeroskill.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuthUtil {

    private static String secretKey;

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    private void init() {
        // @Value로 주입된 값을 static 변수에 할당
        secretKey = secret;
    }

    public static String makeJwt(String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + 1800000))  // 30분
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}
