-- 일반사용자 권한 설정 --
CREATE USER IF NOT EXISTS 'zeroskill2400'@'%' IDENTIFIED WITH 'caching_sha2_password' BY 'Zeroskill2400';
GRANT ALL PRIVILEGES ON *.* TO 'zeroskill2400'@'%' WITH GRANT OPTION;

use db;

CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT INTO user (username, password, email, created_at, updated_at, deleted)
VALUES
    ('user1', 'password1', 'user1@example.com', NOW(), NOW(), false),
    ('user2', 'password2', 'user2@example.com', NOW(), NOW(), false),
    ('user3', 'password3', 'user3@example.com', NOW(), NOW(), false);

FLUSH PRIVILEGES;