1. 초기 설정
   - db 실행: 도커 킨 후 아래 명령어 실행하면됩니다.
   ```bash
   docker-compose -f docker-compose.base.yml up -d
   ```
2. 각 MSA모듈 auth-api, post-api, user-api내부의 Application들을 각각 실행해주시면 됩니다.
3. 회원가입/로그인 시나리오
   회원가입: http://localhost:18082/users 엔드포인트에 POST 요청 (이메일, 비밀번호 포함).
   > > {
   > > "username": "beomsu123",
   > > "password": "1234",
   > > "email": "zeroskill2412@gmail.com",
   > > "age": 27,
   > > "phone": "010-8628-2287",
   > > "gender": "MALE"
   > > }
   > > 로그인: http://localhost:18081/auth/login 엔드포인트에 POST 요청 (이름, 이메일, 비밀번호, 이메일, 핸드폰번호, 성별 포함).
   > > 성공 시 JWT 토큰 발급.
4. 사용자 관리 시나리오
   사용자 조회: /user/{userId} 엔드포인트에 GET 요청.
   사용자 정보 수정: /user/{userId} 엔드포인트에 PUT 요청 (이름, 이메일 등).
   사용자 삭제: /user/{userId} 엔드포인트에 DELETE 요청.
5. 게시판 시나리오
   게시글 작성: /post 엔드포인트에 POST 요청.
   게시글 조회: /post/{postId} 엔드포인트에 GET 요청.
   게시글 수정: /post/{postId} 엔드포인트에 PUT 요청.
   게시글 삭제: /post/{postId} 엔드포인트에 DELETE 요청.