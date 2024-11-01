-- 일반사용자 권한 설정 --
CREATE USER 'zeroskill2400'@'%' IDENTIFIED WITH mysql_native_password BY 'Zeroskill2400';
GRANT ALL PRIVILEGES ON *.* TO 'zeroskill2400'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
