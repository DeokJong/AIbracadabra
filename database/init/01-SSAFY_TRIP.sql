SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;

SET
    @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS,
    FOREIGN_KEY_CHECKS = 0;

SET
    @OLD_SQL_MODE = @@SQL_MODE,
    SQL_MODE = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema ssafytrip
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ssafytrip
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ssafytrip` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE `ssafytrip`;

--

DROP TABLE IF EXISTS `schedules`;

DROP TABLE IF EXISTS `plans`;

DROP TABLE IF EXISTS `bucketlists`;

DROP TABLE IF EXISTS `attractions`;

DROP TABLE IF EXISTS `guguns`;

DROP TABLE IF EXISTS `sidos`;

DROP TABLE IF EXISTS `contenttypes`;

DROP TABLE IF EXISTS `boards`;

DROP TABLE IF EXISTS `board_images`;

DROP TABLE IF EXISTS `comments`;

DROP TABLE IF EXISTS `members`;

DROP TABLE IF EXISTS `news`;

DROP TABLE IF EXISTS `plan`;

DROP TABLE IF EXISTS `image`;

DROP TABLE IF EXISTS `hot_place`;

--

CREATE TABLE `members` (
    `mno` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    `email` VARCHAR(45) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `role` VARCHAR(45) NOT NULL DEFAULT "USER",
    `profile` BLOB DEFAULT NULL,
    PRIMARY KEY (`mno`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `bucketlists` (
    `mno` INT NOT NULL,
    `ano` INT NOT NULL,
    PRIMARY KEY (`mno`, `ano`),
    CONSTRAINT `FK_members_TO_bucketlists_1` FOREIGN KEY (`mno`) REFERENCES `members` (`mno`),
    CONSTRAINT `FK_attractions_TO_bucketlists_1` FOREIGN KEY (`ano`) REFERENCES `attractions` (`no`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `news` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `title` varchar(200) NOT NULL COMMENT '뉴스 제목',
    `publish_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '발행 일시',
    `url_sido_code` smallint unsigned NOT NULL COMMENT '시도 코드',
    `summary` text NOT NULL COMMENT '기사 요약',
    `url` varchar(255) NOT NULL COMMENT '원문 URL',
    PRIMARY KEY (`id`),
    UNIQUE KEY `url` (`url`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '뉴스 테이블';

CREATE TABLE `boards` (
    `bno` INT NOT NULL AUTO_INCREMENT COMMENT '게시판 고유번호',
    `mno` INT NOT NULL COMMENT '작성자 회원번호 (members.mno 참조)',
    `board_type` ENUM('board', 'notice', 'qna') NOT NULL COMMENT '게시판 타입',
    `author` VARCHAR(45) NOT NULL COMMENT '작성자 이름',
    `views` INT NOT NULL DEFAULT 0 COMMENT '조회수',
    `title` VARCHAR(100) NOT NULL COMMENT '질문 제목',
    `content` LONGTEXT NOT NULL COMMENT '질문 내용',
    `created_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성일시',
    `updated_date` DATETIME NULL COMMENT '수정 완료일시',
    `visibility` ENUM('PUBLIC', 'PRIVATE') NOT NULL DEFAULT 'PUBLIC' COMMENT 'PUBLIC=공개, PRIVATE=비공개',
    PRIMARY KEY (`bno`),
    UNIQUE KEY `uq_boards_bno` (`bno`),
    CONSTRAINT `fk_boards_member` FOREIGN KEY (`mno`) REFERENCES `members` (`mno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '게시판 테이블';

CREATE TABLE `comments` (
    `cno` INT NOT NULL AUTO_INCREMENT COMMENT '댓글 고유번호',
    `bno` INT NOT NULL COMMENT '게시글 번호 (boards.bno 참조)',
    `mno` INT NOT NULL COMMENT '댓글 작성자 회원번호 (members.mno 참조)',
    `author` VARCHAR(45) NOT NULL COMMENT '댓글 작성자 이름',
    `content` TEXT NOT NULL COMMENT '댓글 내용',
    `created_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '댓글 작성일시',
    `updated_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 완료일시',
    PRIMARY KEY (`cno`),
    -- 외래키 제약
    CONSTRAINT `fk_comments_boards` FOREIGN KEY (`bno`) REFERENCES `boards` (`bno`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_comments_members` FOREIGN KEY (`mno`) REFERENCES `members` (`mno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '댓글 테이블';

CREATE TABLE `plan` (
    `pno` int NOT NULL AUTO_INCREMENT COMMENT 'Plan 식별자 (PK)',
    `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '여행 계획 제목',
    `mno` int NOT NULL COMMENT '계획을 세운 멤버의 ID (FK)',
    `schedules` json DEFAULT NULL COMMENT '외부 API로 가져온 일정 목록(JSON)',
    PRIMARY KEY (`pno`),
    KEY `idx_mno` (`mno`),
    CONSTRAINT `fk_plan_member` FOREIGN KEY (`mno`) REFERENCES `members` (`mno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '여행 계획 저장 테이블';

CREATE TABLE board_images (
    img_no BIGINT AUTO_INCREMENT PRIMARY KEY,
    bno INT NOT NULL, -- UNSIGNED 제거
    filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    size BIGINT NOT NULL,
    storage_path VARCHAR(512) NOT NULL,
    uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bno) REFERENCES boards (bno) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `image` (
    `ino` INT NOT NULL AUTO_INCREMENT,
    `filename` VARCHAR(255) NOT NULL,
    `content_type` VARCHAR(100) NOT NULL,
    `size` BIGINT NOT NULL,
    `storage_path` VARCHAR(500) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`ino`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `hot_place` (
    `hno` int NOT NULL AUTO_INCREMENT,
    `mno` int NOT NULL,
    `ino` int DEFAULT NULL,
    `overview` text COLLATE utf8mb4_unicode_ci,
    `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `map_x` decimal(13, 10) NOT NULL,
    `map_y` decimal(13, 10) NOT NULL,
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`hno`),
    KEY `idx_hp_mno` (`mno`),
    KEY `idx_hp_imo` (`ino`),
    CONSTRAINT `fk_hp_image` FOREIGN KEY (`ino`) REFERENCES `image` (`ino`) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_hp_member` FOREIGN KEY (`mno`) REFERENCES `members` (`mno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 13 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci

--

SET SQL_MODE = @OLD_SQL_MODE;

SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;

SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;