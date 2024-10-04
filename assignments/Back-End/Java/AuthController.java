@RestController
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        if ("admin".equals(request.getUsername()) && "password".equals(request.getPassword())) {
            String token = Jwts.builder()
                    .setSubject(request.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + 1800000))  // 30분
                    .signWith(SignatureAlgorithm.HS512, "supersecretkey")
                    .compact();
            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/protected")
    public ResponseEntity<?> protectedRoute(@RequestHeader("Authorization") String token) {
        try {
            Jwts.parser().setSigningKey("supersecretkey").parseClaimsJws(token.replace("Bearer ", ""));
            return ResponseEntity.ok("Access granted");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
