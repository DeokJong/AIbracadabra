-- 고유 제약 검사 비활성화 (대량 데이터 삽입 시 성능 향상)
SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;

-- 외래 키 제약 해제 (테이블 간 순환 참조 방지)
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;

-- 기존 SQL_MODE 저장 후 엄격 모드 설정 (데이터 무결성 강화)
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- 스키마 정의
-- -----------------------------------------------------
-- ssafytrip 스키마가 없으면 생성 (UTF8MB4 문자셋 사용)
CREATE SCHEMA IF NOT EXISTS `ssafytrip` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- ssafytrip 스키마로 변경
USE `ssafytrip`;

-- -----------------------------------------------------
-- 기존 테이블 삭제 (참조 무결성 고려하여 순서대로)
-- -----------------------------------------------------
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

DROP TABLE IF EXISTS `hotplace_likes`;

-- -----------------------------------------------------
-- 테이블: members
-- 설명: 사용자 계정 정보 저장
-- -----------------------------------------------------
CREATE TABLE `members` (
    `mno` INT NOT NULL AUTO_INCREMENT COMMENT '회원 고유번호 (PK)',
    `name` VARCHAR(45) NOT NULL COMMENT '회원 이름',
    `email` VARCHAR(45) NOT NULL UNIQUE COMMENT '회원 이메일 (고유)',
    `password` VARCHAR(100) NOT NULL COMMENT '비밀번호 (해시)',
    `role` VARCHAR(45) NOT NULL DEFAULT 'USER' COMMENT '사용자 역할 (USER, ADMIN 등)',
    `profile` BLOB DEFAULT NULL COMMENT '프로필 이미지 (BLOB)',
    PRIMARY KEY (`mno`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- 테이블: bucketlists
-- 설명: 회원과 관광지 간의 다대다 관계 매핑
-- -----------------------------------------------------
CREATE TABLE `bucketlists` (
    `mno` INT NOT NULL COMMENT '회원 번호 (FK)',
    `ano` INT NOT NULL COMMENT '관광지 번호 (FK)',
    PRIMARY KEY (`mno`, `ano`),
    CONSTRAINT `FK_members_TO_bucketlists_1` FOREIGN KEY (`mno`) REFERENCES `members` (`mno`),
    CONSTRAINT `FK_attractions_TO_bucketlists_1` FOREIGN KEY (`ano`) REFERENCES `attractions` (`no`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- 테이블: news
-- 설명: 뉴스 기사 정보 저장
-- -----------------------------------------------------
CREATE TABLE `news` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '뉴스 고유번호 (PK)',
    `title` VARCHAR(200) NOT NULL COMMENT '뉴스 제목',
    `publish_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '발행 일시',
    `url_sido_code` SMALLINT UNSIGNED NOT NULL COMMENT '시도 코드',
    `summary` TEXT NOT NULL COMMENT '기사 요약',
    `url` VARCHAR(255) NOT NULL COMMENT '원문 URL',
    PRIMARY KEY (`id`),
    UNIQUE KEY `url` (`url`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT='뉴스 테이블';

-- -----------------------------------------------------
-- 테이블: boards
-- 설명: 게시판 (일반, 공지, Q&A) 글 저장
-- -----------------------------------------------------
CREATE TABLE `boards` (
    `bno` INT NOT NULL AUTO_INCREMENT COMMENT '게시글 고유번호 (PK)',
    `mno` INT NOT NULL COMMENT '작성자 회원번호 (FK)',
    `board_type` ENUM('board','notice','qna') NOT NULL COMMENT '게시판 타입',
    `author` VARCHAR(45) NOT NULL COMMENT '작성자 이름',
    `views` INT NOT NULL DEFAULT 0 COMMENT '조회수',
    `title` VARCHAR(100) NOT NULL COMMENT '제목',
    `content` LONGTEXT NOT NULL COMMENT '내용',
    `created_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성 일시',
    `updated_date` DATETIME NULL COMMENT '수정 일시',
    `visibility` ENUM('PUBLIC','PRIVATE') NOT NULL DEFAULT 'PUBLIC' COMMENT '공개 여부',
    PRIMARY KEY (`bno`),
    UNIQUE KEY `uq_boards_bno` (`bno`),
    CONSTRAINT `fk_boards_member` FOREIGN KEY (`mno`) REFERENCES `members` (`mno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='게시판 테이블';

-- -----------------------------------------------------
-- 테이블: comments
-- 설명: 게시글 댓글 저장
-- -----------------------------------------------------
CREATE TABLE `comments` (
    `cno` INT NOT NULL AUTO_INCREMENT COMMENT '댓글 고유번호 (PK)',
    `bno` INT NOT NULL COMMENT '게시글 번호 (FK)',
    `mno` INT NOT NULL COMMENT '댓글 작성자 회원번호 (FK)',
    `author` VARCHAR(45) NOT NULL COMMENT '댓글 작성자 이름',
    `content` TEXT NOT NULL COMMENT '댓글 내용',
    `created_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성 일시',
    `updated_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    PRIMARY KEY (`cno`),
    CONSTRAINT `fk_comments_boards` FOREIGN KEY (`bno`) REFERENCES `boards` (`bno`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_comments_members` FOREIGN KEY (`mno`) REFERENCES `members` (`mno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='댓글 테이블';

-- -----------------------------------------------------
-- 테이블: plan
-- 설명: 여행 계획 저장 (JSON 일정 포함)
-- -----------------------------------------------------
CREATE TABLE `plan` (
    `pno` INT NOT NULL AUTO_INCREMENT COMMENT '여행 계획 고유번호 (PK)',
    `title` VARCHAR(255) NOT NULL COMMENT '계획 제목',
    `mno` INT NOT NULL COMMENT '회원 번호 (FK)',
    `schedules` JSON DEFAULT NULL COMMENT '일정 목록(JSON)',
    PRIMARY KEY (`pno`),
    KEY `idx_mno` (`mno`),
    CONSTRAINT `fk_plan_member` FOREIGN KEY (`mno`) REFERENCES `members` (`mno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='여행 계획 테이블';

-- -----------------------------------------------------
-- 테이블: board_images
-- 설명: 게시글 이미지 저장
-- -----------------------------------------------------
CREATE TABLE `board_images` (
    `img_no` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '이미지 고유번호 (PK)',
    `bno` INT NOT NULL COMMENT '게시글 번호 (FK)',
    `filename` VARCHAR(255) NOT NULL COMMENT '원본 파일명',
    `content_type` VARCHAR(100) NOT NULL COMMENT 'MIME 타입',
    `size` BIGINT NOT NULL COMMENT '파일 크기(바이트)',
    `storage_path` VARCHAR(512) NOT NULL COMMENT '저장 경로',
    `uploaded_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '업로드 일시',
    FOREIGN KEY (`bno`) REFERENCES `boards` (`bno`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- -----------------------------------------------------
-- 테이블: image
-- 설명: 공통 이미지 저장소
-- -----------------------------------------------------
CREATE TABLE `image` (
    `ino` INT NOT NULL AUTO_INCREMENT COMMENT '이미지 고유번호 (PK)',
    `filename` VARCHAR(255) NOT NULL COMMENT '원본 파일명',
    `content_type` VARCHAR(100) NOT NULL COMMENT 'MIME 타입',
    `size` BIGINT NOT NULL COMMENT '파일 크기(바이트)',
    `storage_path` VARCHAR(500) NOT NULL COMMENT '저장 경로',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    PRIMARY KEY (`ino`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='이미지 테이블';

-- -----------------------------------------------------
-- 테이블: hot_place
-- 설명: 사용자가 저장한 핫플레이스 정보
-- -----------------------------------------------------
CREATE TABLE `hot_place` (
    `hno` INT NOT NULL AUTO_INCREMENT COMMENT '핫플레이스 고유번호 (PK)',
    `mno` INT NOT NULL COMMENT '회원 번호 (FK)',
    `ino` INT DEFAULT NULL COMMENT '이미지 번호 (FK)',
    `overview` TEXT COMMENT '간단 설명',
    `title` VARCHAR(255) NOT NULL COMMENT '플레이스 제목',
    `map_x` DECIMAL(13,10) NOT NULL COMMENT '경도',
    `map_y` DECIMAL(13,10) NOT NULL COMMENT '위도',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    PRIMARY KEY (`hno`),
    KEY `idx_hp_mno` (`mno`),
    KEY `idx_hp_imo` (`ino`),
    CONSTRAINT `fk_hp_image` FOREIGN KEY (`ino`) REFERENCES `image` (`ino`) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_hp_member` FOREIGN KEY (`mno`) REFERENCES `members` (`mno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 13 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='핫플레이스 테이블';

CREATE TABLE `hotplace_likes` (
  `mno` int NOT NULL,
  `hno` int NOT NULL,
  PRIMARY KEY (`mno`,`hno`),
  KEY `hno` (`hno`),
  CONSTRAINT `hotplace_likes_ibfk_1` FOREIGN KEY (`mno`) REFERENCES `members` (`mno`) ON DELETE CASCADE,
  CONSTRAINT `hotplace_likes_ibfk_2` FOREIGN KEY (`hno`) REFERENCES `hot_place` (`hno`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- 원래 설정 복원
-- -----------------------------------------------------
SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
