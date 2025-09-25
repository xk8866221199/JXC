-- 创建JXC数据库
CREATE DATABASE IF NOT EXISTS jxc_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建开发用户
CREATE USER IF NOT EXISTS 'jxc_user'@'localhost' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON jxc_db.* TO 'jxc_user'@'localhost';
FLUSH PRIVILEGES;

-- 切换到jxc_db数据库
USE jxc_db;

-- 显示创建结果
SELECT 'Database jxc_db created successfully' as result;
SHOW DATABASES LIKE 'jxc_db';