package org.zeroskill.common.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuthUtil {

    @Setter
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

    public static boolean validateJwt(String token) {
        if (token == null || token.isEmpty()) {
            return false; // null 또는 빈 값일 경우 인증 실패로 처리
        }
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token.replace("Bearer ", ""));
            return true; // 인증 성공
        } catch (JwtException e) {
            return false; // 인증 실패
        }
    }
}
